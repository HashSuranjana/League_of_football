package com.example.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

interface ClubsDao {

    @Query("select * from clubs") //returns all items in clubs
    suspend fun getAll(): List<Clubs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg clubs: Clubs)

    @Query("SELECT * FROM clubs WHERE strTeam LIKE '%' || :name || '%'") //get all items if strTeam name contains name
    fun filterClubs(name: String): List<Clubs>


    @Query("delete from clubs") //delete all items in clubs
    suspend fun deleteAll()

}