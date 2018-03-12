package com.example.al_pc.recipescreator.Activities

import android.content.Context
import android.content.Intent
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
import com.example.al_pc.recipescreator.Adapters.IngredientsDBAdapter
import com.example.al_pc.recipescreator.Adapters.SelectedIngredientsAdapter
import com.example.al_pc.recipescreator.R
import com.example.al_pc.recipescreator.data.Ingredient
import com.example.al_pc.recipescreator.data.IngredientName
import com.example.al_pc.recipescreator.data.SearchRepository
import com.example.al_pc.recipescreator.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class CreateDishesIngredientsActivity : AppCompatActivity() {

    @BindView(R.id.name) lateinit var nameET: AppCompatEditText
    @BindView(R.id.count) lateinit var countET: AppCompatEditText
    @BindView(R.id.dimension) lateinit var dimensionET: AppCompatEditText
    @BindView(R.id.ingredients_db) lateinit var ingredientsDB: RecyclerView
    @BindView(R.id.selected_ingredients) lateinit var selectedIngredients: RecyclerView

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    val ingredientsDBList: MutableList<IngredientName> = mutableListOf()

    val ingredientsDBAdapter: IngredientsDBAdapter = IngredientsDBAdapter(ingredientsDBList)

    val selectedIngredientsList: MutableList<Ingredient> = mutableListOf()

    val selectedIngredientsAdapter: SelectedIngredientsAdapter = SelectedIngredientsAdapter(selectedIngredientsList)

    var isHere:Boolean = true
    var IngredientString:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dishes_ingredients)
        ButterKnife.bind(this)

        findingIngredientsDB()
        next()
    }

    override fun onResume() {
        super.onResume()
        val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE)

        val llmSelected = LinearLayoutManager(applicationContext)
        llmSelected.orientation = LinearLayoutManager.VERTICAL
        selectedIngredients.layoutManager = llmSelected

        selectedIngredients.adapter = selectedIngredientsAdapter

        val newDishPref = getSharedPreferences("NewDish", Context.MODE_PRIVATE)

        if(movePref.getString("move", "") == "update"){
            try {
                val oldDishPref = getSharedPreferences("OldDish", Context.MODE_PRIVATE)
                compositeDisposable.add(
                        repository.select_dishes_ingredients(oldDishPref.getString("category", ""), oldDishPref.getString("name", ""))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({ result ->
                                    var rememberIngredients = ""
                                    for ((index, ingredient) in result.withIndex()) {
                                        selectedIngredientsList.add(ingredient)
                                        if (index != result.count() - 1) {
                                            rememberIngredients += ingredient.name + "№" + ingredient.count + "№" + ingredient.dimension + "№№"
                                        } else {
                                            rememberIngredients += ingredient.name + "№" + ingredient.count + "№" + ingredient.dimension
                                        }
                                    }
                                    val editor = getSharedPreferences("OldDish", Context.MODE_PRIVATE).edit()
                                    editor.putString("ingredients", rememberIngredients)
                                    editor.apply()
                                    selectedIngredientsAdapter.notifyData(selectedIngredientsList)
                                })
                )
            }catch (ex:NullPointerException){
                val selectedIngredients = newDishPref.getString("ingredients", "").split("№№")

                for (i in selectedIngredients) {
                    Log.d("Test", i)
                    val ing = i.split('№')

                    val ingredient: Ingredient = Ingredient(ing[0], ing[1], ing[2])
                    selectedIngredientsList.add(ingredient)
                }
                selectedIngredientsAdapter.notifyData(selectedIngredientsList)
            }
        }

        if(movePref.getString("move", "").equals("insert") && !newDishPref.getString("ingredients", "").isEmpty()){
            val selectedIngredients = newDishPref.getString("ingredients", "").split("№№")

            for (i in selectedIngredients) {
                Log.d("Test", i)
                val ing = i.split('№')

                val ingredient: Ingredient = Ingredient(ing[0], ing[1], ing[2])
                selectedIngredientsList.add(ingredient)
            }
            selectedIngredientsAdapter.notifyData(selectedIngredientsList)
        }

        val llmDB = LinearLayoutManager(applicationContext)
        llmDB.orientation = LinearLayoutManager.VERTICAL
        ingredientsDB.layoutManager = llmDB
        isHere = true

        launch(UI) {
            ShowIngredientsThread()
        }
    }

    override fun onPause() {
        super.onPause()
        isHere = false
        if(ingredientsDBAdapter.selectedIngredientsList.size > 0) {
            val editor = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
            var rememberIngredients = ""

            for((index,ingredient) in ingredientsDBAdapter.selectedIngredientsList.withIndex()){
                if(index != ingredientsDBAdapter.selectedIngredientsList.count() - 1) {
                    rememberIngredients += ingredient.name + "№" + ingredient.count + "№" + ingredient.dimension + "№№"
                }else{
                    rememberIngredients += ingredient.name + "№" + ingredient.count + "№" + ingredient.dimension
                }
            }

            editor.putString("ingredients", rememberIngredients)
            editor.apply()
        }
    }

    override fun onStop() {
        super.onStop()
        selectedIngredientsList.clear()
    }

    fun findingIngredientsDB(){
        ingredientsDBAdapter.context = applicationContext
        ingredientsDBAdapter.textName = nameET
        ingredientsDBAdapter.textCount = countET
        ingredientsDBAdapter.textDimension = dimensionET
        ingredientsDBAdapter.ingredients = selectedIngredients
        ingredientsDBAdapter.selectedIngredientsList = selectedIngredientsList
        ingredientsDB.adapter = ingredientsDBAdapter

        nameET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int){
                ingredientsDBAdapter.isChanged = false
                ingredientsDBList.clear()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                IngredientString = nameET.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    fun next(){
        findViewById<AppCompatButton>(R.id.next).setOnClickListener {
            if(ingredientsDBAdapter.selectedIngredientsList.size > 0) {
                val newDishPref = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
                var rememberIngredients = ""
                for((index,ingredient) in ingredientsDBAdapter.selectedIngredientsList.withIndex()){
                    if(index != ingredientsDBAdapter.selectedIngredientsList.count() - 1) {
                        rememberIngredients += ingredient.name + "№" + ingredient.count + "№" + ingredient.dimension + "№№"
                    }else{
                        rememberIngredients += ingredient.name + "№" + ingredient.count + "№" + ingredient.dimension
                    }
                }
                newDishPref.putString("ingredients", rememberIngredients)
                newDishPref.apply()
                startActivity(Intent(this, CreateDishesRecipeActivity::class.java))
            }else{
                Toast.makeText(this, "Введите хотя бы один ингредиент.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun ShowIngredientsThread() {
        while (isHere) {
            delay(1000)
            ingredientsDBList.clear()
            compositeDisposable.add(
                    repository.selectIngredients(IngredientString)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                if (!result.get(0).ingredient.equals("") && !ingredientsDBAdapter.isChanged) {
                                    ingredientsDBList.addAll(result)
                                    ingredientsDBAdapter.notifyData(ingredientsDBList)
                                }else{
                                    ingredientsDBList.clear()
                                    ingredientsDBAdapter.notifyData(ingredientsDBList)
                                }
                            })
            )
        }
    }

    override fun onBackPressed() {
        val editor = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
        var rememberIngredients = ""

        for((index,ingredient) in ingredientsDBAdapter.selectedIngredientsList.withIndex()){
            if(index != ingredientsDBAdapter.selectedIngredientsList.count() - 1) {
                rememberIngredients += ingredient.name + "№" + ingredient.count + "№" + ingredient.dimension + "№№"
            }else{
                rememberIngredients += ingredient.name + "№" + ingredient.count + "№" + ingredient.dimension
            }
        }

        editor.putString("ingredients", rememberIngredients)
        editor.apply()
        startActivity(Intent(this, CreateDishesNameActivity::class.java))
    }
}