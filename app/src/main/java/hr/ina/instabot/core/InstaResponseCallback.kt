package hr.ina.instabot.core

import java.io.IOException

interface InstaResponseCallback {

    fun onResponse(response: InstaResponse?, failure: IOException?)

}