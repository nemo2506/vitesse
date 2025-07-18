package com.openclassrooms.vitesse.data.repository

open class CandidateRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)
class MissingCandidateIdException : CandidateRepositoryException("Candidate ID is null")