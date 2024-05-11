package com.example.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LeaguesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg leagues: Leagues)

    @Query("delete from leagues") //delete all items
    suspend fun deleteAll()
}