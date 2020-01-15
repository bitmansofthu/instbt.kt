package hr.ina.instabot

import android.app.Application
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.network.HttpClient
import hr.ina.instabot.network.InstaCookieManager
import hr.ina.instabot.util.Settings

class App : Application() {

    lateinit var cookieManager: InstaCookieManager

    override fun onCreate() {
        super.onCreate()

        cookieManager = InstaCookieManager(Settings.getHeaderStorage(applicationContext))

        AppDatabase.initDatabase(applicationContext, "instabot_db")
        HttpClient.initClient(applicationContext, cookieManager)
    }

}