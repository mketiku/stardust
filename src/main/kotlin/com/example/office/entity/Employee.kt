package com.example.office.entity

import jakarta.persistence.*

@Entity
class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    val department: String
)
