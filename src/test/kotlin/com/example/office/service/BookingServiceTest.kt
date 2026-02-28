package com.example.office.service

import com.example.office.entity.Desk
import com.example.office.entity.DeskRepository
import com.example.office.entity.Employee
import com.example.office.entity.EmployeeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.*

class BookingServiceTest {

    private val deskRepository = mockk<DeskRepository>()
    private val employeeRepository = mockk<EmployeeRepository>()
    private val bookingService = BookingService(deskRepository, employeeRepository)

    @Test
    fun `should allow booking when employee department matches desk zone`() {
        val desk = Desk(id = 1L, deskCode = "4B-01", floorNumber = 4, departmentZone = "ENGINEERING", isOccupied = false)
        val employee = Employee(id = 1L, name = "Alice", department = "ENGINEERING")

        every { deskRepository.findById(1L) } returns Optional.of(desk)
        every { employeeRepository.findById(1L) } returns Optional.of(employee)
        every { deskRepository.save(any()) } returns desk

        bookingService.reserveDesk(1L, 1L)

        verify { deskRepository.save(match { it.isOccupied }) }
    }

    @Test
    fun `should prevent booking when employee department does not match desk zone`() {
        val desk = Desk(id = 1L, deskCode = "3A-01", floorNumber = 3, departmentZone = "MARKETING", isOccupied = false)
        val employee = Employee(id = 1L, name = "Bob", department = "ENGINEERING")

        every { deskRepository.findById(1L) } returns Optional.of(desk)
        every { employeeRepository.findById(1L) } returns Optional.of(employee)

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.reserveDesk(1L, 1L)
        }

        verify(exactly = 0) { deskRepository.save(any()) }
    }

    @Test
    fun `should prevent booking when desk is already occupied`() {
        val desk = Desk(id = 1L, deskCode = "4B-01", floorNumber = 4, departmentZone = "ENGINEERING", isOccupied = true)
        val employee = Employee(id = 1L, name = "Alice", department = "ENGINEERING")

        every { deskRepository.findById(1L) } returns Optional.of(desk)
        every { employeeRepository.findById(1L) } returns Optional.of(employee)

        assertThrows(IllegalStateException::class.java) {
            bookingService.reserveDesk(1L, 1L)
        }

        verify(exactly = 0) { deskRepository.save(any()) }
    }
}
