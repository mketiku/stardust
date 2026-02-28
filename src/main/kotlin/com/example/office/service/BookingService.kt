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
        
        // BUGGY LOGIC: Just sets isOccupied to true without any validation
        desk.isOccupied = true
        
        deskRepository.save(desk)
    }
}
