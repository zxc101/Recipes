package com.example.al_pc.recipes.data

object SearchRepositoryProvider {
    fun provideSearchRepository():SearchRepository{
        return SearchRepository(ApiService.create())
    }
}