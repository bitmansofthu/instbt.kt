package hr.ina.instabot.core

import okhttp3.Response

open class InstaResponse(response: Response) {

    var statusCode = response.code

    init {

    }

}