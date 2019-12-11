package hr.ina.instabot.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "instausers")
data class InstaUser(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "follow_date") val followDate: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "user_name") val name: String
    )