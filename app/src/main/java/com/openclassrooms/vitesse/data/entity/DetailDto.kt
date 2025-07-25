package com.openclassrooms.vitesse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "detail",
    foreignKeys = [
        ForeignKey(
            entity = CandidateDto::class,
            parentColumns = ["id"],
            childColumns = ["candidateId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["candidateId"])]
)
data class DetailDto(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var date: LocalDateTime? = null,
    var salaryClaim: Long? = null,
    var phone: String? = null,
    var email: String? = null,
    var candidateId: Long
)