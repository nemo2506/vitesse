package com.openclassrooms.vitesse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore
import androidx.room.Index

@Entity(
    tableName = "candidate",
    indices = [Index(value = ["email"], unique = true)]
)
data class CandidateDto(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var firstName: String? = null,
    var lastName: String,
    var phone: String? = null,
    var email: String,
    var isFavorite: Boolean,
    var photoUri: String? = null,
) {
    @Ignore
    var note: String? = null
}

