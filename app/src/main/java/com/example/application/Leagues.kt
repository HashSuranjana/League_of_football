package com.example.application

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Leagues (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val leagueName:String ?,
    val leagueID:String ?,
    val leagueSport:String ?,
    val leagueDesc:String ?
)


