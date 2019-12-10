package hr.ina.instabot.fragment

import androidx.fragment.app.FragmentActivity
import hr.ina.instabot.R

class Navigation(private val activity: FragmentActivity) {

    fun showLogin() {
        activity?.supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment.newInstance()).commit()
    }

}