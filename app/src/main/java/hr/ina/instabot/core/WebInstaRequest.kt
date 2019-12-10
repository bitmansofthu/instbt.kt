package hr.ina.instabot.core

import hr.ina.instabot.network.HttpClient
import hr.ina.instabot.network.InstaCookieManager
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class WebInstaRequest(private val cookieManager : InstaCookieManager) : InstaRequest {

    companion object {
        const val LIKE_URL = "https://"
        const val FOLLOW_URL = "https://"
        const val EXPLORE_URL = "https://www.instagram.com/explore/tags/"
    }

    override fun like(mediaid: String, callback: InstaResponseCallback?) {
        HttpClient.get(LIKE_URL, cookieManager, object : Callback {
            override fun onResponse(call: okhttp3.Call, response: Response) {
                callback?.onResponse(InstaResponse(response), null)
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback?.onResponse(null, e)
            }
        })
    }

    override fun follow(userid: String, callback: InstaResponseCallback?) {
        HttpClient.get(FOLLOW_URL, cookieManager, object : Callback {
            override fun onResponse(call: okhttp3.Call, response: Response) {
                callback?.onResponse(InstaResponse(response), null)
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback?.onResponse(null, e)
            }
        })
    }

    override fun explore(hashtag: String, callback: InstaResponseCallback?) {
        HttpClient.get("$EXPLORE_URL$hashtag/", cookieManager, object : Callback {
            override fun onResponse(call: okhttp3.Call, response: Response) {
                //println(response.body?.string())
                callback?.onResponse(ExploreInstaResponse(response), null)
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback?.onResponse(null, e)
            }
        })
    }

}