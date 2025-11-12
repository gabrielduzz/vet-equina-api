package br.com.vetequina.api;

import br.com.vetequina.api.entity.Horse;
import br.com.vetequina.api.entity.User;
import br.com.vetequina.api.repository.HorseRepository;
import br.com.vetequina.api.repository.UserRepository;
import br.com.vetequina.api.service.HorseService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("🐴 Testes de Integração - Horse Management System")
class HorseIntegrationTest {

    @Autowired
    private HorseRepository horseRepository;

    @Autowired
    private UserRepository userRepository;

    private HorseService horseService;
    private User testUser;
    private User otherUser;

    @BeforeEach
    void setUp() {
        horseRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .id(UUID.randomUUID())
                .firstName("João")
                .lastName("Silva")
                .email("joao@test.com")
                .build();
        testUser = userRepository.save(testUser);

        otherUser = User.builder()
                .id(UUID.randomUUID())
                .firstName("Maria")
                .lastName("Santos")
                .email("maria@test.com")
                .build();
        otherUser = userRepository.save(otherUser);

        horseService = new br.com.vetequina.api.service.HorseServiceImpl(
                horseRepository,
                userRepository);
    }

    @Test
    @Order(1)
    @DisplayName("✅ Deve criar cavalo para usuário autenticado")
    void testCriarCavalo() {
        Horse novoHorse = Horse.builder()
                .name("Relâmpago")
                .breed("Quarto de Milha")
                .birthdate(LocalDate.of(2018, 5, 15))
                .build();

        Horse horseSalvo = horseService.createForCurrentUser(novoHorse, testUser.getId());

        assertNotNull(horseSalvo.getId(), "ID deve ser gerado automaticamente");
        assertEquals("Relâmpago", horseSalvo.getName());
        assertEquals("Quarto de Milha", horseSalvo.getBreed());
        assertEquals(testUser.getId(), horseSalvo.getOwner().getId());
        assertNotNull(horseSalvo.getCreatedAt());
        assertNotNull(horseSalvo.getUpdatedAt());

        Horse horseDoBanco = horseRepository.findById(horseSalvo.getId()).orElseThrow();
        assertEquals("Relâmpago", horseDoBanco.getName());

        System.out.println("✅ Cavalo criado com sucesso: " + horseSalvo.getName());
    }

    @Test
    @Order(2)
    @DisplayName("✅ Deve listar apenas cavalos do usuário logado")
    void testListarMeusCavalos() {
        criarCavalo("Thunder", "Puro Sangue", testUser);
        criarCavalo("Shadow", "Árabe", testUser);
        criarCavalo("Lightning", "Appaloosa", otherUser);

        Page<Horse> meusHorses = horseService.listMyHorses(
                testUser.getId(),
                PageRequest.of(0, 10));

        assertEquals(2, meusHorses.getTotalElements(), "Deve retornar apenas 2 cavalos do usuário");
        assertTrue(meusHorses.stream()
                .allMatch(h -> h.getOwner().getId().equals(testUser.getId())),
                "Todos cavalos devem pertencer ao usuário logado");

        System.out.println("✅ Listagem correta: " + meusHorses.getTotalElements() + " cavalos");
    }

    @Test
    @Order(3)
    @DisplayName("✅ Deve buscar cavalo por ID do usuário")
    void testBuscarCavaloPorId() {
        Horse horse = criarCavalo("Estrela", "Mangalarga", testUser);

        Horse encontrado = horseService.getMyHorse(horse.getId(), testUser.getId());

        assertNotNull(encontrado);
        assertEquals("Estrela", encontrado.getName());
        assertEquals(testUser.getId(), encontrado.getOwner().getId());

        System.out.println("✅ Cavalo encontrado: " + encontrado.getName());
    }

    @Test
    @Order(4)
    @DisplayName("❌ Não deve permitir acessar cavalo de outro usuário")
    void testNaoDeveAcessarCavaloAlheio() {
        Horse horseDele = criarCavalo("Tornado", "Paint Horse", otherUser);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            horseService.getMyHorse(horseDele.getId(), testUser.getId());
        });

        assertTrue(exception.getMessage().contains("não encontrado"));
        System.out.println("✅ Segurança OK: Acesso negado ao cavalo alheio");
    }

    @Test
    @Order(5)
    @DisplayName("✅ Deve atualizar cavalo do usuário")
    void testAtualizarCavalo() {
        Horse horse = criarCavalo("Ventania", "Puro Sangue", testUser);
        UUID horseId = horse.getId();

        Horse updates = Horse.builder()
                .name("Ventania Veloz")
                .breed("Puro Sangue Inglês")
                .birthdate(LocalDate.of(2019, 1, 1))
                .build();

        Horse atualizado = horseService.updateMyHorse(horseId, updates, testUser.getId());

        assertEquals("Ventania Veloz", atualizado.getName());
        assertEquals("Puro Sangue Inglês", atualizado.getBreed());

        Horse doBanco = horseRepository.findById(horseId).orElseThrow();
        assertEquals("Ventania Veloz", doBanco.getName());

        System.out.println("✅ Cavalo atualizado: " + atualizado.getName());
    }

    @Test
    @Order(6)
    @DisplayName("❌ Não deve permitir atualizar cavalo de outro usuário")
    void testNaoDeveAtualizarCavaloAlheio() {
        Horse horseDele = criarCavalo("Zeus", "Lusitano", otherUser);
        Horse updates = Horse.builder().name("Zeus Poderoso").build();

        assertThrows(IllegalArgumentException.class, () -> {
            horseService.updateMyHorse(horseDele.getId(), updates, testUser.getId());
        });

        System.out.println("✅ Segurança OK: Atualização negada");
    }

    @Test
    @Order(7)
    @DisplayName("✅ Deve deletar cavalo do usuário")
    void testDeletarCavalo() {
        Horse horse = criarCavalo("Spirit", "Mustang", testUser);
        UUID horseId = horse.getId();

        horseService.deleteMyHorse(horseId, testUser.getId());

        assertFalse(horseRepository.findById(horseId).isPresent(),
                "Cavalo deve ser deletado do banco");

        System.out.println("✅ Cavalo deletado com sucesso");
    }

    @Test
    @Order(8)
    @DisplayName("❌ Não deve permitir deletar cavalo de outro usuário")
    void testNaoDeveDeletarCavaloAlheio() {
        Horse horseDela = criarCavalo("Bella", "Haflinger", otherUser);

        assertThrows(IllegalArgumentException.class, () -> {
            horseService.deleteMyHorse(horseDela.getId(), testUser.getId());
        });

        assertTrue(horseRepository.findById(horseDela.getId()).isPresent());
        System.out.println("✅ Segurança OK: Deleção negada");
    }

    private Horse criarCavalo(String name, String breed, User owner) {
        Horse horse = Horse.builder()
                .name(name)
                .breed(breed)
                .birthdate(LocalDate.of(2020, 1, 1))
                .owner(owner)
                .build();
        return horseRepository.save(horse);
    }
}