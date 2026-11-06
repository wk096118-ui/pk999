package com.example.ota

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * LiveUpdateManager handles Over-The-Air (OTA) dynamic live updates for PK999.
 * It provides:
 * 1. Dynamic retrieval of live hosted endpoints (Firebase Hosting / GitHub / Custom CDN).
 * 2. Background fetching and atomic caching of the latest frontend bundle to local storage.
 * 3. Offline fallback serving so users always get the latest version even without connectivity.
 * 4. Synchronization with Firebase RTDB app_config for real-time live pushes.
 */
object LiveUpdateManager {

    private const val TAG = "LiveUpdateManager"
    private const val PREFS_NAME = "pk999_ota_prefs"
    private const val KEY_CUSTOM_LIVE_URL = "custom_live_url"
    private const val KEY_LAST_SYNC_TS = "last_sync_timestamp"
    private const val KEY_LAST_CONTENT_LENGTH = "last_content_length"
    private const val KEY_OTA_VERSION = "ota_version"
    private const val BUNDLE_DIR = "ota_bundle"
    private const val BUNDLE_INDEX = "index.html"

    // Default Live Hosting Endpoints
    const val DEFAULT_HOSTED_URL = "https://pk999-9f93b.web.app/index.html"
    const val FIREBASE_APP_URL = "https://pk999-9f93b.firebaseapp.com/index.html"
    const val DEV_CONTAINER_URL = "https://ais-pre-pd6vbcqwmfeihoh6fdxwys-885918296574.asia-east1.run.app/index.html"
    const val FIREBASE_CONFIG_REST = "https://pk999-9f93b-default-rtdb.firebaseio.com/app_config.json"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Returns the currently active Live Hosted URL (custom override, remote config, or default).
     */
    fun getActiveLiveUrl(context: Context): String {
        val prefs = getPrefs(context)
        val custom = prefs.getString(KEY_CUSTOM_LIVE_URL, null)
        if (!custom.isNullOrBlank()) {
            return custom
        }
        return DEFAULT_HOSTED_URL
    }

    /**
     * Allows dynamically updating the Live Hosted URL (e.g. from Firebase or admin action).
     */
    fun setActiveLiveUrl(context: Context, url: String) {
        val clean = url.trim()
        getPrefs(context).edit().putString(KEY_CUSTOM_LIVE_URL, clean).apply()
        Log.i(TAG, "Active Live Hosted URL set to: $clean")
    }

    /**
     * Checks if device is currently connected to active internet.
     */
    fun isNetworkAvailable(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
            val network = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (_: Exception) {
            true // assume reachable if capability check throws
        }
    }

    /**
     * Returns the local directory where the latest OTA bundle is stored.
     */
    fun getOtaBundleDirectory(context: Context): File {
        val dir = File(context.filesDir, BUNDLE_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Returns the cached OTA file if it exists, is readable, and has valid size.
     */
    fun getCachedOtaFile(context: Context, filename: String = BUNDLE_INDEX): File? {
        val file = File(getOtaBundleDirectory(context), filename)
        if (file.exists() && file.isFile && file.length() > 5000) {
            return file
        }
        return null
    }

    /**
     * Returns an InputStream for the cached OTA file, or null if no valid OTA bundle exists.
     */
    fun getCachedOtaInputStream(context: Context, filename: String = BUNDLE_INDEX): InputStream? {
        val file = getCachedOtaFile(context, filename) ?: return null
        return try {
            file.inputStream()
        } catch (e: Exception) {
            Log.w(TAG, "Error opening cached OTA file: ${e.message}")
            null
        }
    }

    /**
     * Asynchronously synchronizes the latest bundle from the live hosted server.
     * When completed, it atomically saves to local storage and notifies callback.
     */
    fun syncLatestOtaBundle(
        context: Context,
        candidateUrl: String? = null,
        onComplete: ((Boolean, String) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. First fetch remote config to see if Firebase specified a new live_url or version
                checkRemoteFirebaseConfig(context)

                val targetUrl = candidateUrl ?: getActiveLiveUrl(context)
                Log.i(TAG, "Starting OTA background sync from: $targetUrl")

                // Try fetching with cache-busting timestamp
                val fetchUrl = if (targetUrl.contains("?")) "$targetUrl&_ota_ts=${System.currentTimeMillis()}" else "$targetUrl?_ota_ts=${System.currentTimeMillis()}"
                val connection = (URL(fetchUrl).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 7000
                    readTimeout = 12000
                    instanceFollowRedirects = true
                    setRequestProperty("User-Agent", "PK999-Android-OTA/2.5.0")
                    setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    setRequestProperty("Cache-Control", "no-cache")
                }

                val responseCode = connection.responseCode
                if (responseCode in 200..299) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line).append("\n")
                    }
                    reader.close()
                    connection.disconnect()

                    val content = stringBuilder.toString()
                    // Sanity check: must be valid HTML and contain PK999 or lottery markers
                    if (content.length > 5000 &&
                        (content.contains("pk999", ignoreCase = true) ||
                         content.contains("firebaseConfig", ignoreCase = true) ||
                         content.contains("Win Go", ignoreCase = true))
                    ) {
                        val bundleDir = getOtaBundleDirectory(context)
                        val tmpFile = File(bundleDir, "$BUNDLE_INDEX.tmp")
                        FileOutputStream(tmpFile).use { fos ->
                            fos.write(content.toByteArray(Charsets.UTF_8))
                            fos.flush()
                        }

                        val targetFile = File(bundleDir, BUNDLE_INDEX)
                        if (tmpFile.renameTo(targetFile) || (tmpFile.copyTo(targetFile, overwrite = true).also { tmpFile.delete() } != null)) {
                            getPrefs(context).edit()
                                .putLong(KEY_LAST_SYNC_TS, System.currentTimeMillis())
                                .putLong(KEY_LAST_CONTENT_LENGTH, targetFile.length())
                                .apply()

                            Log.i(TAG, "OTA live update successfully downloaded and cached (${targetFile.length()} bytes)")
                            withContext(Dispatchers.Main) {
                                onComplete?.invoke(true, "OTA Live Update applied (${targetFile.length()} bytes)")
                            }
                            return@launch
                        }
                    } else {
                        Log.w(TAG, "Downloaded content failed verification checks (length: ${content.length})")
                    }
                } else {
                    Log.w(TAG, "Server responded with HTTP $responseCode for $targetUrl")
                }
            } catch (e: Exception) {
                Log.w(TAG, "OTA live sync encountered error: ${e.message}")
            }

            withContext(Dispatchers.Main) {
                onComplete?.invoke(false, "OTA live sync finished or skipped")
            }
        }
    }

    /**
     * Checks Firebase Realtime Database for dynamic live URL and force-update flags.
     */
    private fun checkRemoteFirebaseConfig(context: Context) {
        try {
            val url = URL(FIREBASE_CONFIG_REST)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 4000
                readTimeout = 4000
                requestMethod = "GET"
            }
            if (conn.responseCode == 200) {
                val text = conn.inputStream.bufferedReader().use { it.readText() }
                conn.disconnect()
                if (text.isNotBlank() && text != "null") {
                    val json = JSONObject(text)
                    if (json.has("live_url")) {
                        val liveUrl = json.getString("live_url")
                        if (liveUrl.isNotBlank() && liveUrl.startsWith("http")) {
                            setActiveLiveUrl(context, liveUrl)
                        }
                    }
                    if (json.has("version")) {
                        val ver = json.optString("version", "")
                        if (ver.isNotBlank()) {
                            getPrefs(context).edit().putString(KEY_OTA_VERSION, ver).apply()
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // Non-blocking background check
        }
    }
}
