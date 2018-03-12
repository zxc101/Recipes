package com.example.al_pc.administrationrecipes.data

class SearchRepository(val apiService: ApiService){
    fun autentificationUser(email:String, password:String):io.reactivex.Observable<Autentification>{
        return apiService.autentificationUser(email, password)
    }
    fun insertIngredient(name:String):io.reactivex.Observable<Result>{
        return apiService.insertIngredient(name)
    }

    fun updateUsersCategory(email: String):io.reactivex.Observable<Result>{
        return apiService.updateUsersCategory(email)
    }

    fun deleteInquery(email: String):io.reactivex.Observable<Result>{
        return apiService.deleteInquery(email);
    }

    fun showLogs(time: String,
                 event:String,
                 userId:String,
                 userEmail:String,
                 userNickname:String,
                 userCategory:String):io.reactivex.Observable<List<Log>>{
        return apiService.showLogs(time, event, userId, userEmail, userNickname, userCategory)
    }

    fun showInquiry():io.reactivex.Observable<List<Inquiry>>{
        return apiService.showInquiry()
    }

    fun selectDishesAndCategories():io.reactivex.Observable<List<DishesName>>{
        return  apiService.selectDishesAndCategories()
    }

    fun selectStagesRecipe(category: String, name: String):io.reactivex.Observable<List<Recipe>>{
        return apiService.selectStagesRecipe(category, name)
    }

    fun selectDishesIngredients(category: String, name: String):io.reactivex.Observable<List<Ingredient>>{
        return apiService.selectDishesIngredients(category, name);
    }

    fun confirmationDish(category: String, name: String):io.reactivex.Observable<Result>{
        return apiService.confirmationDish(category, name)
    }

    fun deleteDish(category: String, name: String):io.reactivex.Observable<Result>{
        return apiService.deleteDish(category, name);
    }
}