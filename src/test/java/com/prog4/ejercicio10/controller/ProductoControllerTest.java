package com.prog4.ejercicio10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prog4.ejercicio10.entity.Producto;
import com.prog4.ejercicio10.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Parte 3 — Tests de Capa de Controlador
 *
 * @WebMvcTest levanta solo la capa web (sin base de datos ni contexto completo).
 * @MockBean simula el ProductoRepository para aislar el controlador.
 * MockMvc permite ejecutar peticiones HTTP simuladas y verificar respuestas.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // El controlador inyecta el repositorio directamente, por eso mockeamos el repo
    @MockBean
    private ProductoRepository productoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        producto1 = new Producto(1L, "Laptop",  1500.00, 10);
        producto2 = new Producto(2L, "Monitor",  350.00,  5);
    }

    // ─────────────────────────────────────────────────────────────
    // Escenario 1: GET /api/productos → 200 OK con lista de productos
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/productos - retorna 200 y la lista de todos los productos")
    void listarProductos_retorna200ConLista() throws Exception {
        // Dado
        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(productoRepository.findAll()).thenReturn(productos);

        // Cuando / Entonces
        mockMvc.perform(get("/api/productos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Laptop"))
            .andExpect(jsonPath("$[0].precio").value(1500.00))
            .andExpect(jsonPath("$[1].nombre").value("Monitor"));
    }

    // ─────────────────────────────────────────────────────────────
    // Escenario 2: GET /api/productos — lista vacía → 200 OK con []
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/productos - retorna 200 con lista vacía cuando no hay productos")
    void listarProductos_sinProductos_retorna200YListaVacia() throws Exception {
        // Dado
        when(productoRepository.findAll()).thenReturn(List.of());

        // Cuando / Entonces
        mockMvc.perform(get("/api/productos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    // ─────────────────────────────────────────────────────────────
    // Escenario 3: POST /api/productos → 201 Created con datos válidos
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/productos - retorna 201 y el producto creado con datos válidos")
    void guardarProducto_conDatosValidos_retorna201YProductoGuardado() throws Exception {
        // Dado
        Producto nuevo    = new Producto(null, "Laptop", 1500.00, 10);
        Producto guardado = new Producto(1L,   "Laptop", 1500.00, 10);
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        // Cuando / Entonces
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Laptop"))
            .andExpect(jsonPath("$.precio").value(1500.00))
            .andExpect(jsonPath("$.stock").value(10));
    }

    // ─────────────────────────────────────────────────────────────
    // Escenario 4: POST /api/productos — precio cero → 201 (borde)
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/productos - acepta producto con precio 0.0 (caso de borde)")
    void guardarProducto_conPrecioCero_retorna201() throws Exception {
        // Dado
        Producto conPrecioCero  = new Producto(null, "Muestra Gratis", 0.0, 5);
        Producto guardado       = new Producto(3L,   "Muestra Gratis", 0.0, 5);
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        // Cuando / Entonces
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conPrecioCero)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.precio").value(0.0));
    }

    // ─────────────────────────────────────────────────────────────
    // Escenario 5: POST /api/productos — body vacío → 400 Bad Request
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/productos - retorna 400 cuando el body no es JSON válido")
    void guardarProducto_conBodyInvalido_retorna400() throws Exception {
        // Cuando / Entonces
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("esto no es json"))
            .andExpect(status().isBadRequest());
    }

    // ─────────────────────────────────────────────────────────────
    // Escenario 6: POST /api/productos — stock null → 201 (stock es opcional)
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/productos - acepta producto sin stock (campo nullable)")
    void guardarProducto_sinStock_retorna201() throws Exception {
        // Dado — stock es @Column sin nullable=false, entonces puede ser null
        Producto sinStock = new Producto(null, "Servicio", 99.99, null);
        Producto guardado = new Producto(4L,   "Servicio", 99.99, null);
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        // Cuando / Entonces
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sinStock)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(4))
            .andExpect(jsonPath("$.nombre").value("Servicio"));
    }
}
