package com.autoclick.tool.di

import android.content.Context
import androidx.room.Room
import com.autoclick.tool.data.database.AutoClickDatabase
import com.autoclick.tool.data.database.ClickPointDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAutoClickDatabase(
        @ApplicationContext context: Context
    ): AutoClickDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AutoClickDatabase::class.java,
            "auto_click_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideClickPointDao(database: AutoClickDatabase): ClickPointDao {
        return database.clickPointDao()
    }
}
