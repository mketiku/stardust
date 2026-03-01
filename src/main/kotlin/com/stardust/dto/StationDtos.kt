package com.stardust.dto

data class DockingBayResponse(
    val id: Long,
    val bayCode: String,
    val deckLevel: Int,
    val requiredFleetAffiliation: String,
    val isOccupied: Boolean,
)

data class DockingManifestResponse(
    val manifestId: Long,
    val dockingBay: DockingBayResponse,
    val starshipRegistry: String,
    val fleetAffiliation: String,
)
