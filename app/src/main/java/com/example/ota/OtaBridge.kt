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

    @JavascriptInterface
    fun showLoyaltyNotification(title: String, message: String) {
        try {
            val channelId = "pk999_promotions_channel"
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? android.app.NotificationManager ?: return

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = android.app.NotificationChannel(
                    channelId,
                    "PK999 Promotions & Loyalty",
                    android.app.NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Special promotions, VIP events, and loyalty cashback announcements"
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                flags = android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = android.app.PendingIntent.getActivity(
                context,
                101,
                launchIntent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
            )

            val builder = androidx.core.app.NotificationCompat.Builder(context, channelId)
                .setSmallIcon(com.example.R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(androidx.core.app.NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            notificationManager.notify(777, builder.build())
        } catch (e: Exception) {
            android.util.Log.w("OtaBridge", "Loyalty notification error: ${e.message}")
        }
    }
}
