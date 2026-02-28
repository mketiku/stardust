package com.example.office.infrastructure.persistence.jpa

import com.example.office.domain.model.Booking
import com.example.office.domain.model.Desk
import com.example.office.domain.model.Employee
import com.example.office.domain.port.DeskRepository
import com.example.office.domain.port.EmployeeRepository
import com.example.office.domain.port.BookingRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaDeskRepository : JpaRepository<Desk, Long>, DeskRepository

@Repository
interface JpaEmployeeRepository : JpaRepository<Employee, Long>, EmployeeRepository

@Repository
interface JpaBookingRepository : JpaRepository<Booking, Long>, BookingRepository
