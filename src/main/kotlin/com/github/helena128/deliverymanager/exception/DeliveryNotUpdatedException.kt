package com.github.helena128.deliverymanager.exception

/**
 * Represents an exception thrown in case delivery entity was not updated
 *
 * @deliveryId unique identifier of delivery to be updated
 */
class DeliveryNotUpdatedException(deliveryId: String)
    : RuntimeException("Couldn't update entity with deliveryId $deliveryId")