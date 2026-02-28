package com.example.office.entity

import jakarta.persistence.*

@Entity
class Desk(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    val deskCode: String,

    val floorNumber: Int,

    val departmentZone: String,

    var isOccupied: Boolean = false,

    @Version
    val version: Long = 0
)
