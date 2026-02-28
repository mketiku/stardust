package com.example.stardust.domain.service

import com.example.stardust.domain.exception.FleetMismatchException
import com.example.stardust.domain.model.DockingBay
import com.example.stardust.domain.model.DockingManifest
import com.example.stardust.domain.model.Starship
import com.example.stardust.domain.port.DockingBayRepository
import com.example.stardust.domain.port.DockingManifestRepository
import com.example.stardust.domain.port.StarshipRepository
import io.micrometer.core.instrument.MeterRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.Optional

class DockingServiceTest {
    private val bayRepository = mockk<DockingBayRepository>()
    private val starshipRepository = mockk<StarshipRepository>()
    private val manifestRepository = mockk<DockingManifestRepository>()
    private val meterRegistry = mockk<MeterRegistry>(relaxed = true)
    private val dockingService = DockingService(
        bayRepository,
        starshipRepository,
        manifestRepository,
        meterRegistry
    )

    @Test
    fun `should allow docking when starship fleet matches bay protocol`() {
        val bay = DockingBay(id = 1L, bayCode = "BAY-01", deckLevel = 4, requiredFleetAffiliation = "SCIENCE", isOccupied = false)
        val ship = Starship(id = 1L, registryName = "USS Discovery", fleetAffiliation = "SCIENCE")

        every { bayRepository.findById(1L) } returns Optional.of(bay)
        every { starshipRepository.findById(1L) } returns Optional.of(ship)
        every { bayRepository.save(any()) } returns bay
        every { manifestRepository.save(any()) } returns DockingManifest(id = 100L, dockingBay = bay, starship = ship)

        dockingService.requestDocking(1L, 1L)

        verify { bayRepository.save(match { it.isOccupied }) }
    }

    @Test
    fun `should prevent docking when starship fleet does not match bay protocol`() {
        val bay = DockingBay(id = 1L, bayCode = "BAY-01", deckLevel = 4, requiredFleetAffiliation = "MILITARY", isOccupied = false)
        val ship = Starship(id = 1L, registryName = "USS Enterprise", fleetAffiliation = "SCIENCE")

        every { bayRepository.findById(1L) } returns Optional.of(bay)
        every { starshipRepository.findById(1L) } returns Optional.of(ship)

        assertThrows(FleetMismatchException::class.java) {
            dockingService.requestDocking(1L, 1L)
        }

        verify(exactly = 0) { bayRepository.save(any()) }
    }

    @Test
    fun `should return manifest when valid id is provided`() {
        val bay = DockingBay(id = 1L, bayCode = "BAY-01", deckLevel = 4, requiredFleetAffiliation = "SCIENCE", isOccupied = true)
        val ship = Starship(id = 1L, registryName = "USS Discovery", fleetAffiliation = "SCIENCE")
        val manifest = DockingManifest(id = 100L, dockingBay = bay, starship = ship)

        every { manifestRepository.findById(100L) } returns Optional.of(manifest)

        val result = dockingService.getManifest(100L)

        assert(result.id == 100L)
        assert(result.starship.registryName == "USS Discovery")
    }
}
