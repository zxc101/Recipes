package com.example.al_pc.administrationrecipes.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.al_pc.administrationrecipes.Activities.DishActivity
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.DishesName
import com.example.al_pc.administrationrecipes.data.SearchRepository
import com.example.al_pc.administrationrecipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dish_item.view.*


class DishesAdapter(list:MutableList<DishesName>): RecyclerView.Adapter<DishesAdapter.ViewHolder>(){

    lateinit var context: Context
    var mItems:MutableList<DishesName> = list

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.textViewCategory.text = item.category
        holder.textViewName.text = item.name
        holder.textViewNickname.text = item.creator
        holder.selectorDishBTN.setOnClickListener {
            val intent = Intent(context, DishActivity::class.java)
            intent.putExtra("name", item.name)
            intent.putExtra("category", item.category)
            intent.putExtra("creator", item.creator)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        holder.negativeBTN.setOnClickListener {
            compositeDisposable.add(
                    repository.deleteDish(item.category, item.name)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                if (result.result.equals("success")) {
                                    Toast.makeText(context, "Рецепт был удалён", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_LONG).show()
                                }
                            })
            )
            mItems.removeAt(position)
            notifyDataSetChanged()
        }

        holder.positiveBTN.setOnClickListener {
            compositeDisposable.add(
                    repository.confirmationDish(item.category, item.name)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                if (result.result.equals("success")) {
                                    Toast.makeText(context, "Рецепт был принят", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_LONG).show()
                                }
                            })
            )
            mItems.removeAt(position)
            notifyDataSetChanged()
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
        val selectorDishBTN = itemView.selector_dish
        val negativeBTN = itemView.negativeBTN
        val positiveBTN = itemView.positiveBTN
    }
}