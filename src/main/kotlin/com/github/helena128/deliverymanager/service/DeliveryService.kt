package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.Delivery
import org.springframework.stereotype.Service

@Service
class DeliveryService {

    val deliveryList: List<Delivery> = listOf(
        Delivery(deliveryId = 1, product = "Ice cream"),
        Delivery(deliveryId = 2, product = "Pancake"),
        Delivery(deliveryId = 3, product = "Waffle")
    )

    fun getDeliveries(): List<Delivery> = deliveryList
}