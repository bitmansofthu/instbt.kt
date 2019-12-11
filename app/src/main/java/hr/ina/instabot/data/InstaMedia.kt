package hr.ina.instabot.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*

data class InstaMedia(
    @PrimaryKey @ColumnInfo(name = "media_id") var mediaId: String,
    @ColumnInfo(name = "like_date") val likedDate: Calendar = Calendar.getInstance()
    )