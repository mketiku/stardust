package com.example.office.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeskRepository : JpaRepository<Desk, Long> {
    fun findAllByIsOccupiedTrue(): List<Desk>
}

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long>

@Repository
interface BookingRepository : JpaRepository<Booking, Long>
