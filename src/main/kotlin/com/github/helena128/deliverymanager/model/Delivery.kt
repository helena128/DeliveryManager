package com.github.helena128.deliverymanager.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class Delivery(
        @GraphQLDescription("Id of the delivery")
        val deliveryId: Int, // TODO: change type
        val product: String/*,
        val supplier: String,
        val quantity: Long,
        val expectedDate: Instant?,
        val expectedWarehouse: String,
        val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,
        val deliveryDate: Instant?*/
)