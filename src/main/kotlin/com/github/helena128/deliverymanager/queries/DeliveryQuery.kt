package com.github.helena128.deliverymanager.queries

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.service.DeliveryService
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.stereotype.Component

@Component
class DeliveryQuery (val deliveryService: DeliveryService) : Query {

    // A query that is able to either list all deliveries not yet received, or all deliveries that have already been received
    @GraphQLDescription("A query returning either list of all deliveries not yet received, or all deliveries that have been received. The behavior is determined by the input parameter")
    suspend fun getDeliveries(@GraphQLDescription("Boolean parameter to filter out items based on whether they were received or not")
                      received: Boolean): List<Delivery> {
        return deliveryService.getDeliveries(received).collectList().awaitFirst()
    }

}