package com.github.helena128.deliverymanager.util

import com.expediagroup.graphql.generator.scalars.ID
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

object DataHelper {

    val deliveryList: List<Delivery> = listOf(
        Delivery(deliveryId = ID("1"), product = "Ice cream", deliveryStatus = DeliveryStatus.PENDING, expectedDate = OffsetDateTime.now().plus(2, ChronoUnit.DAYS), quantity = 10, supplier = "A", expectedWarehouse = "W1"),
        Delivery(deliveryId = ID("2"), product = "Pancake", deliveryStatus = DeliveryStatus.RECEIVED, expectedDate = OffsetDateTime.now().plus(1, ChronoUnit.DAYS), quantity = 20, supplier = "A", expectedWarehouse = "W2"),
        Delivery(deliveryId = ID("3"), product = "Waffle", deliveryStatus = DeliveryStatus.RECEIVED, expectedDate = OffsetDateTime.now().plus(1, ChronoUnit.DAYS), quantity = 30, supplier = "A", expectedWarehouse = "W3")
    )

    fun findDeliveriesByStatus(status: DeliveryStatus): List<Delivery> =
        deliveryList.filter { status.equals(it.deliveryStatus) }.sortedByDescending { it.deliveryId.value }
}