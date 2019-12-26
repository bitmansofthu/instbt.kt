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

        assertEquals(200, resp.statusCode)

        val len = resp.medias.size
        assertTrue(len > 0)

        assertNotNull(resp.medias[0].mediaId)
        assertNotNull(resp.medias[0].likeCount)
        assertNotNull(resp.medias[0].ownerId)
        assertNotNull(resp.medias[0].shortCode)
        assertNotNull(resp.medias[0].takenAt)
    }

    @Test
    fun testLike() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hdrs = Settings.getHeaderStorage(appContext)
        val cookie = InstaCookieManager(hdrs)

        val loggedIn = cookie.isLoggedIn

        val mediaId = "2207231572644826524"

        assertTrue(loggedIn)

        val req = WebInstaRequest(cookie)

        val likeResp = req.like(mediaId)
        assertEquals(200, likeResp.statusCode)
    }

    @Test
    fun testUnlike() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hdrs = Settings.getHeaderStorage(appContext)
        val cookie = InstaCookieManager(hdrs)

        val loggedIn = cookie.isLoggedIn

        val mediaId = "2207231572644826524"

        assertTrue(loggedIn)

        val req = WebInstaRequest(cookie)

        val likeResp = req.unlike(mediaId)
        assertEquals(200, likeResp.statusCode)
    }

    @Test
    fun testMediaInfo() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hdrs = Settings.getHeaderStorage(appContext)
        val cookie = InstaCookieManager(hdrs)

        val loggedIn = cookie.isLoggedIn

        val shortCode = "B6hqWvAhY2c"

        assertTrue(loggedIn)

        val req = WebInstaRequest(cookie)

        val mediaResp = req.getMediaInfo(shortCode)
        assertEquals(200, mediaResp.statusCode)
        assertNotNull(mediaResp.userName)
    }

    @Test
    fun testUserInfo() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hdrs = Settings.getHeaderStorage(appContext)
        val cookie = InstaCookieManager(hdrs)

        val userName = "kalman.eu"

        val loggedIn = cookie.isLoggedIn

        assertTrue(loggedIn)

        val req = WebInstaRequest(cookie)

        val userResp = req.getUserInfo(userName)
        assertEquals(200, userResp.statusCode)
        assertNotNull(userResp.userRoot)
        assertNotNull(userResp.followsCount)
        assertNotNull(userResp.followerCount)
        assertNotNull(userResp.mediaCount)
        assertNotNull(userResp.followsUser)
        assertNotNull(userResp.userFollows)
    }

    @Test
    fun testFollow() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hdrs = Settings.getHeaderStorage(appContext)
        val cookie = InstaCookieManager(hdrs)

        val userId= "3001001071"

        val loggedIn = cookie.isLoggedIn

        assertTrue(loggedIn)

        val req = WebInstaRequest(cookie)

        val followResp = req.follow(userId)
        assertEquals(200, followResp.statusCode)
    }

    @Test
    fun testUnfollow() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val hdrs = Settings.getHeaderStorage(appContext)
        val cookie = InstaCookieManager(hdrs)

        val userId= "3001001071"

        val loggedIn = cookie.isLoggedIn

        assertTrue(loggedIn)

        val req = WebInstaRequest(cookie)

        val followResp = req.unfollow(userId)
        assertEquals(200, followResp.statusCode)
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
