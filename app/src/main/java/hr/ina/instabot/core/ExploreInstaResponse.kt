package hr.ina.instabot.core

import android.util.Log
import hr.ina.instabot.core.model.InstaMedia
import hr.ina.instabot.util.InstaHtmlParser
import okhttp3.Response
import org.json.JSONArray
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExploreInstaResponse(response: Response) : HtmlInstaResponse(response) {

    val medias: List<InstaMedia>
        get() {
            val edges: JSONArray? = try {
                html?.sharedData?.getJSONObject("entry_data")?.
                    getJSONArray("TagPage")?.
                    getJSONObject(0)!!.
                    getJSONObject("graphql")?.
                    getJSONObject("hashtag")?.
                    getJSONObject("edge_hashtag_to_media")?.
                    getJSONArray("edges")
            } catch (e: Exception) {
                Log.d(InstaHtmlParser::class.simpleName, "Failed to evaulate edges", e)
                null
            }

            val medias = ArrayList<InstaMedia>()
            if (edges != null) {
                for (i in 0 until edges.length()) {
                    val node = edges?.getJSONObject(i)!!.getJSONObject("node")
                    val media = InstaMedia(
                        node?.getString("id"),
                        node?.getString("shortcode"),
                        node?.getJSONObject("edge_liked_by")?.getInt("count"),
                        if (node?.getLong("taken_at_timestamp") != null) Date(node?.getLong("taken_at_timestamp")!!) else null,
                        node?.getJSONObject("owner")?.getString("id")
                    )
                    medias.add(media)
                }
            }

            return medias
        }

    init {
    }

}