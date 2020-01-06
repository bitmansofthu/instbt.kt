package hr.ina.instabot.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ina.instabot.R
import hr.ina.instabot.core.InstaAction
import hr.ina.instabot.core.InstaBotActionResult
import kotlinx.android.synthetic.main.activity_item.view.*

class ActionActivityAdapter(val context : Context) : RecyclerView.Adapter<ViewHolder>() {

    private val entries = ArrayList<InstaBotActionResult>()

    fun add(item: InstaBotActionResult) {
        entries.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]

        when (entry.action) {
            InstaAction.LIKE -> {
                holder.statusText.text = "LIKED media " + entry.media?.shortCode
            }
            InstaAction.FOLLOW -> {
                holder.statusText.text = "FOLLOWED user " + entry.userName
            }
            InstaAction.UNFOLLOW -> {
                holder.statusText.text = "UNFOLLOWED user " + entry.userName
            }
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val statusText= view.statusText
}