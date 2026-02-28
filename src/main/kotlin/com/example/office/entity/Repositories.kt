package com.example.office.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeskRepository : JpaRepository<Desk, Long>

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long>
