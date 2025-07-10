package com.autoclick.tool.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.autoclick.tool.data.model.ClickPoint

@Database(
    entities = [ClickPoint::class],
    version = 1,
    exportSchema = false
)
abstract class AutoClickDatabase : RoomDatabase() {
    
    abstract fun clickPointDao(): ClickPointDao
    
    companion object {
        private const val DATABASE_NAME = "auto_click_database"
        
        @Volatile
        private var INSTANCE: AutoClickDatabase? = null
        
        fun getDatabase(context: Context): AutoClickDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AutoClickDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example migration - add new column
                // database.execSQL("ALTER TABLE click_points ADD COLUMN new_column TEXT")
            }
        }
    }
}
