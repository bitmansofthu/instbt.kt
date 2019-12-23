package hr.ina.instabot.util

import android.util.Log
import org.json.JSONObject
import java.lang.Exception

class InstaHtmlParser(val html: String) {

    companion object {
        const val TAG = "InstaHtmlParser"
    }

    val sharedData: JSONObject? = try {
        JSONObject(html.split("window._sharedData = ")[1].split(";</script>")[0])
    } catch (e: Exception) {
        Log.d(TAG, "Failed to evaulate shared data", e)
        null
    }

    val graphql: JSONObject? = try {
        JSONObject("{\"graphql\"" + html.split("{\"graphql\"")[1].split(");</script>")[0])?.getJSONObject("graphql")
    } catch (e: Exception) {
        Log.d(TAG, "Failed to parse graphql", e)
        null
    }

    init {

    }

}