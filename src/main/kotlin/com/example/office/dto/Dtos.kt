package com.example.office.dto

data class DeskResponse(
    val id: Long,
    val deskCode: String,
    val floorNumber: Int,
    val departmentZone: String,
    val isOccupied: Boolean
)

data class BookingResponse(
    val bookingId: Long,
    val desk: DeskResponse,
    val employeeName: String,
    val employeeDepartment: String
)
