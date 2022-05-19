package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentGetWebsiteBinding

class GetWebsiteFragment : Fragment() {
    private var _binding: FragmentGetWebsiteBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentGetWebsiteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString("iUrl").toString()
        loadData(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadData(url: String){
        webView = binding.webView
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        webView.loadUrl("https://www.drugs.com${url}")
    }

}