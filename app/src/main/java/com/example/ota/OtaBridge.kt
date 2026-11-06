package com.example.ota

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OtaBridge(
    private val context: Context,
    private val webViewProvider: () -> WebView?
) {

    @JavascriptInterface
    fun getOtaVersion(): String {
        return "2.5.0-OTA"
    }

    @JavascriptInterface
    fun getActiveLiveUrl(): String {
        return LiveUpdateManager.getActiveLiveUrl(context)
    }

    @JavascriptInterface
    fun setLiveUrl(url: String) {
        if (url.isNotBlank() && url.startsWith("http")) {
            LiveUpdateManager.setActiveLiveUrl(context, url)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "OTA Live URL updated to: $url", Toast.LENGTH_SHORT).show()
                checkForLiveUpdate()
            }
        }
    }

    @JavascriptInterface
    fun checkForLiveUpdate() {
        LiveUpdateManager.syncLatestOtaBundle(context) { success, msg ->
            CoroutineScope(Dispatchers.Main).launch {
                if (success) {
                    Toast.makeText(context, "Latest OTA Live Update downloaded! Reloading...", Toast.LENGTH_SHORT).show()
                    val wv = webViewProvider()
                    wv?.reload()
                }
            }
        }
    }

    @JavascriptInterface
    fun isOnline(): Boolean {
        return LiveUpdateManager.isNetworkAvailable(context)
    }

    @JavascriptInterface
    fun hasCachedOtaBundle(): Boolean {
        return LiveUpdateManager.getCachedOtaFile(context) != null
    }

    @JavascriptInterface
    fun reloadApp() {
        CoroutineScope(Dispatchers.Main).launch {
            val wv = webViewProvider()
            wv?.clearCache(true)
            wv?.reload()
        }
    }
}
