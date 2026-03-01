package com.stardust.domain.port

import com.stardust.domain.model.DockingBay
import com.stardust.domain.model.DockingManifest
import com.stardust.domain.model.Starship
import java.util.Optional

interface DockingBayRepository {
    fun findById(id: Long): Optional<DockingBay>

    fun save(dockingBay: DockingBay): DockingBay

    fun findAllByIsOccupiedTrue(): List<DockingBay>
}

interface StarshipRepository {
    fun findById(id: Long): Optional<Starship>

    fun save(starship: Starship): Starship
}

interface DockingManifestRepository {
    fun findById(id: Long): Optional<DockingManifest>

    fun save(manifest: DockingManifest): DockingManifest

    fun delete(manifest: DockingManifest)
}
