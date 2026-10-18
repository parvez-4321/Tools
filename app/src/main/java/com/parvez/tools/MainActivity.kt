package com.parvez.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.SafeBrowsingResponse
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private var adView: AdView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(10, 13, 22))
        }

        webView = WebView(this).apply {
            setBackgroundColor(Color.rgb(10, 13, 22))
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.allowFileAccessFromFileURLs = false
            settings.allowUniversalAccessFromFileURLs = false
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(false)
            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
            settings.safeBrowsingEnabled = true
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, false)
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val uri = request.url
                    // Only app-bundled local pages are allowed inside this WebView.
                    if (uri.scheme == "file" && uri.host == "android_asset" &&
                        (uri.path == "/index.html" || uri.path?.startsWith("/") == true)) return false

                    // Open external HTTP(S) links in the system browser, never in the app WebView.
                    if (uri.scheme == "https" || uri.scheme == "http") {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, uri))
                        } catch (_: Exception) {
                            // Ignore links when no compatible browser is installed.
                        }
                    }
                    return true
                }
            }
        }

        root.addView(webView, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        // Google's official sample banner unit ID. Replace only after testing is complete.
        MobileAds.initialize(this) {}
        adView = AdView(this).apply {
            adUnitId = "ca-app-pub-3940256099942544/9214589741"
            setAdSize(AdSize.BANNER)
        }
        val adContainer = LinearLayout(this).apply {
            gravity = Gravity.CENTER
            setBackgroundColor(Color.rgb(10, 13, 22))
            addView(adView, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
        root.addView(adContainer, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        setContentView(root)
        if (savedInstanceState == null) {
            webView.loadUrl("file:///android_asset/index.html")
        } else {
            webView.restoreState(savedInstanceState)
        }
        adView?.loadAd(AdRequest.Builder().build())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Android API")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    override fun onPause() {
        adView?.pause()
        webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (::webView.isInitialized) webView.onResume()
        adView?.resume()
    }

    override fun onDestroy() {
        adView?.destroy()
        if (::webView.isInitialized) webView.destroy()
        super.onDestroy()
    }
}
