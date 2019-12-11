package hr.ina.instabot.core

import okhttp3.Response

class ExploreInstaResponse(response: Response) : InstaResponse(response) {

    init {
        println(response.body?.string())
    }

}