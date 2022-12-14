package com.example.plantpet.fragments

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.plantpet.R
import com.example.plantpet.databinding.FragmentBookmarkBinding
import com.example.plantpet.databinding.FragmentStoreBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreFragment : Fragment() {

    private lateinit var binding : FragmentStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val webView : WebView = view.findViewById(R.id.storeWebView)

        webView.apply{
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(false)
            settings.loadsImagesAutomatically = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = false
            settings.displayZoomControls = false

            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.setAppCacheEnabled(false)
            settings.domStorageEnabled = true
            settings.allowContentAccess
            settings.userAgentString = "app"
            settings.defaultTextEncodingName = "UTF-8"
            settings.databaseEnabled = true
        }

        webView.loadUrl("https://m.mojongsijang.com")



        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false)
        binding.hometap.setOnClickListener {
            it.findNavController().navigate(R.id.action_storeFragment_to_homeFragment)
        }
        binding.sensortap.setOnClickListener {
            it.findNavController().navigate(R.id.action_storeFragment_to_sensorFragment)
        }
        binding.talktap.setOnClickListener {
            it.findNavController().navigate(R.id.action_storeFragment_to_talkFragment)
        }

        return view
    }

}