package com.example.al_pc.administrationrecipes.data

object SearchRepositoryProvider {
    fun provideSearchRepository():SearchRepository{
        return SearchRepository(ApiService.create())
    }
}