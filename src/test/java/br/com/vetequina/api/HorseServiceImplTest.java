package br.com.vetequina.api;

import br.com.vetequina.api.entity.Horse;
import br.com.vetequina.api.entity.User;
import br.com.vetequina.api.repository.HorseRepository;
import br.com.vetequina.api.repository.UserRepository;
import br.com.vetequina.api.service.HorseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorseServiceImplTest {

    @Mock
    private HorseRepository horseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HorseServiceImpl service;

    @Test
    void getMyHorse_deveRetornarCavalo_quandoUsuarioForDono() {
        UUID currentUserId = UUID.randomUUID();
        UUID horseId = UUID.randomUUID();

        User owner = User.builder()
                .id(currentUserId)
                .firstName("João")
                .lastName("Silva")
                .email("joao@test.com")
                .build();

        Horse horse = Horse.builder()
                .id(horseId)
                .name("Relâmpago")
                .owner(owner)
                .build();

        when(horseRepository.findByIdAndOwnerId(horseId, currentUserId))
                .thenReturn(Optional.of(horse));

        Horse result = service.getMyHorse(horseId, currentUserId);

        assertNotNull(result);
        assertEquals(horseId, result.getId());
        assertEquals("Relâmpago", result.getName());
        assertEquals(currentUserId, result.getOwner().getId());

        verify(horseRepository).findByIdAndOwnerId(horseId, currentUserId);
        verifyNoMoreInteractions(horseRepository);
    }

    @Test
    void getMyHorse_deveLancarExcecao_quandoCavaloNaoEncontrado() {
        UUID currentUserId = UUID.randomUUID();
        UUID horseId = UUID.randomUUID();

        when(horseRepository.findByIdAndOwnerId(horseId, currentUserId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getMyHorse(horseId, currentUserId);
        });

        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(horseRepository).findByIdAndOwnerId(horseId, currentUserId);
    }
}