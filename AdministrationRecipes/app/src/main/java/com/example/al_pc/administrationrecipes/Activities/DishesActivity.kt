package com.example.al_pc.administrationrecipes.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.administrationrecipes.Adapters.DishesAdapter
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.DishesName
import com.example.al_pc.administrationrecipes.data.SearchRepository
import com.example.al_pc.administrationrecipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DishesActivity :AppCompatActivity() {
    @BindView(R.id.dishes) lateinit var dishes: RecyclerView

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    val dishesList: MutableList<DishesName> = mutableListOf()

    val dishesAdapter: DishesAdapter = DishesAdapter(dishesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dishes)
        ButterKnife.bind(this)

        val llm = LinearLayoutManager(applicationContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        dishes.layoutManager = llm
        dishesAdapter.context = this
        dishes.adapter = dishesAdapter

        compositeDisposable.add(
                repository.selectDishesAndCategories()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({result ->
                            if(!result[0].name.equals("")) {
                                dishesList.addAll(result)
                                dishesAdapter.notifyData(dishesList)
                            }else{
                                dishesList.clear()
                                dishesAdapter.notifyData(dishesList)
                            }
                        })
        )
    }
}