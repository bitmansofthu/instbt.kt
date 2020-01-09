package hr.ina.instabot.core

import android.util.Log
import hr.ina.instabot.core.model.InstaMedia
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.data.InstaUser
import io.reactivex.Single
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min
import kotlin.random.Random

enum class InstaAction {
    LIKE, FOLLOW, UNFOLLOW
}

data class InstaBotActionResult (
    val action : InstaAction,
    val userName : String?,
    val media : InstaMedia?,
    val failureMessage: String?,
    val timestamp: Date = Date()
)

class InstaBotModel(hashtags: Array<String>,
                    val patternForHashtag: Array<InstaAction>,
                    private val instabot : InstaBot,
                    private val database: AppDatabase) {

    companion object {
        const val MIN_USER_FOR_UNFOLLOW = 10

        const val TAG = "InstaBotModel"
    }

    private var actionsForHashtag = 0
    private var hashtagCount = 0
    private var shuffledHashtags = hashtags.toList().shuffled()
    private var shuffledActions = patternForHashtag.toList().shuffled()

    private lateinit var medias: ArrayList<InstaMedia>

    val currentHashtag get() = shuffledHashtags[hashtagCount]

    init {
    }

    private fun getRandomMedia() : InstaMedia? {
        try {
            if (medias.size > 0) {
                return medias.removeAt(Random.nextInt(0, medias.size))
            }
        } catch (e : Exception) {
            Log.d(TAG, "Error with getRandomMedia", e)
        }

        return null
    }

    fun getNextAction() : InstaAction {
        if (actionsForHashtag == shuffledActions.size) {
            actionsForHashtag = 0
            shuffledActions = patternForHashtag.toList().shuffled()

            Log.d(TAG, "Resetting actions order")
        }

        return shuffledActions[actionsForHashtag++]
    }

    fun obtainMedia(max : Int = 10) : Single<InstaMedia> {
        return Single.create {
            var media = getRandomMedia()

            if (media != null) {
                it.onSuccess(media)
            } else {
                try {
                    Log.d(TAG, "Getting medias for hashtag: " + shuffledHashtags[hashtagCount])

                    val resp = instabot.explore(shuffledHashtags[hashtagCount++])
                    medias = ArrayList(resp.medias.subList(0, min(max, resp.medias.size)))
                    if (hashtagCount == shuffledHashtags.size) {
                        hashtagCount = 0
                    }

                    it.onSuccess(getRandomMedia()!!)
                } catch (e : Exception) {
                    it.onError(e)
                }

            }
        }
    }

    fun likeMedia(media : InstaMedia) : Single<InstaBotActionResult> {
        return Single.create {
            if (media.mediaId != null && database.instaMediaDao().findMediaById(media.mediaId) == null) {
                try {
                    Log.d(TAG, "Trying to like media " + media.shortCode)

                    val resp = instabot.like(media)

                    database.instaMediaDao().insertMedia(
                        hr.ina.instabot.data.InstaMedia(
                            mediaId = media.mediaId!!,
                            shortCode = media.shortCode!!
                        )
                    )

                    it.onSuccess(
                        InstaBotActionResult(
                            InstaAction.LIKE,
                            null,
                            media,
                            null
                        )
                    )
                } catch (e: Exception) {
                    it.onError(e)
                }
            } else {
                it.onSuccess(InstaBotActionResult(
                    InstaAction.LIKE,
                    null,
                    media,
                    "User already liked"
                ))
            }
        }
    }

    fun followUserByMedia(media: InstaMedia) : Single<InstaBotActionResult> {
        return Single.create {
            var userName = "N/A"
            if (media.ownerId != null && database.instaUserDao().findByUserId(media.ownerId) == null) {
                try {
                    val userresp = instabot.getUserFromMedia(media)
                    userName = userresp.userName!!

                    val followresp = instabot.follow(media.ownerId, userresp)

                    database.instaUserDao().insertUser(
                        InstaUser(
                            userId = media.ownerId,
                            name = userName
                        )
                    )

                    it.onSuccess(
                        InstaBotActionResult(
                            InstaAction.FOLLOW,
                            userresp.userName,
                            media,
                            null
                        )
                    )
                } catch (e : Exception) {
                    if (e is InstaException) {
                        if (e.type == InstaException.Type.FAKE_USER) {
                            it.onSuccess(
                                InstaBotActionResult(
                                    InstaAction.FOLLOW,
                                    null,
                                    null,
                                    "Fake user"
                                )
                            )
                        } else {
                            it.onError(e)
                        }
                    } else {
                        it.onError(e)
                    }
                }
            } else {
                it.onSuccess(InstaBotActionResult(
                    InstaAction.UNFOLLOW,
                    userName,
                    null,
                    "User already followed or ownerId not available"
                ))
            }
        }
    }

    fun unfollowRandomUser() : Single<InstaBotActionResult> {
        val user = database.instaUserDao().getRandomuser()

        return if (user != null) {
            unfollowUser(user)
        } else {
            Single.create {
                it.onSuccess(InstaBotActionResult(
                    InstaAction.UNFOLLOW,
                    user?.name,
                    null,
                    "User database is empty"
                ))
            }
        }
    }

    fun unfollowUser(user : InstaUser) : Single<InstaBotActionResult> {
        return Single.create {
            if (database.instaUserDao().getNumberOfUsers() >= MIN_USER_FOR_UNFOLLOW) {
                try {
                    val resp = instabot.unfollow(user)

                    database.instaUserDao().deleteUser(user)

                    it.onSuccess(
                        InstaBotActionResult(
                            InstaAction.UNFOLLOW,
                            user.name,
                            null,
                            null
                        )
                    )
                } catch (e : Exception) {
                    it.onError(e)
                }
            } else {
                it.onSuccess(InstaBotActionResult(
                    InstaAction.UNFOLLOW,
                    user.name,
                    null,
                    "Not enough user to unfollow"
                ))
            }
        }
    }

    @Deprecated("Use getNextAction()")
    private fun processMedia(media : InstaMedia) : Single<InstaBotActionResult> {
        return Single.create {
            try {
                when (shuffledActions[actionsForHashtag]) {
                    InstaAction.LIKE -> {
                        val resp = instabot.like(media)

                        database.instaMediaDao().insertMedia(hr.ina.instabot.data.InstaMedia(mediaId = media.mediaId!!, shortCode = media.shortCode!!))

                        actionsForHashtag++
                        it.onSuccess(
                            InstaBotActionResult(
                                InstaAction.LIKE,
                                null,
                                media,
                                null
                            )
                        )
                    }
                    InstaAction.FOLLOW -> {
                        if (media.ownerId != null && database.instaUserDao().findByUserId(media.ownerId) == null) {
                            val userresp = instabot.getUserFromMedia(media)

                            val followresp = instabot.follow(media.ownerId, userresp)

                            database.instaUserDao().insertUser(
                                InstaUser(
                                    userId = media.ownerId,
                                    name = userresp.userName!!
                                )
                            )

                            actionsForHashtag++
                            it.onSuccess(
                                InstaBotActionResult(
                                    InstaAction.LIKE,
                                    userresp.userName,
                                    media,
                                    null
                                )
                            )
                        } else {
                            throw IllegalStateException("User already followed or ownerId not available")
                        }
                    }
                    InstaAction.UNFOLLOW -> {
                        if (database.instaUserDao().getNumberOfUsers() >= MIN_USER_FOR_UNFOLLOW) {
                            val user = database.instaUserDao().getRandomuser()

                            val resp = instabot.unfollow(user!!)

                            database.instaUserDao().deleteUser(user)

                            actionsForHashtag++
                            it.onSuccess(
                                InstaBotActionResult(
                                    InstaAction.LIKE,
                                    user.name,
                                    null,
                                    null
                                )
                            )
                        } else {
                            throw IllegalStateException("Not enough user for unfollow")
                        }
                    }
                }
            } catch (e : Exception) {
                it.onError(e)
            }
        }
    }

}