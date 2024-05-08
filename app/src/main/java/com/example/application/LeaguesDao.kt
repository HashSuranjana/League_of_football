package com.example.application

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

interface LeaguesDao {

    @Query("select * from leagues")
    suspend fun getAll(): List<Leagues>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: Leagues)

    // insert one club without replacing an identical one - duplicates allowed
    @Insert
    suspend fun insertUser(club: Leagues)

    @Delete
    suspend fun deleteUser(club: Leagues)

    @Query("select * from leagues where strTeam LIKE :name")
    fun findByLastName(name: String): Leagues

    @Query("delete from leagues")
    suspend fun deleteAll()
}