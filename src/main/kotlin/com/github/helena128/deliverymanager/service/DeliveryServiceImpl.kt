package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import org.springframework.stereotype.Service

@Service
class DeliveryServiceImpl(val deliveryRepository: DeliveryRepository) : DeliveryService {

    override fun getDeliveries(received: Boolean): List<Delivery> {
        return if (received) deliveryRepository.findReceivedDeliveries() else deliveryRepository.findPendingDeliveries()
    }
}