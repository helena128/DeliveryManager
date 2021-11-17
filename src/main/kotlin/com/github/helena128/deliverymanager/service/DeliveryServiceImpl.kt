package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono

@Service
class DeliveryServiceImpl(
    val deliveryRepository: DeliveryRepository,
    val deliveryMapper: DeliveryMapper,
    val transactionalOperator: TransactionalOperator
) : DeliveryService {

    override fun getDeliveries(received: Boolean) =
        getDeliveryEntities(received).map { deliveryMapper.convertToDto(it) }

    @Transactional
    override fun updateDeliveryStatus(deliveryId: String, newStatus: DeliveryStatus): Mono<Delivery> {
        return transactionalOperator.execute { action ->
            deliveryRepository.findById(deliveryId)
                .filter { !newStatus.equals(it.deliveryStatus) }
                .map { it.deliveryStatus = newStatus; it }
                .flatMap { deliveryRepository.save(it) }
        }
            .next()
            .map { deliveryMapper.convertToDto(it) }
            .switchIfEmpty(Mono.defer { Mono.error(RuntimeException("Exception happened, delivery $deliveryId")) })
    }

    private fun getDeliveryEntities(received: Boolean) =
        if (received) deliveryRepository.findAllByDeliveryStatus(DeliveryStatus.RECEIVED)
        else deliveryRepository.findAllByDeliveryStatusNot(DeliveryStatus.RECEIVED)
}