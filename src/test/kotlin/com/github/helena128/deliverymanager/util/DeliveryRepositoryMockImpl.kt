package com.github.helena128.deliverymanager.util

import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository

class DeliveryRepositoryMockImpl : DeliveryRepository {

    override fun findReceivedDeliveries(): List<Delivery> =
        DataHelper.findDeliveriesByStatus(DeliveryStatus.RECEIVED)

    override fun findPendingDeliveries(): List<Delivery> =
        DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING)
}