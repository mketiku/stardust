package com.example.office.controller

import com.example.office.service.BookingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Office Booking", description = "Endpoints for managing desk reservations")
class BookingController(
    private val bookingService: BookingService
) {
    @PostMapping
    @Operation(summary = "Reserve a desk", description = "Validates department zone and occupancy before creating a booking audit.")
    fun createBooking(@RequestBody request: BookingRequest): Long? {
        return bookingService.reserveDesk(request.deskId, request.employeeId)
    }

    @GetMapping
    @Operation(summary = "List current bookings", description = "Returns all desks that are currently marked as occupied.")
    fun getBookings() = bookingService.getCurrentBookings()

    @DeleteMapping("/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel a booking", description = "Deletes the booking audit and frees up the associated desk.")
    fun cancelBooking(@PathVariable id: Long) {
        bookingService.cancelBooking(id)
    }
}

data class BookingRequest(
    val deskId: Long,
    val employeeId: Long
) {
    init {
        require(deskId > 0) { "deskId must be a positive number" }
        require(employeeId > 0) { "employeeId must be a positive number" }
    }
}
