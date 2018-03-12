package com.example.al_pc.administrationrecipes.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.Log
import kotlinx.android.synthetic.main.log_item.view.*

class LogsAdapter(list:MutableList<Log>): RecyclerView.Adapter<LogsAdapter.ViewHolder>(){

    var mItems:MutableList<Log> = list

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.time.text = item.time
        holder.event.text = item.event
        holder.userId.text = item.userId
        holder.userEmail.text = item.userEmail
        holder.userNickname.text = item.userNickname
        holder.userCategory.text = item.userCategory
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.log_item, parent, false)
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<Log>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val time = itemView.timeTB
        val event = itemView.eventTB
        val userId = itemView.userIdTB
        val userEmail = itemView.userEmailTB
        val userNickname = itemView.userNicknameTB
        val userCategory = itemView.userCategoryTB
    }
}