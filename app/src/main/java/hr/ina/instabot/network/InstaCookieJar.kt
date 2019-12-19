package hr.ina.instabot.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class InstaCookieJar() : CookieJar {

    private val cookies: ArrayList<Cookie> = ArrayList()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies.clear()
        this.cookies.addAll(cookies)
    }
}