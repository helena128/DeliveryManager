package com.github.helena128.deliverymanager.util

import com.expediagroup.graphql.generator.scalars.ID
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus

object DataHelper {

    val deliveryList: List<Delivery> = listOf(
        Delivery(deliveryId = ID("1"), product = "Ice cream", deliveryStatus = DeliveryStatus.PENDING),
        Delivery(deliveryId = ID("2"), product = "Pancake", deliveryStatus = DeliveryStatus.RECEIVED),
        Delivery(deliveryId = ID("3"), product = "Waffle", deliveryStatus = DeliveryStatus.RECEIVED)
    )

    fun findDeliveriesByStatus(status: DeliveryStatus): List<Delivery> =
        deliveryList.filter { status.equals(it.deliveryStatus) }.sortedByDescending { it.deliveryId.value }
}