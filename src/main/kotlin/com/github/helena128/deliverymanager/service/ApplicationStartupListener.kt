package com.github.helena128.deliverymanager.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class ApplicationStartupListener(
    private val repository: DeliveryRepository,
    private val mapper: DeliveryMapper,
    private val objectMapper: ObjectMapper,
    @Value("\${delivery-manager.input-data-path}") private val inputFileName: String
) {

    @EventListener(ApplicationStartedEvent::class)
    fun runAfterStarted() {
        println("Started uploading data...")
        val deliveryList = objectMapper.readValue(this::class.java.classLoader.getResource(inputFileName),
            object : TypeReference<List<Delivery>>() {})
            .map { mapper.convertToEntity(it) }
        repository.saveAll(deliveryList)
            .doOnNext { println("Saved ${it.deliveryId}") }
            .blockLast()
        println("Ended uploading data...")
    }
}