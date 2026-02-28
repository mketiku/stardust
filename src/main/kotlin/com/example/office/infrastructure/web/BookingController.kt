package com.example.office.infrastructure.web

import com.example.office.domain.service.BookingService
import com.example.office.dto.BookingResponse
import com.example.office.dto.DeskResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Office Booking", description = "Endpoints for managing desk reservations")
class BookingController(
    private val bookingService: BookingService,
) {
    @PostMapping
    @Operation(summary = "Reserve a desk", description = "Validates department zone and occupancy before creating a booking audit.")
    fun createBooking(
        @RequestBody request: BookingRequest,
    ): Long? {
        return bookingService.reserveDesk(request.deskId, request.employeeId)
    }

    @GetMapping
    @Operation(summary = "List current bookings", description = "Returns all desks that are currently marked as occupied.")
    fun getBookings(): List<DeskResponse> {
        return bookingService.getCurrentBookings().map { markAsDeskResponse(it) }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific booking", description = "Retrieves details about an existing reservation.")
    fun getBooking(
        @PathVariable id: Long,
    ): BookingResponse {
        val booking = bookingService.getBooking(id)
        return BookingResponse(
            bookingId = booking.id!!,
            desk = markAsDeskResponse(booking.desk),
            employeeName = booking.employee.name,
            employeeDepartment = booking.employee.department,
        )
    }

    @DeleteMapping("/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel a booking", description = "Deletes the booking audit and frees up the associated desk.")
    fun cancelBooking(
        @PathVariable id: Long,
    ) {
        bookingService.cancelBooking(id)
    }

    private fun markAsDeskResponse(desk: com.example.office.domain.model.Desk) =
        DeskResponse(
            id = desk.id!!,
            deskCode = desk.deskCode,
            floorNumber = desk.floorNumber,
            departmentZone = desk.departmentZone,
            isOccupied = desk.isOccupied,
        )
}

data class BookingRequest(
    val deskId: Long,
    val employeeId: Long,
) {
    init {
        require(deskId > 0) { "deskId must be a positive number" }
        require(employeeId > 0) { "employeeId must be a positive number" }
    }
}
