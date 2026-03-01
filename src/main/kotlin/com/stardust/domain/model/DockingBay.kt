package com.stardust.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Version

@Entity
class DockingBay(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true, nullable = false)
    val bayCode: String,
    val deckLevel: Int,
    val requiredFleetAffiliation: String,
    var isOccupied: Boolean = false,
    @Version
    val version: Long = 0,
)
