package hr.ina.instabot.util

import okhttp3.Cookie

class CookieMap() {
    private val map = HashMap<String, String>()

    constructor(cookies : String) : this() {
        mapify(cookies)
    }

    init {
    }

    fun value(key: String) : String? {
        return map[key]
    }

    fun set(key: String, value: String) {
        map[key] = value
    }

    fun set(cookie: Cookie) {
        map[cookie.name] = cookie.value
    }

    fun mapify(cookiesString : String) {
        val cookies = cookiesString.split(";")

        for (cookie in cookies) {
            val key = cookie.split("=")[0].trim()
            val value = cookie.split("=")[1].trim()

            map[key] = value
        }
    }

    fun stringify() : String {
        val cookies = StringBuilder()
        map.keys.iterator().forEach { item -> cookies.append(item).append('=').append(map[item]).append("; ")}

        if (cookies.isNotEmpty()) {
            cookies.delete(cookies.length - 3, cookies.length - 1)
        }

        return cookies.toString()
    }
}