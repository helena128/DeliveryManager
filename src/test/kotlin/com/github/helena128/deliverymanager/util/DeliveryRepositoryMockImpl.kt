package com.github.helena128.deliverymanager.util

import com.github.helena128.deliverymanager.entity.DeliveryEntity
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository

class DeliveryRepositoryMockImpl : DeliveryRepository {

    override fun findReceivedDeliveries() =
        DataHelper.findDeliveriesByStatus(DeliveryStatus.RECEIVED)

    override fun findPendingDeliveries() =
        DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING)

    override fun updateDeliveryStatus(id: String, newStatus: DeliveryStatus): DeliveryEntity? {
        val deliveryEntity = DataHelper.deliveryList.find { id.equals(it.deliveryId) }
        deliveryEntity?.deliveryStatus = DeliveryStatus.RECEIVED
        return deliveryEntity
    }
}