package com.example.al_pc.administrationrecipes.data

import android.support.annotation.Dimension
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService{

    @POST("autentification.php")
    fun autentificationUser(@Query("email") email:String,
                            @Query("password") password:String):io.reactivex.Observable<Autentification>

    @POST("insert_ingredient.php")
    fun insertIngredient(@Query("name") name:String):io.reactivex.Observable<Result>

    @POST("update_users_category.php")
    fun updateUsersCategory(@Query("email") email: String):io.reactivex.Observable<Result>

    @POST("delete_inquery.php")
    fun deleteInquery(@Query("email") email: String):io.reactivex.Observable<Result>

    @POST("show_logs.php")
    fun showLogs(@Query("time")time: String,
                 @Query("event")event:String,
                 @Query("userId")userId:String,
                 @Query("userEmail")userEmail:String,
                 @Query("userNickname")userNickname:String,
                 @Query("userCategory")userCategory:String):io.reactivex.Observable<List<Log>>

    @POST("show_inquiry.php")
    fun showInquiry():io.reactivex.Observable<List<Inquiry>>

    @POST("select_dishes_and_categories.php")
    fun selectDishesAndCategories():io.reactivex.Observable<List<DishesName>>

    @POST("select_stages_recipe.php")
    fun selectStagesRecipe(@Query("category") category: String,
                           @Query("name") name:String):io.reactivex.Observable<List<Recipe>>

    @POST("select_dishes_ingredients.php")
    fun selectDishesIngredients(@Query("category") category: String,
                                @Query("name") name:String):io.reactivex.Observable<List<Ingredient>>

    @POST("confirmation_dish.php")
    fun confirmationDish(@Query("category") category: String,
                           @Query("name") name:String):io.reactivex.Observable<Result>

    @POST("delete_dish.php")
    fun deleteDish(@Query("category") category: String,
                                @Query("name") name:String):io.reactivex.Observable<Result>

    companion object Factory{
        fun create(): ApiService {
            val gson: Gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("http://zxc101.host/administrationRecipe/")
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}