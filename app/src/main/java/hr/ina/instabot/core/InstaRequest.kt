package hr.ina.instabot.core

interface InstaRequest {

    fun like(mediaid: String) : InstaResponse

    fun unlike(mediaid: String) : InstaResponse

    fun follow(userid: String) : InstaResponse

    fun unfollow(userid: String) : InstaResponse

    fun explore(hashtag: String) : ExploreInstaResponse

    fun getMediaInfo(mediaShortCode: String) : MediaInstaResponse

    fun getUserInfo(userName: String) : UserInfoInstaResponse

}