package com.example.office.service

import com.example.office.entity.DeskRepository
import com.example.office.entity.EmployeeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingService(
    private val deskRepository: DeskRepository,
    private val employeeRepository: EmployeeRepository
) {
    /**
     * Buggy implementation: 
     * - Lacks department/floor validation.
     * - Lacks proper error handling.
     * - Only sets isOccupied = true.
     */
    @Transactional
    fun reserveDesk(deskId: Long, employeeId: Long) {
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
    }

    fun getCurrentBookings(): List<com.example.office.entity.Desk> {
        return deskRepository.findAllByIsOccupiedTrue()
    }
}
