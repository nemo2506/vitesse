package com.openclassrooms.vitesse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "detail",
    foreignKeys = [androidx.room.ForeignKey(
        entity = CandidateDto::class,
        parentColumns = ["id"],
        childColumns = ["candidateId"]
    )])
data class DetailDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "date")
    var date: LocalDateTime,

    @ColumnInfo(name = "salaryClaim")
    var salaryClaim: Long,

    @ColumnInfo(name = "note")
    var note: String,

    @ColumnInfo(name = "candidateId")
    var candidateId: Long
)