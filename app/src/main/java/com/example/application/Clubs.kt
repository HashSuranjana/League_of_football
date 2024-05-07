package com.example.application

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clubs (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val clubName:String ?,
    val clubID:String ?
)

