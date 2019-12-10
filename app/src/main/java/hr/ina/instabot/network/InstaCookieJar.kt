package hr.ina.instabot.network

import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class InstaCookieJar(prefs: SharedPreferences) : CookieJar {

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}