package com.stardust.infrastructure.web

import com.stardust.domain.exception.FleetMismatchException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class StationExceptionHandler {
    @ExceptionHandler(FleetMismatchException::class)
    fun handleFleetMismatch(ex: FleetMismatchException): ResponseEntity<StationErrorResponse> {
        val error = StationErrorResponse(HttpStatus.FORBIDDEN.value(), ex.message)
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(ex: IllegalStateException): ResponseEntity<StationErrorResponse> {
        val error = StationErrorResponse(HttpStatus.CONFLICT.value(), ex.message)
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<StationErrorResponse> {
        val error = StationErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}

data class StationErrorResponse(
    val status: Int,
    val message: String?,
)
