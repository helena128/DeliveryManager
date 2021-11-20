package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.DeliveryManagerApplication
import com.github.helena128.deliverymanager.entity.DeliveryEntity
import com.github.helena128.deliverymanager.exception.DeliveryNotUpdatedException
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import com.github.helena128.deliverymanager.util.DataHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest(classes = [DeliveryManagerApplication::class])
@ActiveProfiles("test")
class DeliveryServiceImplTest {

    @MockBean
    lateinit var repository: DeliveryRepository

    @MockBean
    lateinit var mapper: DeliveryMapperImpl

    @Autowired
    lateinit var deliveryService: DeliveryServiceImpl

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

    @Test
    fun `Test updating delivery status for pending delivery`() {
        val testDelivery = DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING).first().copy(id = 1L)
        Mockito.`when`(repository.save(any(DeliveryEntity::class.java))).thenReturn(Mono.just(testDelivery))
        Mockito.`when`(repository.findByDeliveryId(Mockito.anyString())).thenReturn(Mono.just(testDelivery))
        Mockito.`when`(mapper.convertToDto(any(DeliveryEntity::class.java))).thenCallRealMethod()

        val actualResult = deliveryService.updateDeliveryStatus(testDelivery.deliveryId, DeliveryStatus.RECEIVED)
        StepVerifier.create(actualResult)
            .assertNext { delivery ->  {
                Assertions.assertEquals(testDelivery.deliveryId, delivery.deliveryId);
                Assertions.assertEquals(testDelivery.product, delivery.product);
                Assertions.assertNotNull(testDelivery.updatedDate);
                Assertions.assertEquals(DeliveryStatus.RECEIVED, testDelivery.deliveryStatus);
            }}
            .verifyComplete()
    }

    @Test
    fun `Test updating delivery status for non-existent delivery`() {
        val testDeliveryId = "nonExistentDeliveryId"
        Mockito.`when`(repository.findByDeliveryId(Mockito.anyString())).thenReturn(Mono.empty())

        val actualResult = deliveryService.updateDeliveryStatus(testDeliveryId, DeliveryStatus.RECEIVED)
        StepVerifier.create(actualResult)
            .expectError(DeliveryNotUpdatedException::class.java)
            .verify()
    }

    @Test
    fun `Test error on update of delivery status results in exception`() {
        val testDelivery = DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING).first().copy(id = 1L)
        Mockito.`when`(repository.save(any(DeliveryEntity::class.java))).thenReturn(Mono.error(RuntimeException("Some exception happened")))
        Mockito.`when`(repository.findByDeliveryId(Mockito.anyString())).thenReturn(Mono.just(testDelivery))

        val actualResult = deliveryService.updateDeliveryStatus(testDelivery.deliveryId, DeliveryStatus.RECEIVED)
        StepVerifier.create(actualResult)
            .expectError(RuntimeException::class.java)
            .verify()
    }

}
