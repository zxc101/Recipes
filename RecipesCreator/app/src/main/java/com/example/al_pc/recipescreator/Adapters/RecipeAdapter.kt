package com.example.al_pc.recipescreator.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.al_pc.recipescreator.R
import kotlinx.android.synthetic.main.recipe_item.view.*

class RecipeAdapter(list:MutableList<String>): RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    var mItems:MutableList<String> = list

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.recipe.text = item
        holder.button.setOnClickListener {

            mItems.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<String>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val recipe = itemView.recipe
        val button = itemView.recipeBTN
    }
}