package com.example.al_pc.recipes.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.al_pc.recipes.R
import com.example.al_pc.recipes.data.Ingredient
import kotlinx.android.synthetic.main.dish_item.view.*
import kotlinx.android.synthetic.main.ingredient_item.view.*

class IngredientsAdapter(list:MutableList<Ingredient>): RecyclerView.Adapter<IngredientsAdapter.ViewHolder>(){

    lateinit var context: Context
    var mItems:MutableList<Ingredient> = list

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.textViewIngredient.text = item.ingredient + " " +
                                         item.count + " " +
                                         item.dimension
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.ingredient_item, parent, false)
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
        val textViewIngredient = itemView.ingredient
    }
}
