package com.example.application

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClubsDao {

    @Query("select * from clubs")
    suspend fun getAll(): List<Clubs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: Clubs)

    // insert one club without replacing an identical one - duplicates allowed
    @Insert
    suspend fun insertUser(club: Clubs)

    @Delete
    suspend fun deleteUser(club: Clubs)

    @Query("select * from clubs where strTeam LIKE :name")
    fun findByLastName(name: String): Clubs

    @Query("delete from clubs")
    suspend fun deleteAll()

}
