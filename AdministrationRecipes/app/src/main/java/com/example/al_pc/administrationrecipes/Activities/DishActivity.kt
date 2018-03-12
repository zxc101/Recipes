package com.example.al_pc.administrationrecipes.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.al_pc.administrationrecipes.Adapters.IngredientsAdapter
import com.example.al_pc.administrationrecipes.Adapters.RecipesAdapter
import com.example.al_pc.administrationrecipes.R
import com.example.al_pc.administrationrecipes.data.Ingredient
import com.example.al_pc.administrationrecipes.data.Recipe
import com.example.al_pc.administrationrecipes.data.SearchRepository
import com.example.al_pc.administrationrecipes.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DishActivity : AppCompatActivity()  {

    @BindView(R.id.category_dish) lateinit var category: AppCompatTextView
    @BindView(R.id.name_dish) lateinit var name: AppCompatTextView
    @BindView(R.id.creator_dish) lateinit var creator: AppCompatTextView
    @BindView(R.id.recipes) lateinit var recipes: RecyclerView
    @BindView(R.id.ingredients) lateinit var ingredients: RecyclerView

    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    val recipesList: MutableList<Recipe> = mutableListOf()
    val ingredientsList: MutableList<Ingredient> = mutableListOf()

    val recipesAdapter: RecipesAdapter = RecipesAdapter(recipesList)
    val ingredientsAdapter: IngredientsAdapter = IngredientsAdapter(ingredientsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish)
        ButterKnife.bind(this)

        recipes.adapter = recipesAdapter
        ingredients.adapter = ingredientsAdapter

        val intent: Intent = getIntent()
        category.text = intent.getStringExtra("category")
        name.text = intent.getStringExtra("name")
        creator.text = intent.getStringExtra("creator")

        val recipesLLM = LinearLayoutManager(this)
        recipesLLM.orientation = LinearLayoutManager.VERTICAL
        recipes.layoutManager = recipesLLM
        compositeDisposable.add(
                repository.selectStagesRecipe(category.text.toString(), name.text.toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({result ->
                            if(!result[0].recipe.equals("")) {
                                recipesList.addAll(result)
                                recipesAdapter.notifyData(recipesList)
                            }
                        })
        )

        val ingredientsLLM = LinearLayoutManager(this)
        ingredientsLLM.orientation = LinearLayoutManager.VERTICAL
        ingredients.layoutManager = ingredientsLLM
        compositeDisposable.add(
                repository.selectDishesIngredients(category.text.toString(), name.text.toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({result ->
                            if(!result[0].ingredient.equals("")) {
                                ingredientsList.addAll(result)
                                ingredientsAdapter.notifyData(ingredientsList)
                            }
                        })
        )

    }
}