package com.example.al_pc.recipes.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.al_pc.recipes.Activities.DishActivity
import com.example.al_pc.recipes.R
import com.example.al_pc.recipes.data.DishesName
import kotlinx.android.synthetic.main.dish_item.view.*

class DishesAdapter(list:MutableList<DishesName>): RecyclerView.Adapter<DishesAdapter.ViewHolder>(){

    lateinit var context:Context
    var mItems:MutableList<DishesName> = list

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.textViewCategory.text = item.category
        holder.textViewName.text = item.name
        holder.textViewNickname.text = item.creator
        holder.button.setOnClickListener {
            val intent = Intent(context, DishActivity::class.java)
            intent.putExtra("name", item.name)
            intent.putExtra("category", item.category)
            intent.putExtra("creator", item.creator)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.dish_item,parent, false)
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<DishesName>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewCategory = itemView.category
        val textViewName = itemView.name
        val textViewNickname = itemView.creater
        val button = itemView.selector_dish
    }
}