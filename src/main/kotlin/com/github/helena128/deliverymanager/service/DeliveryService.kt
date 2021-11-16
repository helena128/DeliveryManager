package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.Delivery

interface DeliveryService {
    fun getDeliveries(received: Boolean): List<Delivery>
}