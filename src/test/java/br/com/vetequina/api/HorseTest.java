package br.com.vetequina.api;

import org.junit.jupiter.api.Test;
import br.com.vetequina.api.entity.Horse;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class HorseTest {

    @Test
    void prePersist_deveDefinirCreatedAtEUpdatedAt() {
        Horse horse = new Horse();
        assertNull(horse.getCreatedAt());
        assertNull(horse.getUpdatedAt());

        horse.prePersist();

        assertNotNull(horse.getCreatedAt());
        assertNotNull(horse.getUpdatedAt());
        assertEquals(horse.getCreatedAt(), horse.getUpdatedAt());
    }

    @Test
    void preUpdate_deveAtualizarApenasUpdatedAt() throws InterruptedException {
        Horse horse = new Horse();
        horse.prePersist();

        Instant createdAtInicial = horse.getCreatedAt();
        Instant updatedAtInicial = horse.getUpdatedAt();

        Thread.sleep(10);

        horse.preUpdate();

        Instant updatedAtFinal = horse.getUpdatedAt();

        assertEquals(createdAtInicial, horse.getCreatedAt());
        assertNotEquals(updatedAtInicial, updatedAtFinal);
        assertTrue(updatedAtFinal.isAfter(updatedAtInicial));
    }
}