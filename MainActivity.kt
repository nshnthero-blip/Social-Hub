package com.socialhub.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var root: LinearLayout
    private lateinit var webView: WebView
    private lateinit var titleView: TextView
    private var currentUrl = ""
    private val blue = Color.rgb(37, 99, 235)
    private val dark = Color.rgb(15, 23, 42)
    private val ink = Color.rgb(25, 31, 45)
    private val pale = Color.rgb(246, 248, 252)

    data class Platform(val name: String, val symbol: String, val url: String)

    private val platforms = listOf(
        Platform("YouTube", "▶", "https://m.youtube.com/"),
        Platform("Facebook", "f", "https://www.facebook.com/"),
        Platform("TikTok", "♪", "https://www.tiktok.com/"),
        Platform("Instagram", "◎", "https://www.instagram.com/"),
        Platform("WhatsApp", "☎", "https://web.whatsapp.com/"),
        Platform("X", "𝕏", "https://x.com/")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CookieManager.getInstance().setAcceptCookie(true)
        showHome()
    }

    private fun baseLayout() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(pale)
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }
        setContentView(root)
    }

    private fun showHome() {
        baseLayout()
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(20), dp(19), dp(18), dp(19))
            setBackgroundColor(Color.WHITE)
        }
        val title = TextView(this).apply {
            text = "Social Hub"
            textSize = 25f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ink)
        }
        header.addView(title, LinearLayout.LayoutParams(0, -2, 1f))
        val searchIcon = TextView(this).apply {
            text = "⌕"
            textSize = 34f
            setTextColor(ink)
            gravity = Gravity.CENTER
            setOnClickListener { showSearchDialog() }
        }
        header.addView(searchIcon, LinearLayout.LayoutParams(dp(44), dp(44)))
        root.addView(header)
        root.addView(line())

        val body = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(18), dp(28), dp(18), dp(12))
        }
        val google = TextView(this).apply {
            text = "Google"
            textSize = 31f
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            setTextColor(Color.rgb(66, 133, 244))
            gravity = Gravity.CENTER
        }
        body.addView(google, LinearLayout.LayoutParams(-1, dp(48)))

        val search = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(10), 0, dp(8), 0)
            background = android.graphics.drawable.GradientDrawable().apply {
                setColor(Color.WHITE)
                cornerRadius = dp(28).toFloat()
                setStroke(dp(1), Color.rgb(222, 226, 235))
            }
        }
        val plus = TextView(this).apply {
            text = "+"
            textSize = 27f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setOnClickListener { showSearchDialog() }
        }
        search.addView(plus, LinearLayout.LayoutParams(dp(36), dp(52)))
        val query = EditText(this).apply {
            hint = "Search Google"
            textSize = 15f
            singleLine = true
            setTextColor(ink)
            setBackgroundColor(Color.TRANSPARENT)
            setPadding(dp(4), 0, dp(4), 0)
            setOnEditorActionListener { _, _, _ ->
                val q = text.toString().trim()
                if (q.isNotEmpty()) openUrl("https://www.google.com/search?q=" + Uri.encode(q), "Google")
                true
            }
        }
        search.addView(query, LinearLayout.LayoutParams(0, dp(52), 1f))
        val mic = TextView(this).apply {
            text = "🎙"
            textSize = 20f
            gravity = Gravity.CENTER
            setOnClickListener {
                try {
                    val intent = Intent("android.speech.action.RECOGNIZE_SPEECH")
                    intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form")
                    startActivityForResult(intent, 21)
                } catch (_: Exception) {
                    Toast.makeText(this@MainActivity, "Voice search is unavailable.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        search.addView(mic, LinearLayout.LayoutParams(dp(38), dp(52)))
        val lens = TextView(this).apply {
            text = "▣"
            textSize = 22f
            gravity = Gravity.CENTER
            setTextColor(Color.DKGRAY)
            setOnClickListener { openUrl("https://lens.google.com/", "Google Lens") }
        }
        search.addView(lens, LinearLayout.LayoutParams(dp(34), dp(52)))
        body.addView(search, LinearLayout.LayoutParams(-1, dp(56)).apply {
            topMargin = dp(8)
            bottomMargin = dp(28)
        })

        platforms.chunked(3).forEach { group ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }
            group.forEach { p ->
                val item = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                    setPadding(dp(3), dp(7), dp(3), dp(6))
                    setOnClickListener { openPlatform(p) }
                }
                val icon = TextView(this).apply {
                    text = p.symbol
                    textSize = if (p.name == "Facebook") 36f else 32f
                    typeface = Typeface.DEFAULT_BOLD
                    gravity = Gravity.CENTER
                    setTextColor(if (p.name == "Facebook") Color.rgb(24,119,242) else ink)
                    background = android.graphics.drawable.GradientDrawable().apply {
                        setColor(Color.WHITE)
                        cornerRadius = dp(15).toFloat()
                        setStroke(dp(1), Color.rgb(226, 230, 238))
                    }
                }
                item.addView(icon, LinearLayout.LayoutParams(dp(76), dp(76)))
                val label = TextView(this).apply {
                    text = p.name
                    textSize = 12.5f
                    gravity = Gravity.CENTER
                    setTextColor(ink)
                    maxLines = 1
                    setPadding(0, dp(6), 0, 0)
                }
                item.addView(label, LinearLayout.LayoutParams(-1, dp(30)))
                row.addView(item, LinearLayout.LayoutParams(0, dp(126), 1f))
            }
            body.addView(row, LinearLayout.LayoutParams(-1, dp(130)))
        }
        root.addView(body, LinearLayout.LayoutParams(-1, 0, 1f))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
            setPadding(dp(5), dp(7), dp(5), dp(7))
        }
        addNav(nav, "⌂", "Home") { showHome() }
        addNav(nav, "▢", "Chat") { showInfo("Chat", "Chat feature is planned for a future version.") }
        addNav(nav, "♧", "Notifications") { showInfo("Notifications", "Notifications from all platforms may not be available inside Social Hub.") }
        addNav(nav, "☰", "Menu") { showMenu() }
        root.addView(line())
        root.addView(nav)
    }

    private fun addNav(parent: LinearLayout, symbol: String, label: String, action: () -> Unit) {
        val item = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setOnClickListener { action() }
        }
        item.addView(TextView(this).apply {
            text = symbol
            textSize = 25f
            gravity = Gravity.CENTER
            setTextColor(blue)
        }, LinearLayout.LayoutParams(-1, dp(32)))
        item.addView(TextView(this).apply {
            text = label
            textSize = 10f
            gravity = Gravity.CENTER
            setTextColor(ink)
        }, LinearLayout.LayoutParams(-1, dp(20)))
        parent.addView(item, LinearLayout.LayoutParams(0, dp(54), 1f))
    }

    private fun line(): View = View(this).apply {
        setBackgroundColor(Color.rgb(225, 229, 237))
        layoutParams = LinearLayout.LayoutParams(-1, dp(1))
    }

    private fun showSearchDialog() {
        val edit = EditText(this).apply { hint = "Search Google or enter a website" }
        android.app.AlertDialog.Builder(this)
            .setTitle("Search")
            .setView(edit)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Go") { _, _ ->
                val q = edit.text.toString().trim()
                if (q.isNotEmpty()) {
                    val url = if (q.startsWith("https://") || q.startsWith("http://")) q
                    else "https://www.google.com/search?q=" + Uri.encode(q)
                    openUrl(url, "Search")
                }
            }.show()
    }

    private fun openPlatform(platform: Platform) = openUrl(platform.url, platform.name)

    private fun openUrl(url: String, pageTitle: String) {
        currentUrl = url
        baseLayout()
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(6), dp(4), dp(6), dp(4))
            setBackgroundColor(Color.WHITE)
        }
        val back = Button(this).apply {
            text = "←"
            isAllCaps = false
            setOnClickListener {
                if (::webView.isInitialized && webView.canGoBack()) webView.goBack() else showHome()
            }
        }
        titleView = TextView(this).apply {
            text = pageTitle
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(ink)
            gravity = Gravity.CENTER_VERTICAL
        }
        val external = Button(this).apply {
            text = "Open externally"
            isAllCaps = false
            textSize = 11f
            setOnClickListener {
                try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))) }
                catch (_: Exception) { Toast.makeText(this@MainActivity, "No compatible app found.", Toast.LENGTH_SHORT).show() }
            }
        }
        header.addView(back)
        header.addView(titleView, LinearLayout.LayoutParams(0, dp(48), 1f))
        header.addView(external)
        root.addView(header)
        webView = WebView(this)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                if (uri.scheme == "http" || uri.scheme == "https") {
                    currentUrl = uri.toString()
                    return false
                }
                return try {
                    startActivity(Intent(Intent.ACTION_VIEW, uri)); true
                } catch (_: Exception) {
                    Toast.makeText(this@MainActivity, "Could not open this link.", Toast.LENGTH_SHORT).show(); true
                }
            }
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: android.webkit.WebResourceError) {
                if (request.isForMainFrame) Toast.makeText(this@MainActivity, "Page failed to load. Try Open externally.", Toast.LENGTH_LONG).show()
            }
        }
        root.addView(webView, LinearLayout.LayoutParams(-1, 0, 1f))
        webView.loadUrl(url)
    }

    private fun showInfo(title: String, message: String) {
        android.app.AlertDialog.Builder(this).setTitle(title).setMessage(message)
            .setPositiveButton("OK", null).show()
    }

    private fun showMenu() {
        val choices = arrayOf("About Social Hub", "Open Google", "Back to Home")
        android.app.AlertDialog.Builder(this).setTitle("Menu").setItems(choices) { _, which ->
            when (which) {
                0 -> showInfo("About Social Hub", "Social Hub brings shortcuts to your favorite social platforms in one app. Some platforms may require their official app or browser.")
                1 -> openUrl("https://www.google.com/", "Google")
                2 -> showHome()
            }
        }.show()
    }

    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack() else showHome()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
