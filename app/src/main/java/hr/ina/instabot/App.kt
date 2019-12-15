package hr.ina.instabot

import android.app.Application
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.network.HttpClient

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppDatabase.initDatabase(applicationContext)
        HttpClient.initClient()
    }

}