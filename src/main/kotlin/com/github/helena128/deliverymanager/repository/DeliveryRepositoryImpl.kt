package com.github.helena128.deliverymanager.repository

import com.expediagroup.graphql.generator.scalars.ID
import com.github.helena128.deliverymanager.entity.DeliveryEntity
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Repository
class DeliveryRepositoryImpl : DeliveryRepository {

    val deliveryList: List<DeliveryEntity> = listOf(
        DeliveryEntity(deliveryId = "1", product = "Ice cream", deliveryStatus = DeliveryStatus.PENDING, expectedDate = OffsetDateTime.now().plus(2, ChronoUnit.DAYS), quantity = 10, supplier = "A", expectedWarehouse = "W1"),
        DeliveryEntity(deliveryId = "2", product = "Pancake", deliveryStatus = DeliveryStatus.RECEIVED, expectedDate = OffsetDateTime.now().plus(1, ChronoUnit.DAYS), quantity = 20, supplier = "A", expectedWarehouse = "W2"),
        DeliveryEntity(deliveryId = "3", product = "Waffle", deliveryStatus = DeliveryStatus.RECEIVED, expectedDate = OffsetDateTime.now().plus(1, ChronoUnit.DAYS), quantity = 30, supplier = "A", expectedWarehouse = "W3")
    )

    override fun findReceivedDeliveries(): List<DeliveryEntity> = deliveryList.filter { DeliveryStatus.RECEIVED.equals(it.deliveryStatus) }

    override fun findPendingDeliveries(): List<DeliveryEntity> = deliveryList.filter { !DeliveryStatus.RECEIVED.equals(it.deliveryStatus) }
}