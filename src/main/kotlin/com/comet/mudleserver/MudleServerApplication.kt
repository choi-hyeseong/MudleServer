package com.comet.mudleserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.UUID


val uuid : UUID = UUID.fromString("277cfea5-55e4-4af4-8235-56967e2afd96")
@SpringBootApplication
@EnableScheduling
class MudleServerApplication

fun main(args: Array<String>) {
    runApplication<MudleServerApplication>(*args)
}
