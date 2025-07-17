package com.openclassrooms.vitesse.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.Ignore

@Entity(tableName = "candidate")
data class CandidateDto(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var firstName: String,
    var lastName: String,
    var phone: String,
    var email: String,
    var isFavorite: Boolean,
    var photoUri: String,
) {
    @Ignore
    var note: String? = null // ✅ Outside constructor to avoid Room constructor conflict
}
data class CandidateWithDetailDto(
    @Embedded val candidate: CandidateDto,
    @Relation( parentColumn = "id", entityColumn = "candidateId" )
    val details: List<DetailDto> = emptyList()
)
