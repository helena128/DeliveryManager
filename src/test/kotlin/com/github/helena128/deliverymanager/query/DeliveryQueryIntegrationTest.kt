package com.github.helena128.deliverymanager.query

import com.github.helena128.deliverymanager.constants.GRAPHQL_ENDPOINT
import com.github.helena128.deliverymanager.constants.GRAPHQL_MEDIA_TYPE
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import com.github.helena128.deliverymanager.util.DataHelper
import org.hamcrest.collection.IsIn.`in`
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
class DeliveryQueryIntegrationTest(
    @Autowired private val testClient: WebTestClient,
    @Autowired private val repository: DeliveryRepository
) {

    @BeforeEach
    fun setup() {
        StepVerifier.create(repository.saveAll(DataHelper.deliveryList))
            .expectNextCount(DataHelper.deliveryList.size.toLong())
            .verifyComplete()
    }

    @Test
    fun `Test getting pending items`() {
        val query = "getDeliveries"
        val bodyValue = """
              $query (received: false) {
                deliveryId
                product
              }
        """.trimIndent()

        val expectedData = DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING)

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue("query { $bodyValue }")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .jsonPath("$.data").exists()
            .jsonPath("$.data.$query").exists()
            .jsonPath("$.data.$query.[0].deliveryId").isEqualTo(expectedData.get(0).deliveryId)
            .jsonPath("$.data.$query.[0].product").isEqualTo(expectedData.get(0).product)
            .jsonPath("$.data.$query.[0].deliveryStatus").doesNotExist()
    }

    @Test
    fun `Test getting received items`() {
        val query = """
              getDeliveries (received: true) {
                deliveryId
                product
                deliveryStatus
              }
        """.trimIndent()

        val expectedData = DataHelper.findDeliveriesByStatus(DeliveryStatus.RECEIVED)

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue("query { $query }")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .jsonPath("$.data").exists()
            .jsonPath("$.data.getDeliveries").exists()
            .jsonPath("$.data.getDeliveries.[0].deliveryId").value(`in`(expectedData.map { it.deliveryId }))
            .jsonPath("$.data.getDeliveries.[0].product").value(`in`(expectedData.map { it.product }))
            .jsonPath("$.data.getDeliveries.[0].deliveryStatus").isEqualTo(DeliveryStatus.RECEIVED.name)
            .jsonPath("$.data.getDeliveries.[1].deliveryId").value(`in`(expectedData.map { it.deliveryId }))
            .jsonPath("$.data.getDeliveries.[1].product").value(`in`(expectedData.map { it.product }))
    }

    @AfterEach
    fun tearDown() {
        StepVerifier.create(repository.deleteAll())
            .verifyComplete()
    }
}