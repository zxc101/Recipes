package com.example.al_pc.recipescreator.data

import android.provider.ContactsContract

class SearchRepository(val apiService: ApiService){
    fun autentificationUser(email:String, password:String):io.reactivex.Observable<Autentification>{
        return apiService.autentificationUser(email, password)
    }

    fun selectCategories(category: String):io.reactivex.Observable<List<Category>>{
        return apiService.selectCategories(category)
    }

    fun selectIngredients(name: String):io.reactivex.Observable<List<IngredientName>>{
        return apiService.selectIngredients(name)
    }

    fun selectDishNameAndCategory(nickname: String):io.reactivex.Observable<List<DishName>>{
        return apiService.select_dish_name_and_category(nickname)
    }

    fun is_have_dish_name(name: String):io.reactivex.Observable<Result>{
        return apiService.is_have_dish_name(name)
    }

    fun select_dishes_ingredients(category: String, name: String):io.reactivex.Observable<List<Ingredient>>{
        return apiService.select_dishes_ingredients(category, name)
    }

    fun select_stages_recipe(category: String, name: String):io.reactivex.Observable<List<Recipe>>{
        return apiService.select_stages_recipe(category, name)
    }

    fun create_dish(category: String,
                    name: String,
                    recipes: String,
                    ingredients: String,
                    creator: String):io.reactivex.Observable<Result>{
        return apiService.create_dish(category, name, recipes, ingredients, creator)
    }

    fun update_dish(oldCategory: String, newCategory: String,
                    oldName: String, newName: String,
                    oldIngredients: String,
                    newIngredients: String,
                    recipes:String, creator: String):io.reactivex.Observable<Result>{
        return apiService.update_dish(oldCategory, newCategory,
                                      oldName, newName,
                                      oldIngredients, newIngredients,
                                      recipes, creator)
    }

    fun delete_dish(category: String, name: String):io.reactivex.Observable<Result>{
        return apiService.delete_dish(category, name)
    }
}