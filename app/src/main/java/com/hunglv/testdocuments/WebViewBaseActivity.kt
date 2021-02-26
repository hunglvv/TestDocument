package com.hunglv.testdocuments

import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hunglv.testdocuments.databinding.WebviewBinding

abstract class WebViewBaseActivity : AppCompatActivity() {
    private lateinit var binding: WebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        window?.setFlags(16777216,16777216)
        binding.browser.apply {
            webViewClient = WebViewClient()
//            scrollBarStyle = 33554432
            isScrollbarFadingEnabled = true
//            setLayerType( 2, null)
            webChromeClient = WebChromeClient()

            val webSetting = settings
            webSetting.javaScriptEnabled = true
//            webSetting.cacheMode = 2
            webSetting.domStorageEnabled = true
//            webSetting.setRenderPriority(WebwebSetting.RenderPriority.HIGH)
            webSetting.loadWithOverviewMode = true
//            webSetting.layoutAlgorithm = WebwebSetting.LayoutAlgorithm.SINGLE_COLUMN
            webSetting.domStorageEnabled = true
            webSetting.allowFileAccess = true
            webSetting.allowContentAccess = true
            webSetting.setAppCacheEnabled(true)
            webSetting.databaseEnabled = true
//            webSetting.pluginState = WebwebSetting.PluginState.ON
            webSetting.allowFileAccessFromFileURLs = true
            webSetting.allowUniversalAccessFromFileURLs = true
            webSetting.mediaPlaybackRequiresUserGesture = false
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode != 4 || !binding.browser.canGoBack())
            return super.onKeyDown(keyCode, event)
        binding.browser.goBack()
        return false
    }

    override fun onResume() {
        binding.browser.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.browser.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.browser.removeAllViews()
        binding.browser.destroy()
        super.onDestroy()
    }
}