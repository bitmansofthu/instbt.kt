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

        //Navigation(this).showLogin()

        val login = InstaCookieManager(Settings.getHeaderStorage(this)).isLoggedIn
        Log.d(TAG, if (login) "Logged in" else "Not logged in")

        //supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment.newInstance()).commit()
    }
}
