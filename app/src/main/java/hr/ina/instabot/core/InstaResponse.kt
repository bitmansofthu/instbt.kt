package hr.ina.instabot.core

import okhttp3.Response

open class InstaResponse(response: Response) {

    val statusCode = response.code

    init {
        //Log.d(InstaResponse::class.simpleName,response.body?.string() ?: "No response body")
    }

}