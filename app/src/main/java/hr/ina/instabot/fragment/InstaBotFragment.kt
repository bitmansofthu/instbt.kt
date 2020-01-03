package hr.ina.instabot.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.ina.instabot.R
import hr.ina.instabot.core.InstaBot
import hr.ina.instabot.core.WebInstaRequest
import hr.ina.instabot.data.AppDatabase
import hr.ina.instabot.network.InstaCookieManager
import hr.ina.instabot.util.ACTION
import hr.ina.instabot.util.InstaBotRx
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
    }

    val compositeDisposable = CompositeDisposable()
    lateinit var instabot : InstaBotRx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hdrs = Settings.getHeaderStorage(context!!)
        val cookie = InstaCookieManager(hdrs)
        instabot = InstaBotRx(arrayOf(ACTION.FOLLOW, ACTION.LIKE), InstaBot(WebInstaRequest(cookie)), AppDatabase.getDatabase()!!)
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

        val ticker = Observable.interval(5, TimeUnit.MINUTES, Schedulers.io())
            .map {
                instabot.getRandomMedia("")
            }
            .map {

            }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
        compositeDisposable.add(ticker)
    }

    override fun onStop() {
        super.onStop()

        compositeDisposable.dispose()
    }

}