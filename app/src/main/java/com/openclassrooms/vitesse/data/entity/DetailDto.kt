package com.openclassrooms.vitesse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import androidx.room.ForeignKey

@Entity(tableName = "detail",
    foreignKeys = [ ForeignKey(
        entity = CandidateDto::class,
        parentColumns = ["id"],
        childColumns = ["candidateId"]
    )])
data class DetailDto(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var date: LocalDateTime,
    var salaryClaim: Long,
    var note: String,
    var candidateId: Long
)