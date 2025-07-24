package com.openclassrooms.vitesse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "candidate",
    indices = [Index(value = ["lastName"], unique = true)]
)
data class CandidateDto(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var firstName: String? = null,
    var lastName: String? = null,
    var isFavorite: Boolean,
    var photoUri: String? = null,
    var note: String? = null
)

