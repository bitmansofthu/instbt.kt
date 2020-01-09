package hr.ina.instabot.fragment

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import hr.ina.instabot.R
import hr.ina.instabot.util.Settings
import kotlinx.android.synthetic.main.fragment_instaweb.*

class LogoutFragment : InstaWebFragment() {

    companion object {
        fun newInstance() : LogoutFragment {
            val fr = LogoutFragment()
            val bundle = Bundle()
            bundle.putString("url", "https://instagram.com")
            fr.arguments = bundle
            return fr
        }
    }

    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Logout"

        instaWebView.webViewClient = object: WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (request?.url?.path?.contains("logout") == true) {
                    Settings.clearHeaderStorage(context!!)

                    activity?.runOnUiThread {
                        Toast.makeText(context!!, R.string.app_logging_out, Toast.LENGTH_SHORT).show()
                    }

                    handler.postDelayed({
                        navigation.login().show(false,null)
                    }, 5 * 1000)
                }

                return null
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}