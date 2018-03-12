package com.example.al_pc.recipes.data

import com.google.gson.annotations.SerializedName

data class RegistrationOrAutentification(
        @SerializedName("email") val email: String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("category") val category: String,
        @SerializedName("error") val error: String
)

data class DishesName(
        @SerializedName("category") val category: String,
        @SerializedName("name") val name: String,
        @SerializedName("creator") val creator: String
)

data class Recipe(
        @SerializedName("recipe") val recipe: String
)

data class Ingredient(
        @SerializedName("ingredient") val ingredient: String,
        @SerializedName("count") val count: String,
        @SerializedName("dimension") val dimension: String
)

data class Result(
        @SerializedName("result") val result: String
)