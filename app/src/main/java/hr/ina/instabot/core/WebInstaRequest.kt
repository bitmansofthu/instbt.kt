package hr.ina.instabot.core

import hr.ina.instabot.network.HttpClient
import hr.ina.instabot.network.InstaCookieManager

class WebInstaRequest(private val cookieManager : InstaCookieManager) : InstaRequest {

    companion object {
    }

    override fun like(mediaid: String) : InstaResponse {
        val resp = HttpClient.post("https://www.instagram.com/web/likes/$mediaid/like/", cookieManager).execute()
        return JsonInstaResponse(resp)
    }

    override fun follow(userid: String) : InstaResponse {
        val resp = HttpClient.post("https://www.instagram.com/web/friendships/$userid/follow/", cookieManager).execute()
        return JsonInstaResponse(resp)
    }

    override fun unlike(mediaid: String) : InstaResponse {
        val resp = HttpClient.post("https://www.instagram.com/web/likes/$mediaid/unlike/", cookieManager).execute()
        return JsonInstaResponse(resp)
    }

    override fun unfollow(userid: String) : InstaResponse {
        val resp = HttpClient.post("https://www.instagram.com/web/friendships/$userid/unfollow/", cookieManager).execute()
        return JsonInstaResponse(resp)
    }

    override fun explore(hashtag: String) : ExploreInstaResponse {
        val resp = HttpClient.get("https://www.instagram.com/explore/tags/$hashtag/", cookieManager).execute()
        return ExploreInstaResponse(resp)
    }

    override fun getMediaInfo(mediaShortCode: String) : MediaInstaResponse {
        val resp = HttpClient.get("https://www.instagram.com/p/$mediaShortCode/", cookieManager).execute()
        return MediaInstaResponse(resp)
    }

    override fun getUserInfo(userName: String) : UserInfoInstaResponse {
        val resp = HttpClient.get("https://www.instagram.com/$userName/", cookieManager).execute()
        return UserInfoInstaResponse(resp)
    }
}