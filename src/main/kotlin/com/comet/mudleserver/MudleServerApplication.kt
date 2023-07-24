package com.comet.mudleserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MudleServerApplication

fun main(args: Array<String>) {
    runApplication<MudleServerApplication>(*args)
}
