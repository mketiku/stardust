package com.example.stardust.domain.service

import com.example.stardust.domain.exception.FleetMismatchException
import com.example.stardust.domain.model.DockingBay
import com.example.stardust.domain.model.DockingManifest
import com.example.stardust.domain.model.Starship
import com.example.stardust.domain.port.DockingBayRepository
import com.example.stardust.domain.port.DockingManifestRepository
import com.example.stardust.domain.port.StarshipRepository
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DockingService(
    private val bayRepository: DockingBayRepository,
    private val starshipRepository: StarshipRepository,
    private val manifestRepository: DockingManifestRepository,
    private val meterRegistry: MeterRegistry
) {
    /**
     * Records a starship arrival and assigns a docking bay.
     * Enforces fleet-to-bay compatibility protocols.
     */
    @Transactional
    fun requestDocking(bayId: Long, starshipId: Long): Long? {
        val bay = bayRepository.findById(bayId).orElseThrow {
            RuntimeException("Docking bay not found")
        }

        val starship = starshipRepository.findById(starshipId).orElseThrow {
            RuntimeException("Starship registry not found")
        }

        check(!bay.isOccupied) { "Docking Bay ${bay.bayCode} is already occupied" }

        if (starship.fleetAffiliation != bay.requiredFleetAffiliation) {
            throw FleetMismatchException("Starship fleet does not match docking bay protocol")
        }

        bay.isOccupied = true
        bayRepository.save(bay)

        val manifest = DockingManifest(dockingBay = bay, starship = starship)
        val savedManifest = manifestRepository.save(manifest)

        // Record successful docking metric
        meterRegistry.counter("starship_dockings", "fleet", starship.fleetAffiliation).increment()

        return savedManifest.id
    }

    @Transactional
    fun departStation(manifestId: Long) {
        val manifest = manifestRepository.findById(manifestId).orElseThrow {
            RuntimeException("Docking manifest not found")
        }

        val bay = manifest.dockingBay
        bay.isOccupied = false
        bayRepository.save(bay)

        manifestRepository.delete(manifest)
    }

    fun getActiveDocks(): List<DockingBay> {
        return bayRepository.findAllByIsOccupiedTrue()
    }

    fun getManifest(manifestId: Long): DockingManifest {
        return manifestRepository.findById(manifestId).orElseThrow {
            RuntimeException("Manifest not found")
        }
    }
}
