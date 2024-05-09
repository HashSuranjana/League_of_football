package com.example.application

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Leagues")
data class Leagues (
    @PrimaryKey(autoGenerate = true) var id: Int = 1,
    val leagueID :String ?,
    val leagueName :String  ?,
    val leagueSport:String ?,
    val leagueAlt :String  ?
    )


