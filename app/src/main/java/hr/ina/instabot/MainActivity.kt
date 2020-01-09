package hr.ina.instabot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import hr.ina.instabot.core.InstaResponse
import hr.ina.instabot.core.InstaResponseCallback
import hr.ina.instabot.core.WebInstaRequest
import hr.ina.instabot.fragment.LoginFragment
import hr.ina.instabot.fragment.Navigation
import hr.ina.instabot.network.HttpClient
import hr.ina.instabot.network.InstaCookieManager
import hr.ina.instabot.util.Settings
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val TAG = InstaCookieManager::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //WebInstaRequest(InstaCookieManager(Settings.getHeaderStorage(this))).explore("eskuvo", object : InstaResponseCallback {
        //    override fun onResponse(response: InstaResponse?, failure: IOException?) {

        //    }
        //})

        val hdrs = Settings.getHeaderStorage(this)
        val cookie = InstaCookieManager(hdrs)

        if (cookie.isLoggedIn)
            Navigation(this).testBot().show(false, null)
        else
            Navigation(this).login().show(false, null)
    }
}
