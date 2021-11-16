package com.github.helena128.deliverymanager.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription

@GraphQLDescription("Possible status of the delivery")
enum class DeliveryStatus {
    PENDING, RECEIVED
}