package com.openclassrooms.vitesse.data.repository

open class ExerciseRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)
class MissingExerciseIdException : ExerciseRepositoryException("Exercise ID is null")