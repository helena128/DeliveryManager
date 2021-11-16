package com.github.helena128.deliverymanager.repository

import com.expediagroup.graphql.generator.scalars.ID
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import org.springframework.stereotype.Repository

@Repository
// https://github.com/ExpediaGroup/graphql-kotlin/blob/master/examples/server/spring-server/src/main/kotlin/com/expediagroup/graphql/examples/server/spring/dataloaders/CompanyDataLoader.kt
class DeliveryRepositoryImpl : DeliveryRepository {

    val deliveryList: List<Delivery> = listOf(
        Delivery(deliveryId = ID("1"), product = "Ice cream", deliveryStatus = DeliveryStatus.PENDING),
        Delivery(deliveryId = ID("2"), product = "Pancake", deliveryStatus = DeliveryStatus.RECEIVED),
        Delivery(deliveryId = ID("3"), product = "Waffle", deliveryStatus = DeliveryStatus.RECEIVED)
    )

    override fun findReceivedDeliveries(): List<Delivery> = deliveryList.filter { DeliveryStatus.RECEIVED.equals(it.deliveryStatus) }

    override fun findPendingDeliveries(): List<Delivery> = deliveryList.filter { !DeliveryStatus.RECEIVED.equals(it.deliveryStatus) }
}