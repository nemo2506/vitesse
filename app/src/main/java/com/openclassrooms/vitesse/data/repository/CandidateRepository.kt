package com.openclassrooms.vitesse.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Detail
import com.openclassrooms.vitesse.utils.toDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import android.util.Log
//import com.openclassrooms.vitesse.utils.Log

class CandidateRepository(
    private val candidateDao: CandidateDao
) {
    fun getCandidateByAttr(fav: Int, term: String): Flow<List<Candidate?>> {
        val searchTerm = if (term.isEmpty()) "" else "%$term%"
        return candidateDao.getCandidate(fav, searchTerm)
            .catch { e ->
                Log.d("MARC", "getCandidateById error: $e")
                emit(emptyList())
            }
    }

    // Add or Modify a new candidate
    fun upsertCandidateAll(
        candidateId: Long,
        detailId: Long,
        firstName: String? = null,
        lastName: String? = null,
        phone: String? = null,
        email: String? = null,
        isFavorite: Boolean,
        photoUri: String? = null,
        note: String? = null,
        date: String? = null,
        salaryClaim: Long?
    ): Flow<Long> = flow {
        try {
            val candidate = Candidate(
                id = candidateId,
                firstName = firstName,
                lastName = lastName,
                isFavorite = isFavorite,
                photoUri = photoUri,
                note = note
            )
            val detail = Detail(
                    id = detailId,
                    date = date?.toDate(),
                    salaryClaim = salaryClaim,
                    phone = phone,
                    email = email,
                    candidateId = candidateId
                )
            val candidateWithDetailDto = CandidateWithDetailDto(
                candidate.toDto(),
                detail.toDto()
            )
            val id = candidateDao.upsertCandidateAll(candidateWithDetailDto)
            emit(id)
        } catch (e: SQLiteConstraintException) {
            emit(0L)
        } catch (e: Exception) {
            emit(0L)
        }
    }
}