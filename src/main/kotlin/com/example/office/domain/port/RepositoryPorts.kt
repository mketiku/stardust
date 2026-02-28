package com.example.office.domain.port

import com.example.office.domain.model.Booking
import com.example.office.domain.model.Desk
import com.example.office.domain.model.Employee
import java.util.*

interface DeskRepository {
    fun findById(id: Long): Optional<Desk>
    fun save(desk: Desk): Desk
    fun findAllByIsOccupiedTrue(): List<Desk>
}

interface EmployeeRepository {
    fun findById(id: Long): Optional<Employee>
    fun save(employee: Employee): Employee
}

interface BookingRepository {
    fun findById(id: Long): Optional<Booking>
    fun save(booking: Booking): Booking
    fun delete(booking: Booking)
}
