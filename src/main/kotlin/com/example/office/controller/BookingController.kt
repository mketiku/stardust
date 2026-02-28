package com.example.office.controller

import com.example.office.service.BookingService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService
) {
    @PostMapping
    fun createBooking(@RequestBody request: BookingRequest) {
        bookingService.reserveDesk(request.deskId, request.employeeId)
    }

    @GetMapping
    fun getBookings() = bookingService.getCurrentBookings()
}

data class BookingRequest(
    val deskId: Long,
    val employeeId: Long
)
