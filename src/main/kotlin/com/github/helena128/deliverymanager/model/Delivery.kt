package com.github.helena128.deliverymanager.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import java.time.Instant

data class Delivery(
        @GraphQLDescription("Id of the delivery")
        val deliveryId: ID,
        val product: String/*,
        val supplier: String,
        val quantity: Long,
        val expectedDate: Instant?,
        val expectedWarehouse: String*/,
        val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING/*,
        val deliveryDate: Instant? = null*/
)