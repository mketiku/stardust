package com.example.stardust.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures.layeredArchitecture

@AnalyzeClasses(packages = ["com.example.stardust"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchitectureTest {
    @ArchTest
    val hexagonalArchitectureIsRespected: ArchRule =
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("DomainModel").definedBy("com.example.stardust.domain.model..")
            .layer("DomainPort").definedBy("com.example.stardust.domain.port..")
            .layer("DomainService").definedBy("com.example.stardust.domain.service..")
            .layer("Infrastructure").definedBy("com.example.stardust.infrastructure..")
            .layer("Web").definedBy("com.example.stardust.infrastructure.web..")
            .layer("Persistence").definedBy("com.example.stardust.infrastructure.persistence..")
            .whereLayer("DomainModel").mayOnlyBeAccessedByLayers(
                "DomainPort",
                "DomainService",
                "Infrastructure",
                "Web",
                "Persistence",
            )
            .whereLayer("DomainPort").mayOnlyBeAccessedByLayers("DomainService", "Infrastructure", "Persistence")
            .whereLayer("DomainService").mayOnlyBeAccessedByLayers("Web")
            .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
}
