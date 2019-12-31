package hr.ina.instabot.core

import okhttp3.Response

open class InstaResponse(response: Response) {

    val statusCode = response.code

    val isSuccessful = statusCode == 200
    val isUnathorized = statusCode == 400

    init {
        //Log.d(InstaResponse::class.simpleName,response.body?.string() ?: "No response body")
    }

}