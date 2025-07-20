package com.openclassrooms.vitesse.domain.usecase

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<out R>(val value: R) : Result<R>()
    data class Failure(val message: String?) : Result<Nothing>()
}