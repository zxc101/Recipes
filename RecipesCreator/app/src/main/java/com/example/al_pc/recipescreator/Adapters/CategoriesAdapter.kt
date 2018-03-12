package com.example.al_pc.recipescreator.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.al_pc.recipescreator.R
import com.example.al_pc.recipescreator.data.Category
import kotlinx.android.synthetic.main.category_item.view.*

class CategoriesAdapter(list:MutableList<Category>): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>(){

    lateinit var textCategory: AppCompatEditText
    lateinit var context:Context
    var isChanged:Boolean = false
    var mItems:MutableList<Category> = list

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.category.text = item.category
        holder.button.setOnClickListener {
            textCategory.text.clear()
            textCategory.text.append(item.category)
            isChanged = true
            mItems.clear()
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<Category>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val category = itemView.categoryTV
        val button = itemView.categoryBTN
    }
}