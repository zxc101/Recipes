package com.example.al_pc.recipescreator.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.al_pc.recipescreator.Activities.CreateDishesNameActivity
import com.example.al_pc.recipescreator.R
import com.example.al_pc.recipescreator.data.DishName
import com.example.al_pc.recipescreator.data.Ingredient
import com.example.al_pc.recipescreator.data.SearchRepository
import com.example.al_pc.recipescreator.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dish_item.view.*

class DishAdapter(list:MutableList<DishName>): RecyclerView.Adapter<DishAdapter.ViewHolder>() {

//    interface OnCardClickListener {
//        fun onCardClick(position: Int)
//    }

    var mItems:MutableList<DishName> = list
    lateinit var move: String
    lateinit var creator: String
    lateinit var  context: Context
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.category.text = item.category
        holder.name.text = item.name
        holder.button.setOnClickListener {
            if(move == "update"){
                val intent = Intent(context, CreateDishesNameActivity::class.java)
                intent.putExtra("category", item.category)
                intent.putExtra("name", item.name)
                intent.putExtra("creator", creator)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }else if(move == "delete"){

                compositeDisposable.add(
                repository.delete_dish(item.category, item.name)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                )
            }
            mItems.removeAt(position)
            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.dish_item, parent, false)
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<DishName>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val category = itemView.category
        val name = itemView.name
        val button = itemView.selector_dish
    }
}