package hr.ina.instabot.core

import android.util.Log
import okhttp3.Response
import org.json.JSONObject
import java.lang.Exception

class MediaInstaResponse(response: Response) : HtmlInstaResponse(response) {

    companion object {
        const val TAG = "MediaInstaRespone"
    }

    val shortcodeMedia : JSONObject? = try {
        html?.graphql?.getJSONObject("shortcode_media")
    } catch (e : Exception) {
        Log.w(TAG, "Error evaulating shortcode", e)
        null
    }

    val userName : String? = try {
        shortcodeMedia?.getJSONObject("owner")?.getString("username")
    } catch (e: Exception) {
        Log.w(TAG, "Error evaulating userName", e)
        null
    }

    init {

    }
}