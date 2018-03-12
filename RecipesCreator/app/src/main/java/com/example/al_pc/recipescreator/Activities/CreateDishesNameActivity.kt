package com.example.al_pc.recipescreator.Activities

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.recipescreator.Adapters.CategoriesAdapter
import com.example.al_pc.recipescreator.R
import com.example.al_pc.recipescreator.data.Category
import com.example.al_pc.recipescreator.data.SearchRepository
import com.example.al_pc.recipescreator.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.alert

class CreateDishesNameActivity : AppCompatActivity() {

    @BindView(R.id.name) lateinit var name:AppCompatEditText
    @BindView(R.id.category) lateinit var category:AppCompatEditText
    @BindView(R.id.categories) lateinit var categories:RecyclerView

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    val categoriesList: MutableList<Category> = mutableListOf()

    val categoriesAdapter: CategoriesAdapter = CategoriesAdapter(categoriesList)

    var isHere:Boolean = true
    var categoriesString:String = ""

    override fun onResume() {
        super.onResume()
        val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE)
        if(movePref.getString("move", "").equals("update")){
            category.text.clear()
            name.text.clear()
            try {
                val intent: Intent = getIntent()
                category.text.append(intent.getStringExtra("category"))
                name.text.append(intent.getStringExtra("name"))
                val editor = getSharedPreferences("OldDish", Context.MODE_PRIVATE).edit()
                editor.putString("name", name.text.toString())
                editor.putString("category", category.text.toString())
                editor.apply()
            }catch (ex:NullPointerException){
                val newDishPref = getSharedPreferences("NewDish", Context.MODE_PRIVATE)
                name.text.append(newDishPref.getString("name", ""))
                category.text.append(newDishPref.getString("category", ""))
            }
            categoriesAdapter.isChanged = true
        }
        if(movePref.getString("move", "").equals("insert")){
            val newDishPref = getSharedPreferences("NewDish", Context.MODE_PRIVATE)
            category.text.clear()
            name.text.clear()
            category.text.append(newDishPref.getString("category", ""))
            name.text.append(newDishPref.getString("name", ""))
        }
        val llm = LinearLayoutManager(applicationContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        categories.layoutManager = llm
        isHere = true
        launch(UI) {
            ShowCategoryThread()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dishes_name)
        ButterKnife.bind(this)

        findingCategories()
        next()
    }

    override fun onPause() {
        super.onPause()
        isHere = false
        val newDishPref = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
        newDishPref.putString("name", name.text.toString())
        newDishPref.putString("category", category.text.toString())
        newDishPref.apply()
    }

    override fun onStop() {
        super.onStop()
        name.text.clear()
        category.text.clear()
    }

    fun findingCategories(){
        categoriesAdapter.context = applicationContext
        categoriesAdapter.textCategory = category
        categories.adapter = categoriesAdapter

        category.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int){
                categoriesAdapter.isChanged = false
                categoriesList.clear()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                categoriesString = category.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    fun next(){
        findViewById<AppCompatButton>(R.id.next).setOnClickListener {
            val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE)
            if(movePref.getString("move", "").equals("insert")) {
                compositeDisposable.add(
                        repository.is_have_dish_name(name.text.toString())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({ result ->
                                    val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE)
                                    if (result.result.equals("true")) {
                                        if (!name.text.toString().equals("") && !category.text.toString().equals("")) {
                                            val editor = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
                                            editor.putString("name", name.text.toString())
                                            editor.putString("category", category.text.toString())
                                            editor.apply()
                                            startActivity(Intent(this, CreateDishesIngredientsActivity::class.java))
                                        } else {
                                            Toast.makeText(this, "Заполните все поля.", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(this, "Такое название уже есть.", Toast.LENGTH_SHORT).show()
                                    }
                                })
                )
            }else{
                val editor = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
                editor.putString("name", name.text.toString())
                editor.putString("category", category.text.toString())
                editor.apply()
                startActivity(Intent(this, CreateDishesIngredientsActivity::class.java))
            }
        }
    }

    suspend fun ShowCategoryThread() {
        while (isHere) {
            delay(1000)
            if(!categoriesAdapter.isChanged) {
                categoriesList.clear()
                compositeDisposable.add(
                        repository.selectCategories(categoriesString)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({ result ->
                                    if (!result[0].category.equals("") && !categoriesAdapter.isChanged) {
                                        categoriesList.addAll(result)
                                        categoriesAdapter.notifyData(categoriesList)
                                    } else {
                                        categoriesList.clear()
                                        categoriesAdapter.notifyData(categoriesList)
                                    }
                                })
                )
            }
        }
    }

    override fun onBackPressed() {
        alterDialog()
    }

    private fun alterDialog(){
        alert("Точно выйти?"){
            positiveButton("Да"){
                startActivity(Intent(applicationContext, CabinetCreatorActivity::class.java))
            }
            negativeButton("Нет"){
            }
        }.show()
    }
}