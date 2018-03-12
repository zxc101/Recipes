package com.example.al_pc.recipescreator.data

object SearchRepositoryProvider {
    fun provideSearchRepository():SearchRepository{
        return SearchRepository(ApiService.create())
    }
}