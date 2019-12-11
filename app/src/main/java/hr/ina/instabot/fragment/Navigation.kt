package hr.ina.instabot.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import hr.ina.instabot.R

class Navigation(private val activity: FragmentActivity) {

    fun login() : NavigationMethod {
        return NavigationMethod(activity, LoginFragment.newInstance())
    }

    fun navigateUp() {
        activity.supportFragmentManager.popBackStack()
    }

    fun navigateTo(backstackState: String) {
        activity.supportFragmentManager.popBackStack(backstackState, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}

class NavigationMethod(private val activity: FragmentActivity, private val fragment: Fragment) {

    fun show(addToBackstack: Boolean = false, backstackState: String?) {
        val trans = activity?.supportFragmentManager.beginTransaction()
        trans.replace(R.id.container, fragment)
        if (addToBackstack)
            trans.addToBackStack(backstackState)
        trans.commit()
    }

}