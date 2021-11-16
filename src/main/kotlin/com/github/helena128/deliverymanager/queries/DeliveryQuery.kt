package com.github.helena128.deliverymanager.queries

import org.springframework.stereotype.Component
import com.expediagroup.graphql.server.operations.Query
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.service.DeliveryService

@Component
class DeliveryQuery (val deliveryService: DeliveryService) : Query {


    fun getDeliveries(/*deliveryStatus: DeliveryStatus*/) = /*{
        println(">> Delivery status passed: " *//*+ deliveryStatus.name*//*)*/
        deliveryService.getDeliveries()
    //}
}