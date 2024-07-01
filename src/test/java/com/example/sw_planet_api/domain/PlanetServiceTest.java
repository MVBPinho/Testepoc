package com.example.sw_planet_api.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.example.sw_planet_api.common.PlanetConstants.PLANET;
import static com.example.sw_planet_api.common.PlanetConstants.INVALID_PLANET;

//@SpringBootTest(classes = PlanetService.class)
@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

   // @Autowired
    @InjectMocks
    private PlanetService planetService;

    //@MockBean
    @Mock
    private PlanetRepository planetRepository;

    @Test
    // operacao_estado_retorno
    public void createPlanet_WithValidData_ReturnsPlanet() {

        //AAA (ARRANGE, ACT, ASSERT)
        // Arrange
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        // Act
        Planet sut = planetService.create(PLANET);

        // Assert
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_TrowsException() {
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.get(1L);

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty() {
        when(planetRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.get(1L);

        assertThat(sut).isEmpty();
    }
}
