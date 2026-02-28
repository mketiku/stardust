package com.example.office.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.library.Architectures.layeredArchitecture

@AnalyzeClasses(packages = ["com.example.office"], importOptions = [ImportOption.DoNotIncludeTests::class])
class ArchitectureTest {
    @ArchTest
    val hexagonalArchitectureIsRespected: ArchRule =
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("DomainModel").definedBy("com.example.office.domain.model..")
            .layer("DomainPort").definedBy("com.example.office.domain.port..")
            .layer("DomainService").definedBy("com.example.office.domain.service..")
            .layer("Infrastructure").definedBy("com.example.office.infrastructure..")
            .layer("Web").definedBy("com.example.office.infrastructure.web..")
            .layer("Persistence").definedBy("com.example.office.infrastructure.persistence..")
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
