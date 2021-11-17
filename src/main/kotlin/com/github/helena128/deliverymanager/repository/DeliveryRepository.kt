package com.github.helena128.deliverymanager.repository

import com.github.helena128.deliverymanager.entity.DeliveryEntity
import com.github.helena128.deliverymanager.model.DeliveryStatus

interface DeliveryRepository {

    fun findReceivedDeliveries(): List<DeliveryEntity>

    fun findPendingDeliveries(): List<DeliveryEntity>

    fun updateDeliveryStatus(id: String, newStatus: DeliveryStatus): DeliveryEntity?
}