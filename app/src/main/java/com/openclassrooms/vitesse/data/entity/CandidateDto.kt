package com.openclassrooms.vitesse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "candidate"
)
data class CandidateDto(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var firstName: String? = null,
    var lastName: String? = null,
    var isFavorite: Boolean,
    var photoUri: String? = null,
    var note: String? = null
)

