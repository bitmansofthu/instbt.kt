package hr.ina.instabot.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import hr.ina.instabot.R
import hr.ina.instabot.core.InstaResponse
import hr.ina.instabot.core.InstaResponseCallback
import hr.ina.instabot.core.WebInstaRequest
import hr.ina.instabot.network.InstaCookieManager
import hr.ina.instabot.util.Settings
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.IOException

class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() : LoginFragment {
            val fr = LoginFragment()
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Login"

        loginWebView.settings.javaScriptEnabled = true
        loginWebView.webViewClient = object: WebViewClient() {

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (request?.url?.path?.contains("ajax/bz") == true &&
                        request.requestHeaders["X-IG-WWW-Claim"]?.equals("0") == false) {
                    if (context != null) {
                        val man = InstaCookieManager(Settings.getHeaderStorage(context!!))

                        man.importHeaders(request.requestHeaders)

                        val cookies = CookieManager.getInstance().getCookie(request.url.toString())
                        if (cookies != null) {
                            man.importCookies(cookies)

                            activity?.runOnUiThread {
                                navigation.testBot().show(false, null)
                            }
                        } else {
                            // TODO failed to obtain cookies
                        }
                    }
                }

                return null
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        loginWebView.loadUrl("https://instagram.com/accounts/login")
    }
}