package com.example.al_pc.recipescreator.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.recipescreator.Adapters.CategoriesAdapter
import com.example.al_pc.recipescreator.Adapters.RecipeAdapter
import com.example.al_pc.recipescreator.R
import com.example.al_pc.recipescreator.data.Category
import com.example.al_pc.recipescreator.data.Ingredient
import com.example.al_pc.recipescreator.data.SearchRepository
import com.example.al_pc.recipescreator.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert

class CreateDishesRecipeActivity : AppCompatActivity() {

    @BindView(R.id.recipeRV) lateinit var recipes:RecyclerView
    @BindView(R.id.recipeET) lateinit var recipe:AppCompatEditText

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    var recipeList: MutableList<String> = mutableListOf()

    val recipeAdapter: RecipeAdapter = RecipeAdapter(recipeList)

    override fun onResume() {
        super.onResume()
        val newDishPref = getSharedPreferences("NewDish", Context.MODE_PRIVATE)
        val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE)
        val Recipe: String = newDishPref.getString("recipes", "")

        if(movePref.getString("move", "") == "update") {
            try {
                val oldDishPref = getSharedPreferences("OldDish", Context.MODE_PRIVATE)
                compositeDisposable.add(
                        repository.select_stages_recipe(oldDishPref.getString("category", ""), oldDishPref.getString("name", ""))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({ result ->
                                    val editorNewDish = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
                                    var rememberRecipe = ""
                                    for ((index, value) in result.withIndex()) {
                                        recipeList.add(value.recipe)
                                        if (index != result.count() - 1) {
                                            rememberRecipe += value.recipe + "№"
                                        } else {
                                            rememberRecipe += value.recipe
                                        }
                                    }
                                    editorNewDish.putString("recipes", rememberRecipe)
                                    editorNewDish.apply()
                                    recipeAdapter.notifyData(recipeList)
                                })
                )
            }catch (ex:NullPointerException){
                recipes.adapter = recipeAdapter
                recipeList = Recipe.split("№").toMutableList()
                recipeAdapter.notifyData(recipeList)
            }
        }

        if(movePref.getString("move", "").equals("insert") && !newDishPref.getString("recipes", "").isEmpty()){
            recipes.adapter = recipeAdapter
            recipeList = Recipe.split("№").toMutableList()
            recipeAdapter.notifyData(recipeList)
        }

        val llm = LinearLayoutManager(applicationContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        recipes.layoutManager = llm
        recipes.adapter = recipeAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dishes_recipe)
        ButterKnife.bind(this)

        findViewById<AppCompatButton>(R.id.add).setOnClickListener{
            if (!recipe.text.trim().isEmpty()){
                recipeList.add(recipe.text.toString())
                recipeAdapter.notifyData(recipeList)
                recipe.text.clear()
            }
        }

        findViewById<AppCompatButton>(R.id.inspection).setOnClickListener {
            if(recipeList.count() == 0) {
                Toast.makeText(this, "Введите хотя бы одно действие.", Toast.LENGTH_SHORT).show()
            } else {
                val editor = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
                var rememberRecipe = ""
                for ((index, recipe) in recipeList.withIndex()) {
                    if (index != recipeList.count() - 1) {
                        rememberRecipe += recipe + "№"
                    } else {
                        rememberRecipe += recipe
                    }
                }
                editor.putString("recipes", rememberRecipe)
                editor.apply()

                val oldDishPref = getSharedPreferences("OldDish", Context.MODE_PRIVATE)
                val newDishPref = getSharedPreferences("NewDish", Context.MODE_PRIVATE)
                val userPref = getSharedPreferences("User", Context.MODE_PRIVATE)
                val movePref = getSharedPreferences("Move", Context.MODE_PRIVATE)

                val oldCategory: String = oldDishPref.getString("category", "")
                val oldName: String = oldDishPref.getString("name", "")
                val oldIngredients: String = oldDishPref.getString("ingredients", "")

                val newCategory: String = newDishPref.getString("category", "")
                val newName: String = newDishPref.getString("name", "")
                val newIngredients: String = newDishPref.getString("ingredients", "")
                val recipe: String = newDishPref.getString("recipes", "")
                val creator: String = userPref.getString("nickname", "")

                if(movePref.getString("move", "") == "insert") {
                    compositeDisposable.add(
                            repository.create_dish(newCategory, newName, newIngredients, recipe, creator)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe()
                    )
                }

                if(movePref.getString("move", "") == "update") {
                    compositeDisposable.add(
                            repository.update_dish(oldCategory, newCategory,
                                                   oldName, newName,
                                                   oldIngredients, newIngredients,
                                                   recipe, creator)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe()
                    )
                }

                startActivity(Intent(this, CabinetCreatorActivity::class.java))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val editor = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
        var rememberRecipe = ""
        for((index,recipe) in recipeList.withIndex()){
            if(index != recipeList.count() - 1) {
                rememberRecipe += recipe + "№"
            }else{
                rememberRecipe += recipe
            }
        }
        editor.putString("recipes", rememberRecipe)
        editor.apply()
    }

    override fun onBackPressed() {
        val editor = getSharedPreferences("NewDish", Context.MODE_PRIVATE).edit()
        var rememberRecipe = ""
        for((index,recipe) in recipeList.withIndex()){
            if(index != recipeList.count() - 1) {
                rememberRecipe += recipe + "№"
            }else{
                rememberRecipe += recipe
            }
        }
        editor.putString("recipes", rememberRecipe)
        editor.apply()

        startActivity(Intent(this, CreateDishesIngredientsActivity::class.java))
    }
}