package com.example.office.controller

import com.example.office.entity.Desk
import com.example.office.service.BookingService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(BookingController::class)
class BookingControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var bookingService: BookingService

    @Test
    fun `should return all current bookings`() {
        val occupiedDesk = Desk(id = 1L, deskCode = "1A-01", floorNumber = 1, departmentZone = "ENG", isOccupied = true)
        
        every { bookingService.getCurrentBookings() } returns listOf(occupiedDesk)

        mockMvc.perform(get("/api/bookings"))
            .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].deskCode").value("1A-01"))
            .andExpect(jsonPath("$[0].isOccupied").value(true))
    }
}
