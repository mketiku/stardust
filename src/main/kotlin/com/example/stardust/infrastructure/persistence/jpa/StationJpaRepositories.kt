package com.example.stardust.infrastructure.persistence.jpa

import com.example.stardust.domain.model.DockingBay
import com.example.stardust.domain.model.DockingManifest
import com.example.stardust.domain.model.Starship
import com.example.stardust.domain.port.DockingBayRepository
import com.example.stardust.domain.port.DockingManifestRepository
import com.example.stardust.domain.port.StarshipRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaDockingBayRepository : JpaRepository<DockingBay, Long>, DockingBayRepository

@Repository
interface JpaStarshipRepository : JpaRepository<Starship, Long>, StarshipRepository

@Repository
interface JpaDockingManifestRepository : JpaRepository<DockingManifest, Long>, DockingManifestRepository
