package com.example.al_pc.recipes.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService{
    @POST("registration.php")
    fun registrationUser(@Query("email") email:String,
                         @Query("nickname") nickname:String,
                         @Query("password") password:String):io.reactivex.Observable<RegistrationOrAutentification>

    @POST("autentification.php")
    fun autentificationUser(@Query("email") email:String,
                            @Query("password") password:String):io.reactivex.Observable<RegistrationOrAutentification>

    @POST("select_dishes_and_categories.php")
    fun selectDishesAndCategories(@Query("name") name:String):io.reactivex.Observable<List<DishesName>>

    @POST("select_stages_recipe.php")
    fun selectStagesRecipe(@Query("category") category: String,
                           @Query("name") name:String):io.reactivex.Observable<List<Recipe>>

    @POST("select_dishes_ingredients.php")
    fun selectDishesIngredients(@Query("category") category: String,
                                @Query("name") name:String):io.reactivex.Observable<List<Ingredient>>

    @POST("deleteAccount.php")
    fun deleteAccount(@Query("email") email: String):io.reactivex.Observable<Result>

    @POST("sendInquiry.php")
    fun sendInquiry(@Query("email") email: String):io.reactivex.Observable<Result>


    companion object Factory{
        fun create(): ApiService {
            val gson: Gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("http://zxc101.host/recipe/")
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}