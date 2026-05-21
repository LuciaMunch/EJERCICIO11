package com.prog4.ejercicio10;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de carga del contexto de Spring.
 * Usa H2 en memoria (configurado en src/test/resources/application.properties)
 * para no depender de PostgreSQL al correr los tests.
 */
@SpringBootTest
class Ejercicio10ApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring levanta correctamente
    }
}
