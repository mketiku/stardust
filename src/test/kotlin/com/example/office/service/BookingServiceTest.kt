package com.example.office.service

import com.example.office.domain.model.Booking
import com.example.office.domain.model.Desk
import com.example.office.domain.model.Employee
import com.example.office.domain.port.BookingRepository
import com.example.office.domain.port.DeskRepository
import com.example.office.domain.port.EmployeeRepository
import com.example.office.domain.service.BookingService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.*

class BookingServiceTest {

    private val deskRepository = mockk<DeskRepository>()
    private val employeeRepository = mockk<EmployeeRepository>()
    private val bookingRepository = mockk<BookingRepository>()
    private val bookingService = BookingService(deskRepository, employeeRepository, bookingRepository)

    @Test
    fun `should allow booking when employee department matches desk zone`() {
        val desk = Desk(id = 1L, deskCode = "4B-01", floorNumber = 4, departmentZone = "ENGINEERING", isOccupied = false)
        val employee = Employee(id = 1L, name = "Alice", department = "ENGINEERING")

        every { deskRepository.findById(1L) } returns Optional.of(desk)
        every { employeeRepository.findById(1L) } returns Optional.of(employee)
        every { deskRepository.save(any()) } returns desk
        every { bookingRepository.save(any()) } returns Booking(id = 100L, desk = desk, employee = employee)

        bookingService.reserveDesk(1L, 1L)

        verify { deskRepository.save(match { it.isOccupied }) }
    }

    @Test
    fun `should prevent booking when employee department does not match desk zone`() {
        val desk = Desk(id = 1L, deskCode = "3A-01", floorNumber = 3, departmentZone = "MARKETING", isOccupied = false)
        val employee = Employee(id = 1L, name = "Bob", department = "ENGINEERING")

        every { deskRepository.findById(1L) } returns Optional.of(desk)
        every { employeeRepository.findById(1L) } returns Optional.of(employee)

        assertThrows(com.example.office.domain.exception.DepartmentMismatchException::class.java) {
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
    
    @Test
    fun `should return all currently occupied desks`() {
        val occupiedDesk1 = Desk(id = 1L, deskCode = "1A-01", floorNumber = 1, departmentZone = "ENG", isOccupied = true)
        val occupiedDesk2 = Desk(id = 2L, deskCode = "1A-02", floorNumber = 1, departmentZone = "ENG", isOccupied = true)
        
        every { deskRepository.findAllByIsOccupiedTrue() } returns listOf(occupiedDesk1, occupiedDesk2)
        
        val results = bookingService.getCurrentBookings()
        
        assert(results.size == 2)
        assert(results.all { it.isOccupied })
        verify { deskRepository.findAllByIsOccupiedTrue() }
    }

    @Test
    fun `Test 1 - should throw DepartmentMismatchException when Marketing employee tries to book Engineering desk`() {
        val desk = Desk(id = 10L, deskCode = "4B-10", floorNumber = 4, departmentZone = "ENGINEERING", isOccupied = false)
        val employee = Employee(id = 5L, name = "Mark", department = "MARKETING")

        every { deskRepository.findById(10L) } returns Optional.of(desk)
        every { employeeRepository.findById(5L) } returns Optional.of(employee)

        assertThrows(com.example.office.domain.exception.DepartmentMismatchException::class.java) {
            bookingService.reserveDesk(10L, 5L)
        }
    }

    @Test
    fun `Test 2 - should successfully book floor 4 desk for Engineering employee`() {
        val desk = Desk(id = 11L, deskCode = "4B-11", floorNumber = 4, departmentZone = "ENGINEERING", isOccupied = false)
        val employee = Employee(id = 6L, name = "Engie", department = "ENGINEERING")

        every { deskRepository.findById(11L) } returns Optional.of(desk)
        every { employeeRepository.findById(6L) } returns Optional.of(employee)
        every { deskRepository.save(any()) } returns desk
        every { bookingRepository.save(any()) } returns Booking(id = 101L, desk = desk, employee = employee)

        bookingService.reserveDesk(11L, 6L)

        verify { deskRepository.save(match { it.isOccupied && it.deskCode == "4B-11" }) }
    }

    @Test
    fun `should return booking id when successfully reserved`() {
        val desk = Desk(id = 1L, deskCode = "4B-01", floorNumber = 4, departmentZone = "ENGINEERING", isOccupied = false)
        val employee = Employee(id = 1L, name = "Alice", department = "ENGINEERING")
        val booking = Booking(id = 100L, desk = desk, employee = employee)

        every { deskRepository.findById(1L) } returns Optional.of(desk)
        every { employeeRepository.findById(1L) } returns Optional.of(employee)
        every { deskRepository.save(any()) } returns desk
        every { bookingRepository.save(any()) } returns booking

        val bookingId = bookingService.reserveDesk(1L, 1L)

        assert(bookingId == 100L)
        verify { bookingRepository.save(any()) }
    }

    @Test
    fun `should cancel booking and free up desk`() {
        val desk = Desk(id = 1L, deskCode = "4B-01", floorNumber = 4, departmentZone = "ENGINEERING", isOccupied = true)
        val employee = Employee(id = 1L, name = "Alice", department = "ENGINEERING")
        val booking = Booking(id = 100L, desk = desk, employee = employee)

        every { bookingRepository.findById(100L) } returns Optional.of(booking)
        every { deskRepository.save(any()) } returns desk
        every { bookingRepository.delete(booking) } returns Unit

        bookingService.cancelBooking(100L)

        assert(!desk.isOccupied)
        verify { deskRepository.save(match { !it.isOccupied }) }
        verify { bookingRepository.delete(booking) }
    }

    @Test
    fun `should return booking when valid id is provided`() {
        val desk = Desk(id = 1L, deskCode = "4B-01", floorNumber = 4, departmentZone = "ENGINEERING", isOccupied = true)
        val employee = Employee(id = 1L, name = "Alice", department = "ENGINEERING")
        val booking = Booking(id = 100L, desk = desk, employee = employee)

        every { bookingRepository.findById(100L) } returns Optional.of(booking)

        val result = bookingService.getBooking(100L)

        assert(result.id == 100L)
        assert(result.employee.name == "Alice")
    }
}
