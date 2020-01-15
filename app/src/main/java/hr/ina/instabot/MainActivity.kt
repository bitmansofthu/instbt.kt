package hr.ina.instabot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.ina.instabot.fragment.Navigation
import hr.ina.instabot.network.InstaCookieManager
import hr.ina.instabot.util.Settings

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
        val cookie = (application as App).cookieManager

        if (cookie.isLoggedIn)
            Navigation(this).testBot().show(false, null)
        else
            Navigation(this).login().show(false, null)
    }
}
