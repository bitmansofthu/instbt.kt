package hr.ina.instabot.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_instabot.view.*
import java.util.concurrent.TimeUnit

class InstaBotFragment : BaseFragment() {

    companion object {
        fun newInstance() : InstaBotFragment {
            val fr = InstaBotFragment()
            return fr
        }

        const val MAX_MEDIA_FOR_RANDOM = 20
        const val MIN_USER_FOR_UNFOLLOW = 10

        const val TAG = "InstabotFragment"
    }

    private val compositeDisposable = CompositeDisposable()
    lateinit var instabot : InstaBotModel

    lateinit var actionActivityAdapter: ActionActivityAdapter

    val hashtags = arrayOf("instahun", "mutimitcsinalsz", "budapest", "magyarig", "ikozosseg", "mik", "like4like", "magyarinsta", "instagood", "hungariangirl", "sayyes", "eskuvo", "wedding", "mutimiteszel", "ihungary")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionActivityAdapter = ActionActivityAdapter(context!!)

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
        view.activity_list.layoutManager = LinearLayoutManager(context)
        view.activity_list.adapter = actionActivityAdapter
    }

    override fun onStart() {
        super.onStart()

        val ticker = Observable.interval(0, 2, TimeUnit.MINUTES)
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.create<InstaAction> {
                    var action = instabot.getNextAction()
                    val db = AppDatabase.getDatabase()!!

                    if (action == InstaAction.UNFOLLOW && db.instaUserDao().getNumberOfUsers() >= MIN_USER_FOR_UNFOLLOW) {
                        it.onSuccess(InstaAction.LIKE)
                    } else {
                        it.onSuccess(action)
                    }
                }.toObservable()
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
                }.toObservable()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                actionActivityAdapter.add(it)
            }, {
                Log.e(TAG, "", it)
                compositeDisposable.dispose()
            })
        compositeDisposable.add(ticker)
    }

    override fun onStop() {
        super.onStop()

        compositeDisposable.dispose()
    }

}