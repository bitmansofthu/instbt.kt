package hr.ina.instabot.core

import hr.ina.instabot.core.model.InstaMedia
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.data.InstaUser
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.min
import kotlin.random.Random

class InstaBot(val request: InstaRequest) {

    init {
    }

    fun explore(hashtag: String) : ExploreInstaResponse {
        val resp = request.explore(hashtag)

        if (!resp.isSuccessful || resp.medias.isEmpty()) {
            throw InstaException("Explore has failed", InstaException.Type.REQUEST, resp)
        }

        return resp
    }

    fun like(media: InstaMedia) : InstaResponse {
        if (media.mediaId != null) {
            val resp = request.like(media.mediaId)

            if (!resp.isSuccessful) {
                throw InstaException("Like failed", InstaException.Type.REQUEST, resp)
            }

            return resp
        } else {
            throw InstaException("MediaId not available", InstaException.Type.MISSING_VALUE)
        }
    }

    fun getUserFromMedia(media: InstaMedia) : UserInfoInstaResponse {
        if (media.ownerId != null && media.shortCode != null) {
            val miresp = request.getMediaInfo(media.shortCode)

            if (miresp.isSuccessful && miresp.userName != null) {
                val uiresp = request.getUserInfo(miresp.userName)

                if (!uiresp.isSuccessful || uiresp.userRoot == null) {
                    throw InstaException("getUserInfo has failed", InstaException.Type.REQUEST, uiresp)
                }

                return uiresp
            } else {
                throw InstaException("getMediaInfo has failed", InstaException.Type.REQUEST, miresp)
            }
        } else {
            throw InstaException("Media ownerId or shortCode is missing", InstaException.Type.MISSING_VALUE)
        }
    }

    fun follow(userid: String, uiresp: UserInfoInstaResponse) : InstaResponse {
        if (uiresp.followerCount == null) {
            throw InstaException("Follower Count not available", InstaException.Type.MISSING_VALUE)
        } else if (uiresp.followsCount == null) {
            throw InstaException(
                "Follows Count not available",
                InstaException.Type.MISSING_VALUE
            )
        } else if (uiresp.followsUser == null) {
            throw InstaException(
                "Follows User not available",
                InstaException.Type.MISSING_VALUE
            )
        }

        if (checkUserInfoForFollow(uiresp.followerCount, uiresp.followsCount, uiresp.followsUser)) {
            val folresp = request.follow(userid)

            if (folresp.isSuccessful) {
                return folresp
            } else {
                throw InstaException("Follow request failed", InstaException.Type.REQUEST, folresp)
            }
        } else {
            throw InstaException("Won't follow user", InstaException.Type.FAKE_USER, uiresp)
        }
    }

    fun unfollow(user: InstaUser) : InstaResponse {
        val resp = request.getUserInfo(user.name)

        val followsUser = resp.followsUser ?: false

        if (resp.isSuccessful && !followsUser) {
            try {
                Thread.sleep(3000)
            } catch (e : InterruptedException) {

            }

            return request.unfollow(user.userId)
        } else {
            throw InstaException("Unfollow request has failed", InstaException.Type.REQUEST, resp)
        }
    }

    private fun checkUserInfoForFollow(followerCount: Int, followsCount: Int, followsUser: Boolean) : Boolean {
        if (!followsUser) {
            if (followsCount == 0 || followerCount >= 5000) {
                throw InstaException(
                    "This is probably Selebgram account",
                    InstaException.Type.FAKE_USER
                )
            } else if (followerCount == 0 || followsCount / followerCount > 2) {
                throw InstaException(
                    "This is probably Fake account",
                    InstaException.Type.FAKE_USER
                )
            }
        }

        return true
    }

}