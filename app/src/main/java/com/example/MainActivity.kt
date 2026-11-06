package com.example

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ota.LiveUpdateManager
import com.example.ota.OtaBridge
import com.example.ui.theme.CasinoBackground
import com.example.ui.theme.MyApplicationTheme
import java.io.ByteArrayInputStream
import java.io.InputStream

class MainActivity : ComponentActivity() {

    private var currentWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WebView.setWebContentsDebuggingEnabled(true)
        setContent {
            MyApplicationTheme {
                PK999WebViewContainer(
                    onRegisterWebView = { wv -> currentWebView = wv }
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PK999WebViewContainer(
    onRegisterWebView: (WebView) -> Unit = {}
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var reloadCount by remember { mutableIntStateOf(0) }

    BackHandler(enabled = canGoBack) {
        webViewInstance?.goBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        key(reloadCount) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(0xFF1E1E2D.toInt())

                        // Enable full cookie persistence & cross-origin cookies for OAuth popup
                        val cookieManager = CookieManager.getInstance()
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setAcceptThirdPartyCookies(this, true)

                        val defaultUa = settings.userAgentString
                        val sanitizedUa = defaultUa.replace("; wv", "").replace(Regex("Version/\\d+\\.\\d+ "), "")
                        settings.apply {
                            userAgentString = sanitizedUa
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            allowFileAccess = true
                            allowContentAccess = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            mediaPlaybackRequiresUserGesture = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            cacheMode = WebSettings.LOAD_DEFAULT
                            setSupportMultipleWindows(true)
                            javaScriptCanOpenWindowsAutomatically = true
                        }

                        // Register OTA bridge for JavaScript to query updates and reload
                        addJavascriptInterface(OtaBridge(context) { webViewInstance }, "AndroidOTA")

                        // Trigger background OTA bundle synchronization from live URL
                        LiveUpdateManager.syncLatestOtaBundle(context) { updated, _ ->
                            if (updated) {
                                post {
                                    loadUrl("http://localhost/index.html")
                                }
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onCreateWindow(
                                view: WebView?,
                                isDialog: Boolean,
                                isUserGesture: Boolean,
                                resultMsg: Message?
                            ): Boolean {
                                val popupContext = view?.context ?: return false
                                val popupWebView = WebView(popupContext).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    // Enable third-party cookies on popup window for Google/Firebase OAuth handshake
                                    cookieManager.setAcceptThirdPartyCookies(this, true)
                                    val popUa = settings.userAgentString
                                    settings.apply {
                                        userAgentString = popUa.replace("; wv", "").replace(Regex("Version/\\d+\\.\\d+ "), "")
                                        javaScriptEnabled = true
                                        domStorageEnabled = true
                                        databaseEnabled = true
                                        allowFileAccess = true
                                        allowContentAccess = true
                                        loadWithOverviewMode = true
                                        useWideViewPort = true
                                        mediaPlaybackRequiresUserGesture = false
                                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                        setSupportMultipleWindows(true)
                                        javaScriptCanOpenWindowsAutomatically = true
                                    }
                                }

                                val dialog = Dialog(popupContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
                                    setContentView(popupWebView)
                                    setOnCancelListener {
                                        try {
                                            popupWebView.destroy()
                                        } catch (_: Exception) {}
                                    }
                                    show()
                                }

                                popupWebView.webChromeClient = object : WebChromeClient() {
                                    override fun onCloseWindow(window: WebView?) {
                                        try {
                                            dialog.dismiss()
                                            window?.destroy()
                                        } catch (_: Exception) {}
                                    }
                                }

                                popupWebView.webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(v: WebView?, req: WebResourceRequest?): Boolean {
                                        // Allow popup to load Google OAuth and callback URLs smoothly
                                        return false
                                    }

                                    override fun onPageFinished(v: WebView?, url: String?) {
                                        super.onPageFinished(v, url)
                                        val u = url.orEmpty()
                                        // Once the auth flow completes and redirects back to localhost, allow token transfer then close dialog
                                        if (u.contains("localhost") || u.contains("127.0.0.1")) {
                                            v?.postDelayed({
                                                if (dialog.isShowing) {
                                                    try {
                                                        dialog.dismiss()
                                                        v.destroy()
                                                    } catch (_: Exception) {}
                                                }
                                            }, 1200)
                                        }
                                    }

                                    override fun shouldInterceptRequest(
                                        v: WebView?,
                                        req: WebResourceRequest?
                                    ): WebResourceResponse? {
                                        val url = req?.url ?: return null
                                        val host = url.host ?: return null
                                        if (host.equals("localhost", ignoreCase = true) || host.equals("127.0.0.1", ignoreCase = true)) {
                                            val assetManager = v?.context?.assets ?: return null
                                            val inputStream = try { assetManager.open("index.html") } catch (_: Exception) { null }
                                            if (inputStream != null) {
                                                return WebResourceResponse(
                                                    "text/html",
                                                    "UTF-8",
                                                    200,
                                                    "OK",
                                                    mapOf(
                                                        "Access-Control-Allow-Origin" to "*",
                                                        "Access-Control-Allow-Methods" to "GET, POST, OPTIONS, HEAD",
                                                        "Access-Control-Allow-Headers" to "*"
                                                    ),
                                                    inputStream
                                                )
                                            }
                                        }
                                        return super.shouldInterceptRequest(v, req)
                                    }

                                    override fun onRenderProcessGone(v: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                                        try {
                                            dialog.dismiss()
                                            v?.let { wv ->
                                                (wv.parent as? ViewGroup)?.removeView(wv)
                                                wv.destroy()
                                            }
                                        } catch (_: Exception) {}
                                        return true
                                    }
                                }

                                val transport = resultMsg?.obj as? WebView.WebViewTransport
                                transport?.webView = popupWebView
                                resultMsg?.sendToTarget()
                                return true
                            }
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                canGoBack = view?.canGoBack() == true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                canGoBack = view?.canGoBack() == true
                            }

                            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                val reqUrl = request?.url ?: return false
                                val reqHost = reqUrl.host.orEmpty()
                                // Allow localhost URLs (including redirect parameters) to proceed to shouldInterceptRequest
                                if (reqHost.equals("localhost", ignoreCase = true) || reqHost.equals("127.0.0.1", ignoreCase = true)) {
                                    return false
                                }
                                return false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: android.webkit.WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                val reqUrl = request?.url?.toString().orEmpty()
                                if (request?.isForMainFrame == true && (reqUrl.contains("localhost") || reqUrl.contains("127.0.0.1"))) {
                                    view?.loadUrl("http://localhost/index.html")
                                }
                            }

                            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                                view?.let { wv ->
                                    (wv.parent as? ViewGroup)?.removeView(wv)
                                    wv.destroy()
                                }
                                webViewInstance = null
                                reloadCount++
                                return true
                            }

                            override fun shouldInterceptRequest(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): WebResourceResponse? {
                                val url = request?.url ?: return null
                                val host = url.host ?: return null

                                if (host.equals("localhost", ignoreCase = true) || host.equals("127.0.0.1", ignoreCase = true)) {
                                    val rawPath = url.path.orEmpty().removePrefix("/")
                                    // Normalize app/src or SPA paths back to index.html
                                    val assetPath = when {
                                        rawPath.isEmpty() || rawPath == "index.html" || rawPath.contains("app/src") -> "index.html"
                                        !rawPath.contains(".") -> "index.html"
                                        else -> rawPath
                                    }

                                    val ctx = view?.context
                                    val assetManager = ctx?.assets ?: return null

                                    // Check for Over-The-Air updated bundle first (internal storage)
                                    val otaStream = if (ctx != null) LiveUpdateManager.getCachedOtaInputStream(ctx, assetPath) else null

                                    val inputStream: InputStream? = otaStream ?: try {
                                        assetManager.open(assetPath)
                                    } catch (_: Exception) {
                                        try {
                                            assetManager.open("index.html")
                                        } catch (_: Exception) {
                                            null
                                        }
                                    }

                                    if (inputStream != null) {
                                        val mimeType = when {
                                            assetPath.endsWith(".html") -> "text/html"
                                            assetPath.endsWith(".js") -> "application/javascript"
                                            assetPath.endsWith(".css") -> "text/css"
                                            assetPath.endsWith(".json") -> "application/json"
                                            assetPath.endsWith(".png") -> "image/png"
                                            assetPath.endsWith(".jpg") || assetPath.endsWith(".jpeg") -> "image/jpeg"
                                            assetPath.endsWith(".svg") -> "image/svg+xml"
                                            assetPath.endsWith(".ico") -> "image/x-icon"
                                            assetPath.endsWith(".woff2") -> "font/woff2"
                                            assetPath.endsWith(".ttf") -> "font/ttf"
                                            else -> "text/html"
                                        }
                                        val headers = mapOf(
                                            "Access-Control-Allow-Origin" to "*",
                                            "Access-Control-Allow-Methods" to "GET, POST, OPTIONS, HEAD",
                                            "Access-Control-Allow-Headers" to "*"
                                        )
                                        return WebResourceResponse(mimeType, "UTF-8", 200, "OK", headers, inputStream)
                                    } else {
                                        // Fallback to index.html to guarantee ERR_CONNECTION_REFUSED is never triggered on localhost
                                        val fallback = try { assetManager.open("index.html") } catch (_: Exception) { null }
                                        if (fallback != null) {
                                            return WebResourceResponse(
                                                "text/html",
                                                "UTF-8",
                                                200,
                                                "OK",
                                                mapOf("Access-Control-Allow-Origin" to "*"),
                                                fallback
                                            )
                                        }
                                        return WebResourceResponse(
                                            "text/plain",
                                            "UTF-8",
                                            404,
                                            "Not Found",
                                            mapOf("Access-Control-Allow-Origin" to "*"),
                                            ByteArrayInputStream(ByteArray(0))
                                        )
                                    }
                                }
                                return super.shouldInterceptRequest(view, request)
                            }
                        }

                        loadUrl("http://localhost/index.html")
                        webViewInstance = this
                        onRegisterWebView(this)
                    }
                },
                update = { webView ->
                    webViewInstance = webView
                    onRegisterWebView(webView)
                    canGoBack = webView.canGoBack()
                }
            )
        }
    }
}
