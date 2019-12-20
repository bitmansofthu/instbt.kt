package hr.ina.instabot.core.model

import java.util.*

data class InstaMedia(
    val mediaId: String?,
    val likeCount: Int? = 0,
    val takenAt: Date?,
    val ownerId: String?
)