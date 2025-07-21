package com.openclassrooms.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.openclassrooms.vitesse.data.entity.DetailDto
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailDao {

    @Query("SELECT * FROM detail WHERE candidateId = :id")
    fun getDetailById(id: Long): Flow<DetailDto?>

    @Query("DELETE FROM detail WHERE id = :id")
    suspend fun deleteDetail(id: Long)
}