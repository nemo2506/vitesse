package com.openclassrooms.vitesse.data.repository

import com.openclassrooms.vitesse.data.network.ManageClient
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val dataService: ManageClient
){
        /**
     * Attempts to log in a user with the given [id] and [password].
     *
     * @param id The user identifier.
     * @param password The user's password.
     * @return [Result] containing [LoginReportModel] on success or an error message on failure.
     */
    suspend fun getGbp(): Result<LoginReportModel> {
        return try {
            val user = User(id, password)
            val result = dataService.fetchAccess(user)
            val model = result.body()?.toDomainModel() ?: throw Exception("Invalid data")
            Result.Success(model)
        } catch (error: Exception) {
            Result.Failure(error.message)
        }
    }
}