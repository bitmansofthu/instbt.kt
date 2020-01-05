package hr.ina.instabot.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.ina.instabot.R
import hr.ina.instabot.core.InstaBot
import hr.ina.instabot.core.WebInstaRequest
import hr.ina.instabot.core.model.InstaMedia
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.network.InstaCookieManager
import hr.ina.instabot.core.InstaAction
import hr.ina.instabot.core.InstaBotModel
import hr.ina.instabot.util.Settings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class InstaBotFragment : BaseFragment() {

    companion object {
        fun newInstance() : InstaBotFragment {
            val fr = InstaBotFragment()
            return fr
        }

        const val MAX_MEDIA_FOR_RANDOM = 20
        const val MIN_USER_FOR_UNFOLLOW = 10
    }

    val compositeDisposable = CompositeDisposable()
    lateinit var instabot : InstaBotModel

    val hashtags = arrayOf("instahun", "mutimitcsinalsz", "budapest", "magyarig", "ikozosseg", "mik", "like4like", "magyarinsta", "instagood", "hungariangirl", "sayyes", "eskuvo", "wedding", "mutimiteszel", "ihungary")

    lateinit var currentUserName: String
    lateinit var currentMedia: InstaMedia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hdrs = Settings.getHeaderStorage(context!!)
        val cookie = InstaCookieManager(hdrs)
        instabot = InstaBotModel(
            hashtags,
            arrayOf(
                InstaAction.LIKE,
                InstaAction.LIKE,
                InstaAction.LIKE,
                InstaAction.FOLLOW,
                InstaAction.LIKE,
                InstaAction.LIKE,
                InstaAction.UNFOLLOW,
                InstaAction.LIKE
            ),
            InstaBot(WebInstaRequest(cookie)),
            AppDatabase.getDatabase()!!
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instabot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        val ticker = Observable.interval(5, TimeUnit.MINUTES)
            .map {
                var action = instabot.getNextAction()
                val db = AppDatabase.getDatabase()!!

                if (action == InstaAction.UNFOLLOW && db.instaUserDao().getNumberOfUsers() >= MIN_USER_FOR_UNFOLLOW) {
                    action = InstaAction.LIKE
                }
                action
            }
            .flatMap {action ->
                when (action) {
                    InstaAction.LIKE -> instabot.obtainMedia(MAX_MEDIA_FOR_RANDOM).flatMap {
                        instabot.likeMedia(it)
                    }
                    InstaAction.FOLLOW -> instabot.obtainMedia(MAX_MEDIA_FOR_RANDOM).flatMap {
                        instabot.followUserByMedia(it)
                    }
                    InstaAction.UNFOLLOW -> instabot.unfollowRandomUser()
                }.subscribeOn(Schedulers.io()).toObservable()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {

            })
        compositeDisposable.add(ticker)
    }

    override fun onStop() {
        super.onStop()

        compositeDisposable.dispose()
    }

}