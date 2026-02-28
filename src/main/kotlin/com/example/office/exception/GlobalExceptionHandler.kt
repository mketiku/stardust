package com.example.office.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.dao.OptimisticLockingFailureException
import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int,
    val message: String?,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DepartmentMismatchException::class)
    fun handleDepartmentMismatch(ex: DepartmentMismatchException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.message)
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(HttpStatus.CONFLICT.value(), ex.message)
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(OptimisticLockingFailureException::class)
    fun handleConcurrency(ex: OptimisticLockingFailureException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(HttpStatus.PRECONDITION_FAILED.value(), "The desk was modified by another user. Please try again.")
        return ResponseEntity(error, HttpStatus.PRECONDITION_FAILED)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleNotFound(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        if (ex.message?.contains("not found", ignoreCase = true) == true) {
            val error = ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.message)
            return ResponseEntity(error, HttpStatus.NOT_FOUND)
        }
        val error = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred")
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
