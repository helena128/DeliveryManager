package com.github.helena128.deliverymanager.exception

class DeliveryNotUpdatedException(private val deliveryId: String)
    : RuntimeException("Couldn't update entity with deliveryId $deliveryId")