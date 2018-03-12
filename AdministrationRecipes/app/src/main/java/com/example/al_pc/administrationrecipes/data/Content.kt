package com.example.al_pc.administrationrecipes.data

import com.google.gson.annotations.SerializedName

data class Autentification(
        @SerializedName("email") val email:String,
        @SerializedName("nickname") val nickname:String,
        @SerializedName("category") val category:String,
        @SerializedName("error") val error:String
)

data class Result(
        @SerializedName("result") val result:String
)

data class Log(
        @SerializedName("time") val time:String,
        @SerializedName("event") val event:String,
        @SerializedName("userId") val userId:String,
        @SerializedName("userEmail") val userEmail:String,
        @SerializedName("userNickname") val userNickname:String,
        @SerializedName("userCategory") val userCategory:String
)

data class Inquiry(
        @SerializedName("email") val email:String
)

data class DishesName(
        @SerializedName("category") val category: String,
        @SerializedName("name") val name: String,
        @SerializedName("creator") val creator: String
)

data class Ingredient(
        @SerializedName("ingredient") val ingredient: String,
        @SerializedName("count") val count: String,
        @SerializedName("dimension") val dimension: String
)

data class Recipe(
        @SerializedName("recipe") val recipe: String
)