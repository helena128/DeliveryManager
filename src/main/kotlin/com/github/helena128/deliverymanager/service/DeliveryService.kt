package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus

interface DeliveryService {
    fun getDeliveries(received: Boolean): List<Delivery>
    fun updateDeliveryStatus(deliveryId: String, newStatus: DeliveryStatus): Delivery
}