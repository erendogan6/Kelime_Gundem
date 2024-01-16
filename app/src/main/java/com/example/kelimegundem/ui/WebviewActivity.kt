package com.example.kelimegundem.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import com.example.kelimegundem.R
import com.example.kelimegundem.databinding.ActivityWebviewBinding

class WebviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = if (intent.getStringExtra("url")!= null) {
            intent.getStringExtra("url")
        } else {
            "www.google.com"
        }
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val webView = binding.myWebView

        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url!!)

        val isSecure = isHttps(url)
        binding.ImageViewSecure.setImageResource(
            if (isSecure) R.drawable.secure
            else R.drawable.unsecure
        )

        onBackPressedDispatcher.addCallback {
            if (webView.canGoBack()) {
                webView.goBack()
            }
            else{
                finish()
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                val formattedUrl = formatUrlForDisplay(url!!)
                binding.urlTextView.text = formattedUrl
                TooltipCompat.setTooltipText(binding.urlTextView, url)
            }
        }
        binding.closeButton.setOnClickListener { finish() }
        binding.refreshButton.setOnClickListener { webView.reload() }

    }

    fun formatUrlForDisplay(url: String): String {
        return try {
            val uri = Uri.parse(url)
            val host = uri.host?.removePrefix("www.") ?: ""
            val pathSegments = uri.pathSegments
            val titleSegment = if (pathSegments.isNotEmpty()) pathSegments[0] else ""
            "$host/$titleSegment"
        } catch (e: Exception) {
            "Invalid URL"
        }
    }

    private fun isHttps(url: String): Boolean {
        return try {
            val uri = Uri.parse(url)
            uri.scheme?.equals("https", ignoreCase = true) ?: false
        } catch (e: Exception) {
            false
        }
    }

}