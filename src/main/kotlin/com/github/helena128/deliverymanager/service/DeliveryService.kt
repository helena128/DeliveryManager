package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DeliveryService {
    fun getDeliveries(received: Boolean): Flux<Delivery>
    fun updateDeliveryStatus(deliveryId: String, newStatus: DeliveryStatus): Mono<Delivery>
}