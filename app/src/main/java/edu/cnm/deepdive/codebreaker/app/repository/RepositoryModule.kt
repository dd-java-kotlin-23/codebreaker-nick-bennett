package edu.cnm.deepdive.codebreaker.app.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindPreferenceRepository(impl: PreferencesRepositoryImpl): PreferencesRepository

    @Binds
    fun bindGameRepository(impl: GameRepositoryImpl): GameRepository

}