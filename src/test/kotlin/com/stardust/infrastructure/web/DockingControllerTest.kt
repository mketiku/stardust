package com.stardust.infrastructure.web

import com.ninjasquad.springmockk.MockkBean
import com.stardust.domain.model.DockingBay
import com.stardust.domain.model.DockingManifest
import com.stardust.domain.model.Starship
import com.stardust.domain.service.DockingService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(DockingController::class)
class DockingControllerTest(
    @Autowired val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var dockingService: DockingService

    @Test
    fun `should return active docking bays`() {
        val bay =
            DockingBay(
                id = 1L,
                bayCode = "BAY-01",
                deckLevel = 4,
                requiredFleetAffiliation = "SCIENCE",
                isOccupied = true,
            )

        every { dockingService.getActiveDocks() } returns listOf(bay)

        mockMvc
            .perform(get("/api/docking/active"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].bayCode").value("BAY-01"))
    }

    @Test
    fun `should return manifest detail for valid id`() {
        val bay =
            DockingBay(
                id = 1L,
                bayCode = "BAY-01",
                deckLevel = 4,
                requiredFleetAffiliation = "SCIENCE",
                isOccupied = true,
            )
        val ship = Starship(id = 1L, registryName = "USS Discovery", fleetAffiliation = "SCIENCE")
        val manifest = DockingManifest(id = 100L, dockingBay = bay, starship = ship)

        every { dockingService.getManifest(100L) } returns manifest

        mockMvc
            .perform(get("/api/docking/manifest/100"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.manifestId").value(100))
            .andExpect(jsonPath("$.starshipRegistry").value("USS Discovery"))
    }

    @Test
    fun `should return docking manifest id on success`() {
        every { dockingService.requestDocking(any(), any()) } returns 101L

        val body = "{\"bayId\": 1, \"starshipId\": 1}"

        mockMvc
            .perform(
                post("/api/docking/request")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body),
            ).andExpect(status().isOk)
    }
}
