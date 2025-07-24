package com.openclassrooms.vitesse.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Detail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CandidateRepository(
    private val candidateDao: CandidateDao
) {
    fun getCandidateByAttr(fav: Int, term: String): Flow<List<Candidate>> {
        val searchTerm = if (term.isEmpty()) "" else "%$term%"
        return candidateDao.getCandidate(fav, searchTerm)
    }

    // Add or Modify a new candidate
    fun upsertCandidate(candidate: Candidate, detail: Detail): Flow<Long> = flow {
        try {
            val candidateWithDetailDto = CandidateWithDetailDto(
                candidate.toDto(),
                detail.toDto()
            )
            val validate = candidateDao.upsertCandidateWithDetail(candidateWithDetailDto)
            emit(validate)
        } catch (e: SQLiteConstraintException) {
            emit(0L)
        } catch (e: Exception) {
            emit(0L)
        }
    }
}