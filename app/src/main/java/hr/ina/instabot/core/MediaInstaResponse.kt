package hr.ina.instabot.core

import okhttp3.Response
import org.json.JSONObject

class MediaInstaResponse(response: Response) : HtmlInstaResponse(response) {

    val shortcodeMedia : JSONObject? = html?.graphql?.getJSONObject("graphql")?.getJSONObject("shortcode_media")
    val userName : String? = shortcodeMedia?.getJSONObject("owner")?.getString("username")

    init {

    }
}