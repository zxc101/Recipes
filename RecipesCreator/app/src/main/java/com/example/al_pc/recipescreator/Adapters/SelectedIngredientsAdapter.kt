package com.example.al_pc.recipescreator.Adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.al_pc.recipescreator.R
import com.example.al_pc.recipescreator.data.Ingredient
import kotlinx.android.synthetic.main.selected_ingredient_item.view.*

class SelectedIngredientsAdapter(list:MutableList<Ingredient>): RecyclerView.Adapter<SelectedIngredientsAdapter.ViewHolder>(){

    var mItems:MutableList<Ingredient> = list

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.name.text = item.name
        holder.count.text = item.count
        holder.dimension.text = item.dimension
        holder.button.setOnClickListener {
            mItems.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.selected_ingredient_item, parent, false)
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<Ingredient>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.ingredient_name
        val count = itemView.ingredient_count
        val dimension = itemView.ingredient_dimension
        val button = itemView.selectedIngredientBTN
    }
}