package com.example.al_pc.recipes.data

class SearchRepository(val apiService: ApiService){
    fun registrationUser(email:String, nickname:String, password:String):io.reactivex.Observable<RegistrationOrAutentification>{
        return apiService.registrationUser(email, nickname, password)
    }

    fun autentificationUser(email:String, password:String):io.reactivex.Observable<RegistrationOrAutentification>{
        return apiService.autentificationUser(email, password)
    }

    fun selectDishesAndCategories(name:String):io.reactivex.Observable<List<DishesName>>{
        return apiService.selectDishesAndCategories(name)
    }

    fun selectStagesRecipe(category: String, name: String):io.reactivex.Observable<List<Recipe>>{
        return apiService.selectStagesRecipe(category, name)
    }

    fun selectDishesIngredients(category: String, name: String):io.reactivex.Observable<List<Ingredient>>{
        return apiService.selectDishesIngredients(category, name);
    }

    fun deleteAccount(email: String):io.reactivex.Observable<Result>{
        return apiService.deleteAccount(email);
    }

    fun sendInquiry(email: String):io.reactivex.Observable<Result>{
        return apiService.sendInquiry(email)
    }

}