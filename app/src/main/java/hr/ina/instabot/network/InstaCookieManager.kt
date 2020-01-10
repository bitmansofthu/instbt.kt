package hr.ina.instabot.network

import android.content.SharedPreferences
import android.util.Log

class CookieMap(cookiesString: String) {
    private val map = HashMap<String, String>()

    init {
        mapify(cookiesString)
    }

    fun value(key: String) : String? {
        return map[key]
    }

    fun mapify(cookiesString : String) {
        val cookies = cookiesString.split(";")

        for (cookie in cookies) {
            val key = cookie.split(":")[0].trim()
            val value = cookie.split(":")[1].trim()

            map[key] = value
        }
    }

    fun stringify() : String? {
        return null
    }
}

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
        prefs.edit().putString("Cookie", cookies).commit()
        headers["Cookie"] = cookies

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
        prefs.edit().putString(key, value)
    }

}