package hr.ina.instabot.data

import androidx.room.*

@Dao
interface InstaUserDao {

    @Query("SELECT * FROM instausers")
    fun getAll(): List<InstaUser>

    @Query("SELECT * FROM instausers WHERE user_id = :userId LIMIT 1")
    fun findByUserId(userId: String): InstaUser

    @Query("SELECT * FROM instausers ORDER BY RANDOM() LIMIT 1")
    fun getRandomuser(): InstaUser

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUser(user: InstaUser): Long

    @Delete
    fun deleteUser(user: InstaUser)
}