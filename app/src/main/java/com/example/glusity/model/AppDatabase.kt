package com.example.glusity.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ObesityResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun obesityResultDao(): ObesityResultDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "glusity_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}