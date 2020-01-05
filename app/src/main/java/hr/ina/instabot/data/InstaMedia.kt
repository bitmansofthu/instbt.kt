package hr.ina.instabot.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "instamedias")
data class InstaMedia(
    @PrimaryKey @ColumnInfo(name = "media_id") var mediaId: String,
    @ColumnInfo(name = "like_date") val likedDate: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "shortcode") val shortCode: String?
    )