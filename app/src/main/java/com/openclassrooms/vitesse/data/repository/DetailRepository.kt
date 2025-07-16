package com.openclassrooms.vitesse.data.repository

import com.openclassrooms.vitesse.data.dao.DetailDao
import com.openclassrooms.vitesse.domain.model.Detail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DetailRepository(
    private val detailDao: DetailDao
) {
    // Get all Detail
    fun getDetailById(id: Long): Flow<Detail?> = flow {
        detailDao.getDetailById(id)
            .map { dto -> dto?.let { Detail.fromDto(it) } }
            .catch {
                emit(null)
            }
    }

    // Add or Modify a new detail
    fun updateDetail(detail: Detail): Flow<Result<Unit>> = flow {
        detailDao.updateDetail(detail.toDto())
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(ExerciseRepositoryException("Failed to add/modify detail", e)))
    }

    // Del a detail
    fun deleteDetail(detail: Detail): Flow<Result<Unit>> = flow {
        val id = detail.id ?: throw MissingExerciseIdException()
        detailDao.deleteDetail(id)
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(ExerciseRepositoryException("Failed to del exercise", e)))
    }
}