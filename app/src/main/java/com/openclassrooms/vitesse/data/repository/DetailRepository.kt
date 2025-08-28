package com.openclassrooms.vitesse.data.repository

import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
//import android.util.Log
import com.openclassrooms.vitesse.utils.Log

class DetailRepository(
    private val candidateDao: CandidateDao
) {

    fun getCandidateById(id: Long): Flow<CandidateWithDetailDto?> {
        return candidateDao.getCandidateById(id)
            .catch { e ->
                Log.d(tag="MARC", msg="getCandidateById error: $e")
                emit(null)
            }
    }

    // Del a candidate
    fun deleteCandidate(candidateId: Long): Flow<Int> = flow {
        try {
            val result = candidateDao.deleteCandidate(candidateId)
            emit(result)
        } catch (e: Exception) {
            Log.d(tag="MARC", msg="deleteCandidateError: $e")
            emit(0)
        }
    }

    // Add or Modify a new candidate
    fun updateFavoriteCandidate(id: Long, fav: Boolean): Flow<Int> = flow {
        try {
            val result = candidateDao.updateCandidateFavorite(id, fav)
            emit(result)
        } catch (e: Exception) {
            Log.d(tag="MARC", msg="updateFavoriteCandidateError: $e")
            emit(0)
        }
    }
}