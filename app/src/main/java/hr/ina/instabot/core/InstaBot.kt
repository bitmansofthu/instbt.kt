package hr.ina.instabot.core

import hr.ina.instabot.core.model.InstaMedia
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.data.InstaUser
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.min
import kotlin.random.Random

class InstaBot(val request: InstaRequest, val database: AppDatabase) {

    private lateinit var medias: List<InstaMedia>

    var selector: (List<InstaMedia>) -> InstaMedia = {
        it[Random.nextInt(0, min(it.size, 10))]
    }

    init {
    }

    fun loadMedias(ins: InputStream) {

    }

    fun saveMedias(outs: OutputStream) {

    }

    fun explore(hashtag: String) : InstaResponse {
        val resp = request.explore(hashtag)

        if (resp.isSuccessful && medias.size > 0) {
            medias = resp.medias
        } else {
            throw InstaException("Explore has failed", InstaException.Type.REQUEST, resp)
        }

        return resp
    }

    fun like() : InstaResponse? {
        val media = selector(medias)

        if (media.mediaId != null) {
            if (database.instaMediaDao().findMediaById(media.mediaId) == null) {
                val resp = request.like(media.mediaId!!)

                if (resp.isSuccessful) {
                    database.instaMediaDao().insertMedia(hr.ina.instabot.data.InstaMedia(mediaId = media.mediaId, likeResponseCode = resp.statusCode))
                }

                return resp
            } else {

            }
        }

        return null
    }

    fun follow() : InstaResponse? {
        val media = selector(medias)

        if (media.ownerId != null && media.shortCode != null) {

            if (database.instaUserDao().findByUserId(media.ownerId) != null) {
                throw InstaException("User already followed", InstaException.Type.ENTRY_EXISTS)
            }

            val resp = request.getMediaInfo(media.shortCode!!)

            if (resp.isSuccessful && resp.userName != null) {
                val userName = resp.userName
                val resp = request.getUserInfo(userName)

                if (resp.followerCount == null) {
                    throw InstaException("Follower Count not available", InstaException.Type.MISSING_VALUE)
                } else if (resp.followsCount == null) {
                    throw InstaException(
                        "Follows Count not available",
                        InstaException.Type.MISSING_VALUE
                    )
                } else if (resp.followsUser == null) {
                    throw InstaException(
                        "Follows User not available",
                        InstaException.Type.MISSING_VALUE
                    )
                }

                if (resp.isSuccessful && checkUserInfoForFollow(resp.followerCount!!, resp.followsCount!!, resp.followsUser!!)) {
                    val folresp = request.follow(media.ownerId)

                    if (folresp.isSuccessful) {
                        database.instaUserDao().insertUser(InstaUser(userId = media.ownerId, name = userName))
                    }

                    return folresp
                }

            }
        }

        return null
    }

    fun unfollow() : InstaResponse? {
        val user = database.instaUserDao().getRandomuser()

        val resp = request.getUserInfo(user.name)

        val followsUser = resp.followsUser ?: false

        if (resp.isSuccessful && followsUser) {
            val resp = request.unfollow(user.userId!!)

            database.instaUserDao().deleteUser(user)

            return resp
        }

        return null
    }

    private fun checkUserInfoForFollow(followerCount: Int, followsCount: Int, followsUser: Boolean) : Boolean {
        if (!followsUser) {
            if (followsCount == 0 || followerCount >= 5000) {
                throw InstaException(
                    "This is probably Selebgram account",
                    InstaException.Type.FAKE_USER
                )
                return false
            } else if (followerCount == 0 || followsCount / followerCount > 2) {
                throw InstaException(
                    "This is probably Fake account",
                    InstaException.Type.FAKE_USER
                )
                return false
            }
        }

        return true
    }

}