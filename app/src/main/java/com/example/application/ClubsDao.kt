package com.example.application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

interface ClubsDao {

    @Query("select * from clubs")
    suspend fun getAll(): List<Clubs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg clubs: Clubs)

    @Query("select * from clubs where strTeam LIKE :name")
    fun findByLastName(name: String): Clubs

    @Query("delete from clubs")
    suspend fun deleteAll()

}