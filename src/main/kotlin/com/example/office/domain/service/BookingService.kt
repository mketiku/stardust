package com.example.office.domain.service

import com.example.office.domain.model.*
import com.example.office.domain.port.*
import com.example.office.domain.exception.DepartmentMismatchException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingService(
    private val deskRepository: DeskRepository,
    private val employeeRepository: EmployeeRepository,
    private val bookingRepository: BookingRepository
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

        check(!desk.isOccupied) { "Desk ${desk.deskCode} is already occupied" }

        if (employee.department != desk.departmentZone) {
            throw DepartmentMismatchException("Employee department does not match desk zone")
        }
        
        desk.isOccupied = true
        deskRepository.save(desk)

        val booking = Booking(desk = desk, employee = employee)
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

    fun getCurrentBookings(): List<Desk> {
        return deskRepository.findAllByIsOccupiedTrue()
    }
}
