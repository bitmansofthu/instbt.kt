package hr.ina.instabot.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
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
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class InstaBotFragment : BaseFragment() {

    companion object {
        fun newInstance() : InstaBotFragment {
            val fr = InstaBotFragment()
            return fr
        }

        const val MAX_MEDIA_FOR_HASHTAG = 10
        const val MIN_USER_FOR_UNFOLLOW = 10
        const val DEFAULT_ACTION_INTERVAL_MINUTES : Long = 2
        const val DEFAULT_ACTION_DELAY_MIN_SECONDS : Long = 20
        const val DEFAULT_ACTION_DELAY_MAX_SECONDS : Long = 180

        const val TAG = "InstabotFragment"
    }

    private lateinit var compositeDisposable : CompositeDisposable
    lateinit var instabot : InstaBotModel

    lateinit var actionActivityAdapter: ActionActivityAdapter

    val hashtags = arrayOf("instahun", "mutimitcsinalsz", "budapest", "magyarig", "ikozosseg", "mik", "like4like", "magyarinsta", "instagood", "hungariangirl", "sayyes", "eskuvo", "wedding", "mutimiteszel", "ihungary")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

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
                InstaAction.FOLLOW,
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
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        view.activity_list.layoutManager = layoutManager
        view.activity_list.adapter = actionActivityAdapter
        view.activity_list.addItemDecoration(DividerItemDecoration(context,
            layoutManager.orientation))

        activity?.title = "TestBot"
    }

    override fun onStart() {
        super.onStart()

        compositeDisposable = CompositeDisposable()

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val ticker = Observable.interval(0, DEFAULT_ACTION_INTERVAL_MINUTES, TimeUnit.MINUTES)
            .subscribeOn(Schedulers.io())
            .flatMap {
                Single.create<InstaAction> {
                    var action = instabot.getNextAction()
                    val db = AppDatabase.getDatabase()!!

                    if (action == InstaAction.UNFOLLOW && db.instaUserDao().getNumberOfUsers() < MIN_USER_FOR_UNFOLLOW) {
                        it.onSuccess(InstaAction.LIKE)
                    } else {
                        it.onSuccess(action)
                    }
                }.toObservable()
            }
            .delay((DEFAULT_ACTION_DELAY_MIN_SECONDS..DEFAULT_ACTION_DELAY_MAX_SECONDS).random(), TimeUnit.SECONDS)
            .flatMap {action ->
                when (action) {
                    InstaAction.LIKE -> instabot.obtainMedia(MAX_MEDIA_FOR_HASHTAG).flatMap {
                        instabot.likeMedia(it)
                    }
                    InstaAction.FOLLOW -> instabot.obtainMedia(MAX_MEDIA_FOR_HASHTAG).flatMap {
                        instabot.followUserByMedia(it)
                    }
                    InstaAction.UNFOLLOW -> instabot.unfollowRandomUser()
                }.toObservable()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                actionActivityAdapter.add(it)
                view?.current_hashtag?.text = "#${instabot.currentHashtag}"
            }, {
                Log.e(TAG, "Fatal error", it)

                view?.error_status?.text = it.message
                view?.error_status?.visibility = View.VISIBLE

                //compositeDisposable.dispose()
            })
        compositeDisposable.add(ticker)
    }

    override fun onStop() {
        super.onStop()

        compositeDisposable.dispose()

        view?.error_status?.visibility = View.GONE

        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_testbot, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_logout -> {
                navigation.logout().show(true,"logout")

                return true
            }
        }

        return false
    }

}