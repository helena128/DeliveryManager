package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.exception.DeliveryNotUpdatedException
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import org.springframework.stereotype.Service
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

    override fun updateDeliveryStatus(deliveryId: String, newStatus: DeliveryStatus): Mono<Delivery> {

        return transactionalOperator.execute { action ->
            deliveryRepository.findByDeliveryId(deliveryId)
                .doOnNext { println(">> Found delivery with id $deliveryId ") }
                .filter { !newStatus.equals(it.deliveryStatus) }
                .doOnNext { println(">> Delivery with id $deliveryId has status ${it.deliveryStatus.name}") }
                .map { it.deliveryStatus = newStatus; it }
                .flatMap { deliveryRepository.save(it) }
                .doOnNext { println(">> Updated delivery with id $deliveryId, new status ${it.deliveryStatus.name}") }
                .map { deliveryMapper.convertToDto(it) }
        }
            .next()
            .switchIfEmpty(Mono.error(DeliveryNotUpdatedException(deliveryId)))
    }

    private fun getDeliveryEntities(received: Boolean) =
        if (received) deliveryRepository.findAllByDeliveryStatus(DeliveryStatus.RECEIVED)
        else deliveryRepository.findAllByDeliveryStatusNot(DeliveryStatus.RECEIVED)
}