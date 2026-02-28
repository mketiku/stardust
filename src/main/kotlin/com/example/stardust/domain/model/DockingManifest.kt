package com.example.stardust.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class DockingManifest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bay_id", nullable = false)
    val dockingBay: DockingBay,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starship_id", nullable = false)
    val starship: Starship,
    val arrivalTime: LocalDateTime = LocalDateTime.now(),
)
