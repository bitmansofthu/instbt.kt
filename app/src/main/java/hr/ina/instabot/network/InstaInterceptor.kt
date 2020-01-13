package hr.ina.instabot.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class InstaInterceptor(val context: Context, val cookieManager: InstaCookieManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}