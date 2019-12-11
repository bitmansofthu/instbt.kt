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

    override fun like(mediaid: String) : InstaResponse {
        val resp = HttpClient.post(LIKE_URL, cookieManager).execute()
        return InstaResponse(resp)
    }

    override fun follow(userid: String) : InstaResponse {
        val resp = HttpClient.post(FOLLOW_URL, cookieManager).execute()
        return InstaResponse(resp)
    }

    override fun explore(hashtag: String) : ExploreInstaResponse {
        val resp = HttpClient.get("$EXPLORE_URL$hashtag/", cookieManager).execute()
        return ExploreInstaResponse(resp)
    }

}