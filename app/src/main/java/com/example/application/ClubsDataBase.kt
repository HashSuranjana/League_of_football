package com.example.application

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Clubs::class], version = 1_15)
abstract class ClubsDataBase : RoomDatabase(){

    abstract fun clubsDao():ClubsDao
}