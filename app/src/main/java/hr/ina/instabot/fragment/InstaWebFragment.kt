package hr.ina.instabot.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import hr.ina.instabot.R
import kotlinx.android.synthetic.main.fragment_instaweb.*

open class InstaWebFragment : BaseFragment() {

    companion object {
        fun newInstance(url : String) : InstaWebFragment {
            val fr = InstaWebFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            fr.arguments = bundle
            return fr
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instaweb, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        instaWebView.settings.javaScriptEnabled = true
        instaWebView.webViewClient = WebViewClient()
    }

    override fun onStart() {
        super.onStart()

        instaWebView.loadUrl(arguments?.getString("url", "https://instagram.com"))
    }

}