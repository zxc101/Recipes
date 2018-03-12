package com.example.al_pc.recipescreator.Activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.recipescreator.Adapters.DishAdapter
import com.example.al_pc.recipescreator.R
import com.example.al_pc.recipescreator.data.DishName
import com.example.al_pc.recipescreator.data.SearchRepository
import com.example.al_pc.recipescreator.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_dishes_name.*

class SelectDishActivity : AppCompatActivity() {

    @BindView(R.id.dishes) lateinit var dishes:RecyclerView

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    val dishesList: MutableList<DishName> = mutableListOf()

    val dishAdapter: DishAdapter = DishAdapter(dishesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_dish)
        ButterKnife.bind(this)

        val llm = LinearLayoutManager(applicationContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        dishes.layoutManager = llm

        val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE)
        dishAdapter.move = movePref.getString("move","")

        dishes.adapter = dishAdapter

        val userPref = getSharedPreferences("User", Context.MODE_PRIVATE)
        val creator = userPref.getString("nickname", "")
        dishAdapter.creator = creator
        dishAdapter.context = this

        compositeDisposable.add(
                repository.selectDishNameAndCategory(creator)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({result ->
                            if(!(result[0].name.equals("") && result[0].category.equals(""))) {
                                dishesList.addAll(result)
                                dishAdapter.notifyData(dishesList)
                            }
                        })
        )
    }

}