package hr.ina.instabot.core

interface InstaRequest {

    fun like(mediaid: String) : InstaResponse

    fun follow(userid: String) : InstaResponse

    fun explore(hashtag: String) : ExploreInstaResponse

    fun getMediaInfo(mediaShortCode: String) : MediaInstaResponse

}