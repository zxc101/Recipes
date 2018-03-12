package com.example.al_pc.administrationrecipes.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.Recipe
import kotlinx.android.synthetic.main.recipe_stage_item.view.*

class RecipesAdapter(list:MutableList<Recipe>): RecyclerView.Adapter<RecipesAdapter.ViewHolder>(){

    lateinit var context: Context
    var mItems:MutableList<Recipe> = list

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.textViewRecipesStage.text = item.recipe
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.recipe_stage_item, parent, false)
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<Recipe>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewRecipesStage = itemView.recipes_stage
    }
}