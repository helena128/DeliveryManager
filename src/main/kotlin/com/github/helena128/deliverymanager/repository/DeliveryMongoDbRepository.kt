package com.github.helena128.deliverymanager.repository

import com.github.helena128.deliverymanager.entity.DeliveryEntity
import com.github.helena128.deliverymanager.model.DeliveryStatus
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DeliveryMongoDbRepository : ReactiveCrudRepository<DeliveryEntity, String> {

    fun findAllByDeliveryStatus(status: DeliveryStatus): Flux<DeliveryEntity>

    fun findAllByDeliveryStatusNot(status: DeliveryStatus): Flux<DeliveryEntity>

    fun findByDeliveryId(deliveryId: String): Mono<DeliveryEntity>
}