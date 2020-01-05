package hr.ina.instabot.core

import hr.ina.instabot.core.model.InstaMedia
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.data.InstaUser
import io.reactivex.Single
import java.lang.Exception
import java.lang.IllegalStateException
import kotlin.math.min
import kotlin.random.Random

enum class InstaAction {
    LIKE, FOLLOW, UNFOLLOW
}

data class InstaBotActionResult (
    val action : InstaAction,
    val userName : String?,
    val media : InstaMedia?
)

class InstaBotModel(hashtags: Array<String>,
                    val patternForHashtag: Array<InstaAction>,
                    private val instabot : InstaBot,
                    private val database: AppDatabase) {

    companion object {
        const val MIN_USER_FOR_UNFOLLOW = 10
    }

    private var actionsForHashtag = 0
    private var hashtagCount = 0
    private var shuffledHashtags = hashtags.toList().shuffled()
    private var shuffledActions = patternForHashtag.toList().shuffled()

    private lateinit var medias: ArrayList<InstaMedia>

    val currentHashtag get() = shuffledHashtags[hashtagCount]

    init {
    }

    private fun getRandomMedia(max : Int = 10) : InstaMedia {
        return medias.removeAt(Random.nextInt(0, min(medias.size, max)))
    }

    fun getNextAction() : InstaAction {
        if (actionsForHashtag == shuffledActions.size) {
            actionsForHashtag = 0
            shuffledActions = patternForHashtag.toList().shuffled()
        }

        return shuffledActions[actionsForHashtag++]
    }

    fun obtainMedia(max : Int = 10) : Single<InstaMedia> {
        return Single.create {
            var media : InstaMedia? = null

            try {
                media = getRandomMedia(max)
            } catch (e : Exception) {
                // not initialized
            }

            if (media != null && actionsForHashtag < patternForHashtag.size) {
                it.onSuccess(media)
            } else {
                try {
                    val resp = instabot.explore(shuffledHashtags[hashtagCount++])
                    if (hashtagCount == shuffledHashtags.size) {
                        hashtagCount = 0
                    }

                    it.onSuccess(getRandomMedia(max))
                } catch (e : Exception) {
                    it.onError(e)
                }

            }
        }
    }

    fun likeMedia(media : InstaMedia) : Single<InstaBotActionResult> {
        return Single.create {
            try {
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
                        media
                    )
                )
            } catch (e : Exception) {

            }
        }
    }

    fun followUserByMedia(media: InstaMedia) : Single<InstaBotActionResult> {
        return Single.create {
            if (media.ownerId != null && database.instaUserDao().findByUserId(media.ownerId) == null) {
                try {
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
                            media
                        )
                    )
                } catch (e : Exception) {
                    it.onError(e)
                }
            } else {
                it.onError(IllegalStateException("User already followed or ownerId not available"))
            }
        }
    }

    fun unfollowRandomUser() : Single<InstaBotActionResult> {
        val user = database.instaUserDao().getRandomuser()

        return if (user != null) {
            unfollowUser(user)
        } else {
            Single.create {
                it.onError(IllegalStateException("User database is empty"))
            }
        }
    }

    fun unfollowUser(user : InstaUser) : Single<InstaBotActionResult> {
        return Single.create {
            if (database.instaUserDao().getNumberOfUsers() >= MIN_USER_FOR_UNFOLLOW) {
                try {
                    val resp = instabot.unfollow(user)

                    database.instaUserDao().deleteUser(user)

                    actionsForHashtag++
                    it.onSuccess(
                        InstaBotActionResult(
                            InstaAction.LIKE,
                            user.name,
                            null
                        )
                    )
                } catch (e : Exception) {

                }
            } else {
                it.onError(IllegalStateException("Not enough user for unfollow"))
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
                                media
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
                                    media
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