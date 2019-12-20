package hr.ina.instabot.core

import okhttp3.Response
import org.json.JSONObject

class JsonInstaResponse(val response: Response) : InstaResponse(response) {

    val json: JSONObject = JSONObject(response.body?.string())

    init {

    }

}