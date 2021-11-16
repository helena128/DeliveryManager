package com.github.helena128.deliverymanager.repository

import com.github.helena128.deliverymanager.entity.DeliveryEntity

interface DeliveryRepository {

    fun findReceivedDeliveries(): List<DeliveryEntity>

    fun findPendingDeliveries(): List<DeliveryEntity>
}