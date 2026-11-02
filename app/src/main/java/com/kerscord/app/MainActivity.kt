package com.kerscord.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private val discordHost = "discord.com"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(9, 9, 9)
        window.navigationBarColor = Color.rgb(9, 9, 9)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(9, 9, 9))
        }
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(18, 10, 18, 10)
            setBackgroundColor(Color.rgb(9, 9, 9))
        }
        val brand = TextView(this).apply {
            text = "KERSCORD"
            textSize = 18f
            letterSpacing = .18f
            setTextColor(Color.WHITE)
            typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD)
        }
        val spacer = View(this)
        val browserButton = Button(this).apply {
            text = "OPEN IN BROWSER"
            textSize = 10f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(38, 38, 38))
            setOnClickListener { openExternal("https://discord.com/app") }
        }
        header.addView(brand)
        header.addView(spacer, LinearLayout.LayoutParams(0, 1, 1f))
        header.addView(browserButton)
        root.addView(header)

        webView = WebView(this).apply {
            setBackgroundColor(Color.rgb(9, 9, 9))
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(false)
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val uri = request?.url ?: return false
                    val host = uri.host.orEmpty()
                    return if (host == discordHost || host.endsWith(".discord.com") || host.endsWith(".discordapp.com")) {
                        false
                    } else {
                        openExternal(uri.toString())
                        true
                    }
                }
            }
        }

        root.addView(webView, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
        if (savedInstanceState == null) webView.loadUrl("https://discord.com/app")
        else webView.restoreState(savedInstanceState)
    }

    private fun openExternal(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: Exception) {
            // No compatible browser installed.
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::webView.isInitialized) webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Android API")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    override fun onDestroy() {
        if (::webView.isInitialized) webView.destroy()
        super.onDestroy()
    }
}
