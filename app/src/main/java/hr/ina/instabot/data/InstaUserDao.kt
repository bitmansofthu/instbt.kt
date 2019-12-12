package hr.ina.instabot.data

import androidx.room.*

@Dao
interface InstaUserDao {

    @Query("SELECT * FROM instausers")
    fun getAll(): List<InstaUser>

    @Query("SELECT * FROM instausers WHERE user_id = :userId")
    fun findByUserId(userId: String): List<InstaUser>

    @Query("SELECT * FROM instausers ORDER BY RANDOM() LIMIT 1")
    fun getRandomuser(): List<InstaUser>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: InstaUser): Long

    @Delete
    suspend fun deleteUser(user: InstaUser)
}