package com.example.office.controller

import com.example.office.service.BookingService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService
) {
    @PostMapping
    fun createBooking(@RequestBody request: BookingRequest): Long? {
        return bookingService.reserveDesk(request.deskId, request.employeeId)
    }

    @GetMapping
    fun getBookings() = bookingService.getCurrentBookings()

    @DeleteMapping("/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    fun cancelBooking(@PathVariable id: Long) {
        bookingService.cancelBooking(id)
    }
}

data class BookingRequest(
    val deskId: Long,
    val employeeId: Long
)
