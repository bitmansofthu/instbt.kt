package hr.ina.instabot.data

import androidx.room.*

@Dao
interface InstaMediaDao {

    @Query("SELECT * FROM instamedias")
    fun getAll(): List<InstaMedia>

    @Query("SELECT * FROM instamedias WHERE media_id LIKE :mediaId LIMIT 1")
    fun findMediaById(mediaId: String): InstaMedia?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMedia(media: InstaMedia): Long

    @Delete
    fun deleteMedia(media: InstaMedia)

}