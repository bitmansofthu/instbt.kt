package hr.ina.instabot.fragment

import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    lateinit var navigation : Navigation

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigation = Navigation(activity!!)
    }

}