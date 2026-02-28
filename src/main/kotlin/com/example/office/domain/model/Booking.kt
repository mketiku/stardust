package com.example.office.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desk_id", nullable = false)
    val desk: Desk,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    val employee: Employee,

    val bookingTime: LocalDateTime = LocalDateTime.now()
)
