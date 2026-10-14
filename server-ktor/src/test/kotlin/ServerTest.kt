package dev.builtbyswap

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class ServerTest {

    @Test
    fun `health returns ok`() = testApplication {
        configure()                                   // starts the real app from application.yaml
        val response = client.get("/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"status\":\"ok\""))
    }

    @Test
    fun `plan for a date in the plan returns 200`() = testApplication {
        configure()
        val response = client.get("/plan/2026-10-11")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"id\":\"PRJ-02\""))
    }

    @Test
    fun `plan with a malformed date returns 400`() = testApplication {
        configure()
        val response = client.get("/plan/yesterday")
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertTrue(response.bodyAsText().contains("date must look like"))
    }

    @Test
    fun `plan for a date outside the plan returns 404`() = testApplication {
        configure()
        val response = client.get("/plan/2030-01-01")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertTrue(response.bodyAsText().contains("no plan for 2030-01-01"))
    }

    @Test
    fun `question 433 returns 200`() = testApplication {
        configure()
        val response = client.get("/questions/433")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("On-device AI"))
    }

    @Test
    fun `question with a non-number returns 400`() = testApplication {
        configure()
        assertEquals(HttpStatusCode.BadRequest, client.get("/questions/abc").status)
    }

    @Test
    fun `unknown question returns 404`() = testApplication {
        configure()
        assertEquals(HttpStatusCode.NotFound, client.get("/questions/99999").status)
    }

    private suspend fun HttpClient.postJson(path: String, json: String) =
        post(path) { contentType(ContentType.Application.Json); setBody(json) }

    @Test
    fun `creating progress returns 201 and a Location header`() = testApplication {
        configure()
        val response = client.postJson("/progress", """{"date":"2026-10-15","status":"done","note":"BE-03"}""")
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("/progress/2026-10-15", response.headers[HttpHeaders.Location])
        assertTrue(response.bodyAsText().contains("\"status\":\"done\""))
    }

    @Test
    fun `creating the same date twice returns 409`() = testApplication {
        configure()
        val body = """{"date":"2026-10-15","status":"done"}"""
        assertEquals(HttpStatusCode.Created, client.postJson("/progress", body).status)
        assertEquals(HttpStatusCode.Conflict, client.postJson("/progress", body).status)
    }

    @Test
    fun `an unknown status returns 422 with the field named`() = testApplication {
        configure()
        val response = client.postJson("/progress", """{"date":"2026-10-15","status":"banana"}""")
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
        assertTrue(response.bodyAsText().contains("validation_failed"))
        assertTrue(response.bodyAsText().contains("status"))
    }

    @Test
    fun `a bad date returns 422`() = testApplication {
        configure()
        val response = client.postJson("/progress", """{"date":"tomorrow","status":"done"}""")
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun `broken JSON returns 400`() = testApplication {
        configure()
        assertEquals(HttpStatusCode.BadRequest, client.postJson("/progress", "{ not json").status)
    }

    @Test
    fun `a missing required field returns 400 not 422`() = testApplication {
        configure()
        assertEquals(HttpStatusCode.BadRequest, client.postJson("/progress", """{"date":"2026-10-15"}""").status)
    }

    @Test
    fun `a body that is not JSON returns 415`() = testApplication {
        configure()
        val response = client.post("/progress") { setBody("hello") }
        assertEquals(HttpStatusCode.UnsupportedMediaType, response.status)
    }

    @Test
    fun `an unknown extra field is ignored (tolerant reader)`() = testApplication {
        configure()
        val response = client.postJson("/progress", """{"date":"2026-10-16","status":"done","addedLater":"x"}""")
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `a missing note uses the default and is still sent`() = testApplication {
        configure()
        val response = client.postJson("/progress", """{"date":"2026-10-17","status":"partial"}""")
        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("\"note\":\"\""))
    }

    @Test
    fun `reading progress back works, and an unknown date is 404`() = testApplication {
        configure()
        client.postJson("/progress", """{"date":"2026-10-18","status":"done"}""")
        assertEquals(HttpStatusCode.OK, client.get("/progress/2026-10-18").status)
        assertEquals(HttpStatusCode.NotFound, client.get("/progress/2030-01-01").status)
    }
}