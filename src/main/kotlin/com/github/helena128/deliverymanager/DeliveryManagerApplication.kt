package com.github.helena128.deliverymanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories("com.github.helena128.deliverymanager")
class DeliveryManagerApplication

fun main(args: Array<String>) {
    runApplication<DeliveryManagerApplication>(*args)
}
