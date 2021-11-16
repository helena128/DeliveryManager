package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.repository.DeliveryRepository
import org.springframework.stereotype.Service

@Service
class DeliveryServiceImpl(val deliveryRepository: DeliveryRepository, val deliveryMapper: DeliveryMapper) : DeliveryService {

    override fun getDeliveries(received: Boolean) = getDeliveryEntities(received).map { deliveryMapper.convertToDto(it) }

    fun getDeliveryEntities(received: Boolean) =
        if (received) deliveryRepository.findReceivedDeliveries() else deliveryRepository.findPendingDeliveries()
}