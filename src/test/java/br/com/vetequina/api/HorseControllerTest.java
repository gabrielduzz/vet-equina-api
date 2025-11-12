package br.com.vetequina.api;

import br.com.vetequina.api.entity.Horse;
import br.com.vetequina.api.mapper.HorseMapper;
import br.com.vetequina.api.security.CurrentUser;
import br.com.vetequina.api.controller.HorseController;
import br.com.vetequina.api.dto.horse.*;
import br.com.vetequina.api.service.HorseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorseControllerTest {

    @Mock
    private HorseService service;

    @Mock
    private HorseMapper mapper;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private HorseController controller;

    @Test
    void create_deveRetornarOk_quandoRequestValido() {
        UUID currentUserId = UUID.randomUUID();
        UUID horseId = UUID.randomUUID();

        HorseCreateRequest requestBody = new HorseCreateRequest(
                "Pé de Pano",
                "Manga-larga",
                LocalDate.of(2020, 1, 1),
                null);

        Horse horseSemId = Horse.builder()
                .name("Pé de Pano")
                .breed("Manga-larga")
                .birthdate(LocalDate.of(2020, 1, 1))
                .build();

        Horse horseSalvo = Horse.builder()
                .id(horseId)
                .name("Pé de Pano")
                .breed("Manga-larga")
                .birthdate(LocalDate.of(2020, 1, 1))
                .build();

        HorseResponse responseDto = new HorseResponse(
                horseId,
                "Pé de Pano",
                "Manga-larga",
                LocalDate.of(2020, 1, 1),
                null);

        when(currentUser.id()).thenReturn(currentUserId);
        when(mapper.toEntity(requestBody)).thenReturn(horseSemId);
        when(service.createForCurrentUser(horseSemId, currentUserId)).thenReturn(horseSalvo);
        when(mapper.toResponse(horseSalvo)).thenReturn(responseDto);

        ResponseEntity<HorseResponse> response = controller.create(requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pé de Pano", response.getBody().name());
        assertEquals(horseId, response.getBody().id());

        verify(currentUser).id();
        verify(mapper).toEntity(requestBody);
        verify(service).createForCurrentUser(horseSemId, currentUserId);
        verify(mapper).toResponse(horseSalvo);
        verifyNoMoreInteractions(service, mapper, currentUser);
    }

    @Test
    void get_deveRetornarCavalo_quandoIdValido() {
        UUID currentUserId = UUID.randomUUID();
        UUID horseId = UUID.randomUUID();

        Horse horse = Horse.builder()
                .id(horseId)
                .name("Thunder")
                .breed("Puro Sangue")
                .birthdate(LocalDate.of(2019, 5, 10))
                .build();

        HorseResponse responseDto = new HorseResponse(
                horseId,
                "Thunder",
                "Puro Sangue",
                LocalDate.of(2019, 5, 10),
                null);

        when(currentUser.id()).thenReturn(currentUserId);
        when(service.getMyHorse(horseId, currentUserId)).thenReturn(horse);
        when(mapper.toResponse(horse)).thenReturn(responseDto);

        ResponseEntity<HorseResponse> response = controller.get(horseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Thunder", response.getBody().name());

        verify(currentUser).id();
        verify(service).getMyHorse(horseId, currentUserId);
        verify(mapper).toResponse(horse);
    }
}