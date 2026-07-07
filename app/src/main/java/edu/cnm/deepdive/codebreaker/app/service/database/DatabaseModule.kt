package edu.cnm.deepdive.codebreaker.app.service.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.cnm.deepdive.codebreaker.app.model.dao.CompleteGameDao
import edu.cnm.deepdive.codebreaker.app.model.dao.IncompleteGameDao
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideIncompleteGameDao(database: CodebreakerDatabase): IncompleteGameDao =
        database.getIncompleteGameDao()

    @Provides
    @Singleton
    fun provideCompleteGameDao(database: CodebreakerDatabase): CompleteGameDao =
        database.getCompleteGameDao()

    @Provides
    @Singleton
    fun provideCodebreakerDatabase(@ApplicationContext context: Context): CodebreakerDatabase {
        return Room.databaseBuilder(
            context,
            CodebreakerDatabase::class.java,
            CodebreakerDatabase.name
        )
            .build()
    }

}