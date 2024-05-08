package com.example.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

interface LeaguesDao {

    @Query("select * from leagues")
    suspend fun getAll(): List<Leagues>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg clubs: Leagues)

    @Query("select * from leagues where strTeam LIKE :name")
    fun findByLastName(name: String): Leagues

    @Query("delete from leagues")
    suspend fun deleteAll()

}