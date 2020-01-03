package hr.ina.instabot.core

import android.util.Log
import okhttp3.Response
import java.lang.Exception
import java.util.*

class UserInfoInstaResponse(response: Response) : HtmlInstaResponse(response) {

    companion object {
        const val TAG = "UserInfoInstaResponse"
    }

    val userRoot = try {
        html?.sharedData?.getJSONObject("entry_data")?.getJSONArray("ProfilePage")?.getJSONObject(0)?.getJSONObject("graphql")?.getJSONObject("user")
    } catch (e: Exception) {
        Log.w(TAG, "Failed to evaulate user data", e)
        null
    }

    val userName = try {
        userRoot?.getString("username")
    } catch (e : Exception) {
        Log.w(TAG, "Failed to evaulate userName", e)
        null
    }
    val followsUser = try {
        userRoot?.getBoolean("follows_viewer")
    } catch (e : Exception) {
        Log.w(TAG, "Failed to evaulate follows_viewer", e)
        null
    }
    val userFollows = try {
        userRoot?.getBoolean("followed_by_viewer")
    } catch (e : Exception) {
        Log.w(TAG, "Failed to evaulate followed_by_viewer", e)
        null
    }
    val mediaCount = try {
        userRoot?.getJSONObject("edge_owner_to_timeline_media")?.getInt("count")
    } catch (e : Exception) {
        Log.w(TAG, "Failed to evaulate edge_owner_to_timeline_media", e)
        null
    }
    val followsCount = try {
        userRoot?.getJSONObject("edge_follow")?.getInt("count")
    } catch (e : Exception) {
        Log.w(TAG, "Failed to evaulate edge_follow", e)
        null
    }
    val followerCount = try {
        userRoot?.getJSONObject("edge_followed_by")?.getInt("count")
    } catch (e : Exception) {
        Log.w(TAG, "Failed to evaulate edge_followed_by", e)
        null
    }
    val lastMediaTaken = try {
        Date(userRoot?.getJSONObject("edge_owner_to_timeline_media")?.getJSONArray("edges")?.getJSONObject(0)?.getJSONObject("node")?.getLong("taken_at_timestamp")!!)
    } catch (e : Exception) {
        Log.w(TAG, "Failed to evaulate edge_owner_to_timeline_media", e)
        null
    }
}