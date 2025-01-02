package com.example.sw_planet_api;

import com.example.sw_planet_api.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.example.sw_planet_api.common.PlanetConstants.PLANET;
import static com.example.sw_planet_api.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetITWebTestClient {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void createPlanet_ReturnsCreated() {
        Planet sut = webTestClient.post()
                .uri("/planets")
                .bodyValue(PLANET)
                .exchange()
                .expectStatus().isCreated() // Já verifica o status HTTP 201
                .expectBody(Planet.class)
                .returnResult()
                .getResponseBody();

        assertThat(sut.getId()).isNotNull();
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
    }

    @Test
    public void getPlanet_ReturnsPlanet() {
        Planet sut = webTestClient.get().uri("/planets/1")
                .exchange().expectStatus().isOk().expectBody(Planet.class)
                .returnResult().getResponseBody();

        int statusCode = capturarStatusCode("/planets/1");

        assertThat(statusCode).isEqualTo(200);
        assertThat(sut).isEqualTo(TATOOINE);
    }


    @Test
    public void getPlanetByName_ReturnsPlanet() {
        Planet sut = webTestClient.get().uri("/planets/name/" + TATOOINE.getName())
                .exchange().expectStatus().isOk().expectBody(Planet.class)
                .returnResult().getResponseBody();

        int statusCode = capturarStatusCode("/planets/name/" + TATOOINE.getName());

        assertThat(statusCode).isEqualTo(200);
        assertThat(sut).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        Planet[] sut = webTestClient.get().uri("/planets")
                .exchange().expectStatus().isOk().expectBody(Planet[].class)
                .returnResult().getResponseBody();

        int statusCode = capturarStatusCode("/planets");

        assertThat(statusCode).isEqualTo(200);
        assertThat(sut).hasSize(3);
        assertThat(sut[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByClimate_ReturnsPlanets() {
        Planet[] sut = webTestClient.get().uri("/planets?climate=" + TATOOINE.getClimate())
                .exchange().expectStatus().isOk().expectBody(Planet[].class)
                .returnResult().getResponseBody();

        int statusCode = capturarStatusCode("/planets?climate=" + TATOOINE.getClimate());

        assertThat(statusCode).isEqualTo(200);
        assertThat(sut).hasSize(1);
        assertThat(sut[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByTerrain_ReturnsPlanets() {
        Planet[] sut = webTestClient.get().uri("/planets?terrain=" + TATOOINE.getTerrain())
                .exchange().expectStatus().isOk().expectBody(Planet[].class)
                .returnResult().getResponseBody();

        int statusCode = capturarStatusCode("/planets?terrain=" + TATOOINE.getTerrain());

        assertThat(statusCode).isEqualTo(200);
        assertThat(sut).hasSize(1);
        assertThat(sut[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void removePlanet_ReturnsNoContent() {
        var response = webTestClient
                .delete()
                .uri("/planets/" + TATOOINE.getId())
                .exchange()
                .expectStatus().isNoContent()
                .returnResult(Void.class);

        assertThat(response.getStatus().value()).isEqualTo(204);
    }


    private int capturarStatusCode(String url) {
        return webTestClient.get()
                .uri(url)
                .exchange()
                .returnResult(Planet.class)
                .getStatus()
                .value();
    }
}
