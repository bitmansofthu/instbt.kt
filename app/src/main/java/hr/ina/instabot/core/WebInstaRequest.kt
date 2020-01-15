package hr.ina.instabot.core

import hr.ina.instabot.network.HttpClient
import hr.ina.instabot.network.InstaCookieManager

class WebInstaRequest(private val cookieManager : InstaCookieManager) : InstaRequest {

    companion object {
    }

    override fun like(mediaid: String) : InstaResponse {
        val resp = HttpClient.post("https://www.instagram.com/web/likes/$mediaid/like/").execute()
        return JsonInstaResponse(resp)
    }

    override fun follow(userid: String) : InstaResponse {
        val resp = HttpClient.post("https://www.instagram.com/web/friendships/$userid/follow/").execute()
        return JsonInstaResponse(resp)
    }

    override fun unlike(mediaid: String) : InstaResponse {
        val resp = HttpClient.post("https://www.instagram.com/web/likes/$mediaid/unlike/").execute()
        return JsonInstaResponse(resp)
    }

    override fun unfollow(userid: String) : InstaResponse {
        val resp = HttpClient.post("https://www.instagram.com/web/friendships/$userid/unfollow/").execute()
        return JsonInstaResponse(resp)
    }

    override fun explore(hashtag: String) : ExploreInstaResponse {
        val resp = HttpClient.get("https://www.instagram.com/explore/tags/$hashtag/").execute()
        return ExploreInstaResponse(resp)
    }

    override fun getMediaInfo(mediaShortCode: String) : MediaInstaResponse {
        val resp = HttpClient.get("https://www.instagram.com/p/$mediaShortCode/").execute()
        return MediaInstaResponse(resp)
    }

    override fun getUserInfo(userName: String) : UserInfoInstaResponse {
        val resp = HttpClient.get("https://www.instagram.com/$userName/").execute()
        return UserInfoInstaResponse(resp)
    }
}