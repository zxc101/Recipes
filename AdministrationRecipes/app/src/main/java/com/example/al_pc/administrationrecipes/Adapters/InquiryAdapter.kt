package com.example.al_pc.administrationrecipes.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.Inquiry
import com.example.al_pc.administrationrecipes.data.SearchRepository
import com.example.al_pc.administrationrecipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.inquiry_item.view.*

class InquiryAdapter(list:MutableList<Inquiry>): RecyclerView.Adapter<InquiryAdapter.ViewHolder>(){

    var mItems:MutableList<Inquiry> = list
    lateinit var context:Context

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.email.text = item.email
        holder.negativeBTN.setOnClickListener {
            compositeDisposable.add(
                    repository.deleteInquery(item.email)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                if (result.result.equals("success")) {
                                    Toast.makeText(context, "В запросе было отказано", Toast.LENGTH_LONG).show()
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
                    repository.updateUsersCategory(item.email)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                if (result.result.equals("success")) {
                                    Toast.makeText(context, "Категория была изменена", Toast.LENGTH_LONG).show()
                                }else if(result.result.equals("error 1")){
                                    Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_LONG).show()
                                }else if(result.result.equals("error 2") || result.result.equals("error 3")) {
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
        val view = layoutInflater.inflate(R.layout.inquiry_item, parent, false)
        return ViewHolder(view)
    }

    fun notifyData(updateList:MutableList<Inquiry>) {
        mItems = updateList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val email = itemView.emailTB
        val negativeBTN = itemView.negativeBTN
        val positiveBTN = itemView.positiveBTN
    }
}