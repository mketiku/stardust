package com.stardust

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StardustStationApplication

fun main(args: Array<String>) {
    runApplication<StardustStationApplication>(*args)
}
