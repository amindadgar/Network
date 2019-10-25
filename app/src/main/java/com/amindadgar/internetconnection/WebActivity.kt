package com.amindadgar.internetconnection


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val murl:String = intent.getStringExtra("myUrl")
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true


        webView.webViewClient = object :WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                progressBar.isVisible = true
                return true
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.isVisible = false
            }
        }
        webView.loadUrl(murl)
        if (murl.isEmpty())
            Toast.makeText(this,"Url was not Entered!",Toast.LENGTH_LONG).show()
    }
}
