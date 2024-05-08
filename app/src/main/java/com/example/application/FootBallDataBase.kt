package com.example.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Leagues::class], version = 1)
abstract class FootBallDataBase : RoomDatabase() {
    abstract fun leaguesDao(): LeaguesDao

    companion object {
        @Volatile
        private var Instance : FootBallDataBase ? = null

        fun getDatabase(context: Context) :FootBallDataBase {
            val tempInstance = Instance

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FootBallDataBase::class.java,
                    "Leagues DataBase"
                ).build()
                Instance= instance
                return instance
            }
        }
    }
}
