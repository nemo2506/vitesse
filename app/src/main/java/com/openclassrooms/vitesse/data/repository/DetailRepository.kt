package com.openclassrooms.vitesse.data.repository

import android.util.Log
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.DetailDao
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DetailRepository(
    private val detailDao: DetailDao,
    private val candidateDao: CandidateDao
) {
    // Get Candidate By Id
    fun getCandidateById(query: SupportSQLiteQuery): Flow<CandidateWithDetailDto> {
        return candidateDao.getCandidateById(query)
    }

    // Del a candidate
    fun deleteCandidate(candidateId: Long): Flow<Boolean> = flow {
        try {
            val result = candidateDao.deleteCandidate(candidateId)
            emit(result)
        } catch (e: Exception) {
            Log.d("MARC", "deleteCandidate: $e")
            emit(false)
        }
    }

    // Add or Modify a new candidate
    fun updateFavoriteCandidate(id: Long, fav: Boolean): Flow<Boolean> = flow {
        try {
            val result = candidateDao.updateCandidateFavorite(id, fav)
            emit(result)
        } catch (e: Exception) {
            Log.d("MARC", "updateFavoriteCandidate: $e")
            emit(false)
        }
    }
}