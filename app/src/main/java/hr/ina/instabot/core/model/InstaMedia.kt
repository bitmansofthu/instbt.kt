package hr.ina.instabot.core.model

import java.util.*

data class InstaMedia(
    val mediaId: String?,
    val shortCode: String?,
    val likeCount: Int?,
    val takenAt: Date?,
    val ownerId: String?
)