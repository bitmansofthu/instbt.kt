package hr.ina.instabot.util

import hr.ina.instabot.core.InstaBot
import hr.ina.instabot.core.InstaResponse
import hr.ina.instabot.core.model.InstaMedia
import hr.ina.instabot.data.AppDatabase
import io.reactivex.Single
import java.lang.Exception

enum class ACTION {
    LIKE, FOLLOW, UNFOLLOW
}

class InstaBotRx(val patternForHashtag: Array<ACTION>,
                 private val instabot : InstaBot,
                 private val database: AppDatabase) {

    companion object {

    }

    private var actionsForHashtag = 0
    private lateinit var shuffledActions : ArrayList<ACTION>

    init {

    }

    fun getRandomMedia(hashtag: String) : Single<InstaMedia> {
        return Single.create {
            var media = instabot.getRandomMedia(20)

            if (media != null && actionsForHashtag < patternForHashtag.size) {
                it.onSuccess(media)
            } else {
                try {
                    val resp = instabot.explore(hashtag)

                    shuffledActions = ArrayList(patternForHashtag.toList())
                    shuffledActions.shuffle()
                    actionsForHashtag = 0

                    media = instabot.getRandomMedia(10)
                    it.onSuccess(media!!)
                } catch (e : Exception) {
                    it.onError(e)
                }

            }
        }
    }

    fun processMedia(media : InstaMedia) : Single<InstaResponse> {
        return Single.create {
            try {
                when (shuffledActions[actionsForHashtag++]) {
                    ACTION.LIKE -> {
                        it.onSuccess(instabot.like(media!!))
                    }
                    ACTION.FOLLOW -> {

                    }
                    ACTION.UNFOLLOW -> {

                    }
                }
            } catch (e : Exception) {
                it.onError(e)
            }
        }
    }

}