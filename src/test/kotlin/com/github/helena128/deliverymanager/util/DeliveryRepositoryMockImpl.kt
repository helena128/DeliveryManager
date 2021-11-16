package com.github.helena128.deliverymanager.util

import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository

class DeliveryRepositoryMockImpl : DeliveryRepository {

    override fun findReceivedDeliveries() =
        DataHelper.findDeliveriesByStatus(DeliveryStatus.RECEIVED)

    override fun findPendingDeliveries() =
        DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING)
}