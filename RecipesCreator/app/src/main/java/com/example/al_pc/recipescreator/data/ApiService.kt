package com.example.al_pc.recipescreator.data

import android.provider.ContactsContract
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService{

    @POST("autentification.php")
    fun autentificationUser(@Query("email") email: String,
                            @Query("password") password: String):io.reactivex.Observable<Autentification>

    @POST("select_categories.php")
    fun selectCategories(@Query("category") category: String):io.reactivex.Observable<List<Category>>

    @POST("select_ingredients.php")
    fun selectIngredients(@Query("name") name: String):io.reactivex.Observable<List<IngredientName>>

    @POST("select_dish_name_and_category.php")
    fun select_dish_name_and_category(@Query("nickname") nickname: String):io.reactivex.Observable<List<DishName>>

    @POST("is_have_dish_name.php")
    fun is_have_dish_name(@Query("name") name: String):io.reactivex.Observable<Result>

    @POST("select_dishes_ingredients.php")
    fun select_dishes_ingredients(@Query("category") category: String,
                                  @Query("name") name: String):io.reactivex.Observable<List<Ingredient>>

    @POST("select_stages_recipe.php")
    fun select_stages_recipe(@Query("category") category: String,
                             @Query("name") name: String):io.reactivex.Observable<List<Recipe>>

    @POST("create_dish.php")
    fun create_dish(@Query("category") category: String,
                    @Query("name") name: String,
                    @Query("ingredients") ingredients: String,
                    @Query("recipes") recipes:String,
                    @Query("creator") creator: String):io.reactivex.Observable<Result>

    @POST("update_dish.php")
    fun update_dish(@Query("oldCategory") oldCategory: String,
                    @Query("newCategory") newCategory: String,
                    @Query("oldName") oldName: String,
                    @Query("newName") newName: String,
                    @Query("oldIngredients") oldIngredients: String,
                    @Query("newIngredients") newIngredients: String,
                    @Query("recipes") recipes:String,
                    @Query("creator") creator: String):io.reactivex.Observable<Result>

    @POST("delete_dish.php")
    fun delete_dish(@Query("category") category: String,
                    @Query("name") name: String):io.reactivex.Observable<Result>

    companion object Factory{
        fun create(): ApiService {
            val gson: Gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("http://zxc101.host/recipecreator/")
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}