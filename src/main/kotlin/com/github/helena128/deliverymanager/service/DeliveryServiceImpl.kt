package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.entity.DeliveryEntity
import com.github.helena128.deliverymanager.exception.DeliveryNotUpdatedException
import com.github.helena128.deliverymanager.model.Delivery
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import com.github.helena128.deliverymanager.util.LoggingContants.CURRENT_STATUS_FILTER
import com.github.helena128.deliverymanager.util.LoggingContants.DELIVERY_FOUND
import com.github.helena128.deliverymanager.util.LoggingContants.DELIVERY_SAVED
import com.github.helena128.deliverymanager.util.LoggingContants.GET_FINISHED
import com.github.helena128.deliverymanager.util.LoggingContants.GET_STARTED
import com.github.helena128.deliverymanager.util.LoggingContants.SUCCESS_UPDATED
import com.github.helena128.deliverymanager.util.loggerFor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime

@Service
class DeliveryServiceImpl(
    val deliveryRepository: DeliveryRepository,
    val deliveryMapper: DeliveryMapper,
    val transactionalOperator: TransactionalOperator
) : DeliveryService {

    private val LOG = loggerFor(javaClass)

    /**
     * Retrieves list of deliveries based on the input parameter
     *
     * @param received when true, method returns deliveries that have already been received, when false - otherwise
     */
    override fun getDeliveries(received: Boolean): Flux<Delivery> =
        Flux.just(received)
            .doOnNext { LOG.info("MSG=$GET_STARTED, receivedFlag=$received") }
            .flatMap {receivedFlag -> getDeliveryEntities(received)}
            .map { deliveryMapper.convertToDto(it) }
            .doOnComplete { LOG.info("MSG=$GET_FINISHED") }

    /**
     * Updates status of the delivery to the newStatus, sets updatedDate to the current moment
     *
     * @param deliveryId unique identifier of the delivery to be updated
     * @param newStatus status to be set for the delivery
     */
    @Transactional
    override fun updateDeliveryStatus(deliveryId: String, newStatus: DeliveryStatus): Mono<Delivery> {

        return transactionalOperator.execute { action ->
            deliveryRepository.findByDeliveryId(deliveryId)
                .doOnNext { LOG.debug("MSG=$DELIVERY_FOUND, deliveryId=$deliveryId") }
                .filter { !newStatus.equals(it.deliveryStatus) }
                .doOnNext { LOG.debug("MSG=$CURRENT_STATUS_FILTER, deliveryId=$deliveryId, deliveryStatus=${it.deliveryStatus.name}") }
                .map { updateStatus(it, newStatus) }
                .flatMap { deliveryRepository.save(it) }
                .doOnNext { LOG.debug("MSG=$DELIVERY_SAVED, deliveryId=$deliveryId, deliveryStatus=${it.deliveryStatus.name}") }
                .map { deliveryMapper.convertToDto(it) }
        }
            .next()
            .doOnNext { LOG.info("MSG=$SUCCESS_UPDATED, deliveryId=${it.deliveryId}") }
            .switchIfEmpty(Mono.error(DeliveryNotUpdatedException(deliveryId)))
    }

    private fun getDeliveryEntities(received: Boolean): Flux<DeliveryEntity> =
        when {
            received -> deliveryRepository.findAllByDeliveryStatus(DeliveryStatus.RECEIVED)
            else -> deliveryRepository.findAllByDeliveryStatusNot(DeliveryStatus.RECEIVED)
        }

    private fun updateStatus(delivery: DeliveryEntity, newStatus: DeliveryStatus): DeliveryEntity {
        delivery.deliveryStatus = newStatus
        delivery.updatedDate = OffsetDateTime.now()
        return delivery
    }
}