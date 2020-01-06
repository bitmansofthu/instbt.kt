package hr.ina.instabot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [InstaUser::class, InstaMedia::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun instaUserDao(): InstaUserDao
    abstract fun instaMediaDao(): InstaMediaDao

    companion object {
        @Volatile private var database: AppDatabase? = null

        fun initDatabase(ctx: Context, name: String) {
            database = buildDatabase(ctx, name)
        }

        fun getDatabase() : AppDatabase? {
            return database
        }

        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context, name: String): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, name)
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                    }
                })
                .build()
        }
    }
}