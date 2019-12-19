package hr.ina.instabot

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import hr.ina.instabot.core.WebInstaRequest
import hr.ina.instabot.network.InstaCookieManager
import hr.ina.instabot.util.Settings

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("hr.ina.instabot", appContext.packageName)
    }

    @Test
    fun testHashTagExplore() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hdrs = Settings.getHeaderStorage(appContext)
        val cookie = InstaCookieManager(hdrs)

        val loggedIn = cookie.isLoggedIn

        assertTrue(loggedIn)

        val req = WebInstaRequest(cookie)
        val resp = req.explore("eskuvo")

        assertTrue(resp.statusCode == 200)

        val len = resp.medias.size
        assertTrue(len > 0)
    }

    @Test
    fun testHashTagExploreAndLike() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hdrs = Settings.getHeaderStorage(appContext)
        val cookie = InstaCookieManager(hdrs)

        val loggedIn = cookie.isLoggedIn

        assertTrue(loggedIn)

        val req = WebInstaRequest(cookie)
        val exploreResp = req.explore("eskuvo")

        assertTrue(exploreResp.statusCode == 200)

        val len = exploreResp.medias?.size
        assertTrue(len > 0)

        val media = exploreResp.medias[0]
        assertNotNull("mediaId cant be null", media.mediaId)

        val likeResp = req.like(media!!.mediaId!!)
        assertTrue(likeResp.statusCode == 200)
    }
}
