package com.github.helena128.deliverymanager.repository

import com.github.helena128.deliverymanager.model.Delivery

interface DeliveryRepository {

    fun findReceivedDeliveries(): List<Delivery>

    fun findPendingDeliveries(): List<Delivery>
}