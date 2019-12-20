package hr.ina.instabot.core

import hr.ina.instabot.util.InstaHtmlParser
import okhttp3.Response

open class HtmlInstaResponse(response: Response) : InstaResponse(response) {

    val html:InstaHtmlParser?

    init {
        if (response.body != null) {
            html = InstaHtmlParser(response.body!!.string())
        } else {
            html = null
        }
    }

}