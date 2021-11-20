package com.github.helena128.deliverymanager.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.service.DeliveryService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component

@Component
class DeliveryMutation(private val deliveryService: DeliveryService) : Mutation {

    @GraphQLDescription("Marks delivery received")
    suspend fun markDeliveryReceived(@GraphQLDescription("ID of the delivery to be marked received") id: String): Delivery {
        return deliveryService.updateDeliveryStatus(id, DeliveryStatus.RECEIVED).awaitSingle()
    }

}