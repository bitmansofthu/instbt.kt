package hr.ina.instabot

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import hr.ina.instabot.core.WebInstaRequest
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.data.InstaMedia
import hr.ina.instabot.data.InstaUser
import hr.ina.instabot.network.InstaCookieManager
import hr.ina.instabot.util.Settings

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        AppDatabase.initDatabase(appContext, "instabot_db")
    }

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

    @Test
    fun testHashTagExploreAndMediaInfo() {
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
        assertNotNull("mediaShortCode cant be null", media.shortCode)

        val mediaResp = req.getMediaInfo(media!!.shortCode!!)
        assertTrue(mediaResp.statusCode == 200)
        assertNotNull(mediaResp.userName)
    }

    @Test
    fun testHashTagExploreAndUserInfo() {
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
        assertNotNull("mediaShortCode cant be null", media.shortCode)

        val mediaResp = req.getMediaInfo(media!!.shortCode!!)
        assertTrue(mediaResp.statusCode == 200)
        assertNotNull(mediaResp.userName)

        val userResp = req.getUserInfo(mediaResp.userName!!)
        assertTrue(userResp.statusCode == 200)
        assertNotNull(userResp.userRoot)
        assertTrue(userResp.followsCount!! > 0)
    }

    @Test
    fun testMediaDb() {
        val mediaId = "test12345678"

        val media = InstaMedia(mediaId = mediaId, likeResponseCode = 200)
        AppDatabase.getDatabase()?.instaMediaDao()?.insertMedia(media)

        var read = AppDatabase.getDatabase()?.instaMediaDao()?.findMediaById(mediaId)

        assertEquals(mediaId, read?.mediaId)

        val readAll = AppDatabase.getDatabase()?.instaMediaDao()?.getAll()

        assertTrue(readAll?.size == 1)

        AppDatabase.getDatabase()?.instaMediaDao()?.deleteMedia(read!!)

        read = AppDatabase.getDatabase()?.instaMediaDao()?.findMediaById(mediaId)

        assertNull(read)
    }

    @Test
    fun testUserDb() {
        val userId = "user_1234567"

        val user = InstaUser(userId = userId, name = "TestUser")
        AppDatabase.getDatabase()?.instaUserDao()?.insertUser(user)

        var read = AppDatabase.getDatabase()?.instaUserDao()?.findByUserId(userId)

        assertEquals(userId, read?.userId)

        val readAll = AppDatabase.getDatabase()?.instaUserDao()?.getAll()

        assertTrue(readAll?.size == 1)

        var readRandom = AppDatabase.getDatabase()?.instaUserDao()?.getRandomuser()

        assertEquals(userId, readRandom?.userId)

        AppDatabase.getDatabase()?.instaUserDao()?.deleteUser(read!!)

        read = AppDatabase.getDatabase()?.instaUserDao()?.findByUserId("test12345678")

        assertNull(read)
    }
}
