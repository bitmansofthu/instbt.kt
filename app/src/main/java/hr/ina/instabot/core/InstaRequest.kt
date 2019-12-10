package hr.ina.instabot.core

interface InstaRequest {

    fun like(mediaid: String, callback: InstaResponseCallback?)

    fun follow(userid: String, callback: InstaResponseCallback?)

    fun explore(hashtag: String, callback: InstaResponseCallback?)

}