package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import org.springframework.stereotype.Service

@Service
class DeliveryServiceImpl(val deliveryRepository: DeliveryRepository, val deliveryMapper: DeliveryMapper) : DeliveryService {

    override fun getDeliveries(received: Boolean) = getDeliveryEntities(received).map { deliveryMapper.convertToDto(it) }

    override fun updateDeliveryStatus(deliveryId: String, newStatus: DeliveryStatus): Delivery {
        // TODO: check current status of delivery, if RECEIVED
        val deliveryEntity = deliveryRepository.updateDeliveryStatus(deliveryId, newStatus)
        if (deliveryEntity == null) {
            throw IllegalArgumentException("Couldn't find entity with id $deliveryId") // TODO: custom exception
        }
        return deliveryMapper.convertToDto(deliveryEntity)
    }

    private fun getDeliveryEntities(received: Boolean) =
        if (received) deliveryRepository.findReceivedDeliveries() else deliveryRepository.findPendingDeliveries()
}