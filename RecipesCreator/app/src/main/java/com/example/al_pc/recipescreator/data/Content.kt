package com.example.al_pc.recipescreator.data

import com.google.gson.annotations.SerializedName
import io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess

data class Log(
        @SerializedName("user") val user: String,
        @SerializedName("time") val time: String,
        @SerializedName("impact") val impact: String,
        @SerializedName("dish") val dish: String
)

data class Autentification(
        @SerializedName("email") val email: String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("category") val category: String,
        @SerializedName("error") val error: String
)

data class Category(
        @SerializedName("category") val category: String
)

data class IngredientName(
        @SerializedName("ingredient") val ingredient: String
)

data class Ingredient(
        @SerializedName("ingredient") val name: String,
        @SerializedName("count") val count: String,
        @SerializedName("dimension") val dimension: String
)

data class DishName(
        @SerializedName("name") val name: String,
        @SerializedName("category") val category: String
)

data class Result(
        @SerializedName("result") val result: String
)

data class Dish(
        @SerializedName("name") val name: String,
        @SerializedName("category") val category: String,
        @SerializedName("ingredients") val ingredients: String,
        @SerializedName("recipes") val recipes: String
)

data class Recipe(
        @SerializedName("recipe") val recipe: String,
        @SerializedName("position") val position: String
)