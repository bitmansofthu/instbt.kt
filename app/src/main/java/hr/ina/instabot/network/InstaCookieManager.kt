package hr.ina.instabot.network

import android.content.SharedPreferences
import android.util.Log
import hr.ina.instabot.util.CookieMap

class InstaCookieManager(private val prefs: SharedPreferences) {

    companion object {
        const val TAG = "InstaCookieManager"
    }

    val headers = HashMap<String, String>()
    val isLoggedIn: Boolean
        get() = headers["Cookie"] != null

    init {
        for ((k, v) in prefs.all) {
            headers[k] = v as String
        }
    }

    fun importCookies(cookies: String) {
        updateHeader("Cookie", cookies)

        Log.d(TAG, "Cookie $cookies")
    }

    fun importHeaders(headers: Map<String, String>) {
        val editor = prefs.edit()
        for ((k, v) in headers) {
            editor.putString(k, v)
            this.headers[k] = v

            Log.d(TAG, "Header $k: $v")
        }
        editor.commit()
    }

    fun updateHeader(key: String, value: String) {
        headers[key] = value
        prefs.edit().putString(key, value).commit()
    }

    fun cookies() : CookieMap? {
        val cookies = headers["Cookie"]
        if (cookies != null) {
            return CookieMap(cookies)
        }

        return null
    }

    fun updateCookies(map : CookieMap) {
        updateHeader("Cookie", map.stringify())
    }

}