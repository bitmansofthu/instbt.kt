package hr.ina.instabot.util

import android.content.Context
import android.content.SharedPreferences

class Settings {

    companion object {
        fun getHeaderStorage(ctx : Context) : SharedPreferences {
            return ctx.getSharedPreferences("headers", Context.MODE_PRIVATE)
        }

        fun clearHeaderStorage(ctx : Context) {
            ctx.getSharedPreferences("headers", Context.MODE_PRIVATE).edit().clear().commit()
        }
    }

}