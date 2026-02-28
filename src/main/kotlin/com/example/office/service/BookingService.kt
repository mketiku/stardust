package com.example.office.service

import com.example.office.entity.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingService(
    private val deskRepository: DeskRepository,
    private val employeeRepository: EmployeeRepository,
    private val bookingRepository: com.example.office.entity.BookingRepository
) {
    /**
     * Refined implementation: 
     * - Returns the generated Booking ID.
     * - Creates a Booking audit record.
     */
    @Transactional
    fun reserveDesk(deskId: Long, employeeId: Long): Long? {
        val desk = deskRepository.findById(deskId).orElseThrow { 
            RuntimeException("Desk not found") 
        }

        val employee = employeeRepository.findById(employeeId).orElseThrow {
            RuntimeException("Employee not found")
        }

        if (desk.isOccupied) {
            throw IllegalStateException("Desk is already occupied")
        }

        if (employee.department != desk.departmentZone) {
            throw com.example.office.exception.DepartmentMismatchException("Employee department does not match desk zone")
        }
        
        desk.isOccupied = true
        deskRepository.save(desk)

        val booking = com.example.office.entity.Booking(desk = desk, employee = employee)
        return bookingRepository.save(booking).id
    }

    @Transactional
    fun cancelBooking(bookingId: Long) {
        val booking = bookingRepository.findById(bookingId).orElseThrow {
            RuntimeException("Booking not found")
        }
        
        val desk = booking.desk
        desk.isOccupied = false
        deskRepository.save(desk)
        
        bookingRepository.delete(booking)
    }

    fun getCurrentBookings(): List<com.example.office.entity.Desk> {
        return deskRepository.findAllByIsOccupiedTrue()
    }
}
