package hr.ina.instabot.network

import android.content.Context
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.RequestBody.Companion.toRequestBody

object HttpClient {
    private lateinit var client : OkHttpClient

    fun initClient(context : Context, cookieManager: InstaCookieManager) {
        val logging = HttpLoggingInterceptor()
        val insta = InstaInterceptor(context, cookieManager)
        logging.level = HttpLoggingInterceptor.Level.BODY
        this.client = OkHttpClient.Builder()
            .addInterceptor(insta)
            .addInterceptor(logging)
            .build()
    }

    fun get(url: String) : Call {
        return client.newCall(buildRequest(url).get().build())
    }

    fun post(url: String, body: String? = null) : Call {
        return client.newCall(buildRequest(url).post((body ?: "").toRequestBody()).build())
    }

    private fun buildRequest(url: String) : Request.Builder {
        val builder = Request.Builder()
            .url(url)

        /*for ((k, v) in manager.headers) {
            builder.header(k, v)
        }*/

        return builder
    }

}