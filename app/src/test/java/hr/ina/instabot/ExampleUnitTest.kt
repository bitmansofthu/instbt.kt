package hr.ina.instabot

import hr.ina.instabot.util.CookieMap
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCookieMap() {
        val cookies = "ig_cb=1; ig_did=D3E83F5A-F636-4FF3-8F2D-936968F5EA41; mid=Xhg_fwABAAERBDan8Cz9W4qC-rwl; csrftoken=HQt1Rs4kcK9EU6iv3SoVWMoIAC3ltHy9; shbid=4841; shbts=1578647441.7494674; ds_user_id=2197969058; sessionid=2197969058%3Am1sQmQC2BfZAV5%3A16; rur=PRN; urlgen=\"{\\\"37.76.92.200\\\": 5483}:1ipqJX:b8EgtpVUEDAWWVlK1iS1V3eoJ4Q\""

        val map = CookieMap(cookies)

        assertEquals("HQt1Rs4kcK9EU6iv3SoVWMoIAC3ltHy9", map.value("csrftoken"))

        map.set("urlgen", "hello")

        val cookies_str = map.stringify()

        assertTrue(cookies_str.contains("urlgen=hello"))
    }
}
