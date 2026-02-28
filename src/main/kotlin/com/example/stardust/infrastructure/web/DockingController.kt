package com.example.stardust.infrastructure.web

import com.example.stardust.domain.service.DockingService
import com.example.stardust.dto.DockingBayResponse
import com.example.stardust.dto.DockingManifestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/docking")
@Tag(name = "Stardust Station Docking", description = "Endpoints for managing starship docking protocols")
class DockingController(
    private val dockingService: DockingService
) {
    @PostMapping("/request")
    @Operation(summary = "Request docking", description = "Validates fleet affiliation and bay occupancy before authorizing docking.")
    fun requestDocking(@RequestBody request: DockingRequest): Long? {
        return dockingService.requestDocking(request.bayId, request.starshipId)
    }

    @GetMapping("/active")
    @Operation(summary = "List active dockings", description = "Returns all docking bays that are currently occupied.")
    fun getActiveDocks(): List<DockingBayResponse> {
        return dockingService.getActiveDocks().map { mapToBayResponse(it) }
    }

    @GetMapping("/manifest/{id}")
    @Operation(summary = "Get docking manifest", description = "Retrieves details about an active starship docking.")
    fun getManifest(@PathVariable id: Long): DockingManifestResponse {
        val manifest = dockingService.getManifest(id)
        return DockingManifestResponse(
            manifestId = manifest.id!!,
            dockingBay = mapToBayResponse(manifest.dockingBay),
            starshipRegistry = manifest.starship.registryName,
            fleetAffiliation = manifest.starship.fleetAffiliation
        )
    }

    @DeleteMapping("/depart/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Authorize departure", description = "Clears the docking bay and archive the manifest.")
    fun authorizeDeparture(@PathVariable id: Long) {
        dockingService.departStation(id)
    }

    private fun mapToBayResponse(bay: com.example.stardust.domain.model.DockingBay) = DockingBayResponse(
        id = bay.id!!,
        bayCode = bay.bayCode,
        deckLevel = bay.deckLevel,
        requiredFleetAffiliation = bay.requiredFleetAffiliation,
        isOccupied = bay.isOccupied
    )
}

data class DockingRequest(
    val bayId: Long,
    val starshipId: Long
) {
    init {
        require(bayId > 0) { "bayId must be a positive number" }
        require(starshipId > 0) { "starshipId must be a positive number" }
    }
}
