package com.openclassrooms.vitesse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidate")
data class CandidateDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "firstName")
    var firstName: String,

    @ColumnInfo(name = "lastName")
    var lastName: String,

    @ColumnInfo(name = "phone")
    var phone: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean,

    @ColumnInfo(name = "photoUri")
    var photoUri: String,
)