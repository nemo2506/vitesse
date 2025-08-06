package com.openclassrooms.vitesse

import org.junit.Assert.*
import org.junit.Test
import com.openclassrooms.vitesse.domain.usecase.Result

class ResultTest {

    @Test
    fun `Result return value to Success`() {
        // WHEN
        val successValue = "OpenClassrooms"
        val successResult: Result<String> = Result.Success(successValue)
        // THEN
        if (successResult is Result.Success) {
            assertEquals(successValue, successResult.value)
        } else {
            fail("Result is not Success")
        }
    }

    @Test
    fun `Result return value to Failure`() {
        // WHEN
        val failureMessage = "Erreur réseau"
        val failureResult: Result<String> = Result.Failure(failureMessage)
        // THEN
        if (failureResult is Result.Failure) {
            assertEquals(failureMessage, failureResult.message)
        } else {
            fail("Result is not Failure")
        }
    }

    @Test
    fun `Result return value to Loading`() {
        // WHEN
        val loadingResult: Result<String> = Result.Loading
        // THEN
        assertTrue(loadingResult is Result.Loading)
    }
}