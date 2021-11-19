package com.github.helena128.deliverymanager.mutation

import com.github.helena128.deliverymanager.constants.GRAPHQL_ENDPOINT
import com.github.helena128.deliverymanager.constants.GRAPHQL_MEDIA_TYPE
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import com.github.helena128.deliverymanager.util.DataHelper
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeliveryMutationIntegrationTest(@Autowired private val testClient: WebTestClient,
                                      @Autowired private val repository: DeliveryRepository) {

    @BeforeEach
    fun setup() {
        StepVerifier.create(repository.saveAll(DataHelper.deliveryList))
            .expectNextCount(DataHelper.deliveryList.size.toLong())
            .verifyComplete()
    }

    @Test
    fun `Verify Marking Non-Delivered Item Results in Success`() {
        val query = "markDeliveryReceived"
        val pendingEntity = DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING).first()
        val bodyValue = """
            mutation {
            	$query(id: "${pendingEntity.deliveryId}") {
                    deliveryId
                    deliveryStatus
                    product
                    updatedDate
              }
            }
        """.trimIndent()

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue(bodyValue)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .jsonPath("$.data").exists()
            .jsonPath("$.data.$query").exists()
            .jsonPath("$.data.$query.deliveryId").isEqualTo(pendingEntity.deliveryId)
            .jsonPath("$.data.$query.deliveryStatus").isEqualTo(DeliveryStatus.RECEIVED.name)
            .jsonPath("$.data.$query.product").isEqualTo(pendingEntity.product)
            .jsonPath("$.data.$query.expectedDate").doesNotExist()
            .jsonPath("$.data.$query.updatedDate").isNotEmpty()
    }

    @Test
    fun `Verify Marking Delivered Item Results in Client error`() {
        val query = "markDeliveryReceived"
        val receivedEntity = DataHelper.findDeliveriesByStatus(DeliveryStatus.RECEIVED).first()
        val bodyValue = """
            mutation {
            	$query(id: "${receivedEntity.deliveryId}") {
                    deliveryId
                    deliveryStatus
                    product
              }
            }
        """.trimIndent()

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue(bodyValue)
            .exchange()
            .expectBody()
            .jsonPath("$.errors.[0].message").value(containsString("Couldn't update entity with deliveryId ${receivedEntity.deliveryId}"))
    }

    @AfterEach
    fun tearDown() {
        StepVerifier.create(repository.deleteAll())
            .verifyComplete()
    }
}