package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryMongoDbRepository
import com.github.helena128.deliverymanager.util.DataHelper
import com.nhaarman.mockito_kotlin.times
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class DeliveryServiceImplTest { // TODO: add test for mutation

    @Mock
    lateinit var repository: DeliveryMongoDbRepository

    @Mock
    lateinit var mapper: DeliveryMapperImpl

    @InjectMocks
    lateinit var deliveryService: DeliveryServiceImpl

    @Mock
    lateinit var transactionalOperator: TransactionalOperator

    @Test
    fun `Test Listing Not Received Items`() {
        val expectedValues = DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING)
        val pendingDeliveries = Flux.fromIterable(expectedValues)
        Mockito.`when`(repository.findAllByDeliveryStatusNot(DeliveryStatus.RECEIVED)).thenReturn(pendingDeliveries)
        Mockito.`when`(mapper.convertToDto(any())).thenCallRealMethod()

        val actualResult = deliveryService.getDeliveries(false)
        val expectedResult = expectedValues.map { mapper.convertToDto(it) }
        StepVerifier.create(actualResult)
            .assertNext { Assertions.assertTrue(expectedResult.contains(it)) }
            .verifyComplete()
        Mockito.verify(repository, times(1)).findAllByDeliveryStatusNot(DeliveryStatus.RECEIVED)
        Mockito.verify(repository, times(0)).findAllByDeliveryStatus(DeliveryStatus.PENDING)
    }

    @Test
    fun `Test Listing Received Items`() {
        val sourceValues = DataHelper.findDeliveriesByStatus(DeliveryStatus.RECEIVED)
        val receivedDeliveries = Flux.fromIterable(sourceValues)
        Mockito.`when`(repository.findAllByDeliveryStatus(DeliveryStatus.RECEIVED)).thenReturn(receivedDeliveries)
        Mockito.`when`(mapper.convertToDto(any())).thenCallRealMethod()

        val actualResult = deliveryService.getDeliveries(true)
        val expectedResult = sourceValues.map { mapper.convertToDto(it) }
        StepVerifier.create(actualResult)
            .assertNext { println(it); Assertions.assertTrue(expectedResult.contains(it)) }
            .assertNext { println(it); Assertions.assertTrue(expectedResult.contains(it)) }
            .verifyComplete()

        Mockito.verify(repository, times(1)).findAllByDeliveryStatus(DeliveryStatus.RECEIVED)
        Mockito.verify(repository, times(0)).findAllByDeliveryStatusNot(DeliveryStatus.RECEIVED)
    }

    @Test
    fun `Test Receiving Empty List When No Items In System`() {
        Mockito.`when`(repository.findAllByDeliveryStatus(DeliveryStatus.RECEIVED)).thenReturn(Flux.empty())
        val actualResult = deliveryService.getDeliveries(true)

        StepVerifier.create(actualResult)
            .expectNextCount(0)
            .verifyComplete()
        Mockito.verify(repository, times(0)).findAllByDeliveryStatusNot(DeliveryStatus.RECEIVED)
        Mockito.verify(repository, times(1)).findAllByDeliveryStatus(DeliveryStatus.RECEIVED)
    }

}