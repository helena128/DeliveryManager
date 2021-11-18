package com.github.helena128.deliverymanager.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.service.DeliveryService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component

@Component
class DeliveryMutation(val deliveryService: DeliveryService) : Mutation {

    suspend fun markDeliveryReceived(id: String): Delivery {
        return deliveryService.updateDeliveryStatus(id, DeliveryStatus.RECEIVED).awaitSingle()
    }

}