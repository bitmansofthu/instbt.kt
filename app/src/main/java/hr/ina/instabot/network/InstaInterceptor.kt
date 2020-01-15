package hr.ina.instabot.network

import android.content.Context
import okhttp3.Cookie
import okhttp3.Interceptor
import okhttp3.Response

class InstaInterceptor(val context: Context, val cookieManager: InstaCookieManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val builder = request.newBuilder()

        for ((k, v) in cookieManager.headers) {
            builder.header(k, v)
        }

        val response = chain.proceed(builder.build())

        val cookies = response.headers("set-cookie")
        val cookieMap = cookieManager.cookies()

        if (cookieMap != null) {
            cookies.forEach {
                val parsed = Cookie.parse(request.url, it)

                if (parsed != null) {
                    cookieMap.set(parsed)
                }
            }

            cookieManager.updateCookies(cookieMap)
        }

        return response
    }

}