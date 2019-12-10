package hr.ina.instabot.network

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

object HttpClient {
    private lateinit var client : OkHttpClient

    fun initClient() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        this.client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun get(url: String, manager: InstaCookieManager, callback: Callback) {
        client.newCall(buildRequest(url, manager)).enqueue(callback)
    }

    private fun buildRequest(url: String, manager: InstaCookieManager) : Request {
        val builder = Request.Builder()
            .url(url)

        for ((k, v) in manager.headers) {
            builder.header(k, v)
        }

        return builder.build()
    }

}