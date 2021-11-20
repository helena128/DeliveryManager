package com.github.helena128.deliverymanager.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import com.github.helena128.deliverymanager.util.LoggingContants
import com.github.helena128.deliverymanager.util.loggerFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.File

@Component
@Profile("!test")
class ApplicationStartupListener(
    private val repository: DeliveryRepository,
    private val mapper: DeliveryMapper,
    private val objectMapper: ObjectMapper,
    @Value("\${delivery-manager.input-data-path}") private val inputFileName: String
) {

    private val LOG = loggerFor(javaClass)

    @EventListener(ApplicationStartedEvent::class)
    fun runAfterStarted() {
        LOG.info("MSG={}", LoggingContants.INIT_DB_STARTED)
        val deliveryList = objectMapper.readValue(
            File(inputFileName),
            object : TypeReference<List<Delivery>>() {})
            .map { mapper.convertToEntity(it) }
        repository.saveAll(deliveryList)
            .doOnNext { LOG.debug("MSG={}, deliveryId=${it.deliveryId}") }
            .blockLast()
        LOG.info("MSG={}", LoggingContants.INIT_DB_FINISHED)
    }
}