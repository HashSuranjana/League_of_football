package com.example.application

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Leagues")
data class Leagues(
    @PrimaryKey(autoGenerate = true) var id: Int = 1,
    val strTeam :String  ?,
    val idTeam :String ?,
    val strTeamShort :String  ?,
    val strAlternate :String  ?,
    val intFormedYear :String  ?,
    val strLeague :String  ?,
    val idLeague :String  ?,
    val strStadium :String  ?,
    val strKeywords :String  ?,
    val strStadiumThumb :String  ?,
    val strStadiumLocation :String  ?,
    val intStadiumCapacity :String  ?,
    val strWebsite :String  ?,
    val strTeamJersey :String  ?,
    val strTeamLogo :String  ?,
)





