package com.example.application

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Clubs")
data class Clubs(
    @PrimaryKey(autoGenerate = true) var id: Int = 1,
    val idTeam :String ?,
    val strTeam :String  ?,
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





