package com.example.al_pc.recipes.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.*
import android.text.Editable
import android.text.TextWatcher
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.recipes.Adapters.DishesAdapter
import com.example.al_pc.recipes.R
import com.example.al_pc.recipes.data.DishesName
import com.example.al_pc.recipes.data.SearchRepository
import com.example.al_pc.recipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startActivity
import java.nio.file.OpenOption

class DishesMainActivity : AppCompatActivity() {

    @BindView(R.id.input_dishes) lateinit var inputDishes:AppCompatEditText
    @BindView(R.id.dishes) lateinit var dishes:RecyclerView

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    val dishesList: MutableList<DishesName> = mutableListOf()

    val dishesAdapter: DishesAdapter = DishesAdapter(dishesList)

    var isHere:Boolean = true
    var dishesString:String = ""

    override fun onResume() {
        super.onResume()
        val llm = LinearLayoutManager(applicationContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        dishes.layoutManager = llm
        isHere = true
        launch (UI) {
            ShowDishes()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dishes_main)
        ButterKnife.bind(this)
        init()
    }

    fun init(){
        checkingAutentification()
        findingDishes()
        openOptions()
        exit()
    }

    override fun onPause() {
        super.onPause()
        isHere = false
    }

    fun checkingAutentification(){
        val pref = getSharedPreferences("User", Context.MODE_PRIVATE)
        if(pref.getString("nickname", "").isEmpty()){
            startActivity(Intent(this, AutentificationActivity::class.java))
        }
    }

    fun findingDishes(){
        dishesAdapter.context = this
        dishes.adapter = dishesAdapter

        inputDishes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int){
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                dishesString = inputDishes.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    fun openOptions(){
        findViewById<AppCompatButton>(R.id.options).setOnClickListener {
            startActivity<OptionsActivity>()
        }
    }

    fun exit(){
        findViewById<AppCompatButton>(R.id.exit).setOnClickListener {
            val editor = getSharedPreferences("User", Context.MODE_PRIVATE).edit()
            editor.putString("email", "")
            editor.putString("category", "")
            editor.putString("nickname", "")
            editor.apply()
            startActivity(Intent(this, AutentificationActivity::class.java))
        }
    }

    suspend fun ShowDishes(){
        while(isHere) {
            delay(1000)
            dishesList.clear()
            compositeDisposable.add(
                    repository.selectDishesAndCategories(dishesString)
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

    override fun onBackPressed() {
    }
}
