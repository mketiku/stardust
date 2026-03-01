package com.stardust.infrastructure.persistence.jpa

import com.stardust.domain.model.DockingBay
import com.stardust.domain.model.DockingManifest
import com.stardust.domain.model.Starship
import com.stardust.domain.port.DockingBayRepository
import com.stardust.domain.port.DockingManifestRepository
import com.stardust.domain.port.StarshipRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaDockingBayRepository :
    JpaRepository<DockingBay, Long>,
    DockingBayRepository

@Repository
interface JpaStarshipRepository :
    JpaRepository<Starship, Long>,
    StarshipRepository

@Repository
interface JpaDockingManifestRepository :
    JpaRepository<DockingManifest, Long>,
    DockingManifestRepository
