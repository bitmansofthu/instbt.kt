package hr.ina.instabot.fragment

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ina.instabot.R
import hr.ina.instabot.core.InstaAction
import hr.ina.instabot.core.InstaBotActionResult
import kotlinx.android.synthetic.main.activity_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActionActivityAdapter(val context : Context) : RecyclerView.Adapter<ViewHolder>() {

    private val entries = ArrayList<InstaBotActionResult>()

    fun add(item: InstaBotActionResult) {
        entries.add(item)
        notifyDataSetChanged()
    }

    fun clear() {
        entries.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]

        val dateformat = DateFormat.format("yyyy-MM-dd HH:mm:ss", entry.timestamp)

        when (entry.action) {
            InstaAction.LIKE -> {
                holder.statusText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0)
                holder.statusText.text = "$dateformat\n${entry.media?.shortCode}"
            }
            InstaAction.FOLLOW -> {
                holder.statusText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_follow, 0, 0, 0)
                holder.statusText.text = "$dateformat\n${entry.userName}"
            }
            InstaAction.UNFOLLOW -> {
                holder.statusText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unfollow, 0, 0, 0)
                holder.statusText.text = "$dateformat\n${entry.userName}"
            }
        }

        if (entry.failureMessage != null) {
            holder.failureText.text = entry.failureMessage
            holder.failureText.visibility = View.VISIBLE
        } else {
            holder.failureText.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val statusText = view.statusText!!
    val failureText = view.failureText!!
}