package hr.ina.instabot.util

import android.util.Log
import org.json.JSONObject
import java.lang.Exception

class InstaHtmlParser(val html: String) {

    val sharedData: JSONObject? = try {
        JSONObject(html.split("window._sharedData = ")[1].split(";</script>")[0])
    } catch (e: Exception) {
        Log.d(InstaHtmlParser::class.simpleName, "Failed to get shared data", e)
        null
    }

    init {

    }

}