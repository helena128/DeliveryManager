package com.github.helena128.deliverymanager.queries

import com.expediagroup.graphql.server.operations.Query
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.service.DeliveryService
import org.springframework.stereotype.Component

@Component
class DeliveryQuery (val deliveryService: DeliveryService) : Query {

    // A query that is able to either list all deliveries not yet received, or all deliveries that have already been received
    fun getDeliveries(received: Boolean): List<Delivery> = deliveryService.getDeliveries(received)

}