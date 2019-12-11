package hr.ina.instabot.network

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.RequestBody.Companion.toRequestBody

object HttpClient {
    private lateinit var client : OkHttpClient

    fun initClient() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        this.client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun get(url: String, manager: InstaCookieManager) : Call {
        return client.newCall(buildRequest(url, manager).get().build())
    }

    fun post(url: String, manager: InstaCookieManager, body: String? = null) : Call {
        return client.newCall(buildRequest(url, manager).post((body ?: "").toRequestBody()).build())
    }

    private fun buildRequest(url: String, manager: InstaCookieManager) : Request.Builder {
        val builder = Request.Builder()
            .url(url)

        for ((k, v) in manager.headers) {
            builder.header(k, v)
        }

        return builder
    }

}