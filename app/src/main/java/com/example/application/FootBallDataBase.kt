package com.example.application

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Leagues::class], version = 1)
abstract class FootBallDataBase : RoomDatabase() {
    abstract fun leaguesDao(): LeaguesDao
}