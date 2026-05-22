# Ejercicio 11 — Testing Unitario en Spring Boot con JUnit 5 y Mockito

**Entidad:** `Producto`  
**Tecnologías:** JUnit 5, Mockito, MockMvc, H2 (base de datos en memoria para tests)

---

## Parte 1 — Escenarios de Prueba (Gherkin)

### Escenario 1 — GET todos los productos (Happy Path)
> Dado que existen productos en el sistema, cuando se hace GET /api/productos, entonces se retorna 200 y la lista completa.

### Escenario 2 — GET sin productos registrados (Happy Path / Borde)
> Dado que no hay productos en el sistema, cuando se hace GET /api/productos, entonces se retorna 200 con una lista vacía.

### Escenario 3 — POST producto válido (Happy Path)
> Dado datos válidos (nombre, precio, stock), cuando se hace POST /api/productos, entonces se retorna 201 y el producto con su ID asignado.

### Escenario 4 — POST producto con precio cero (Caso de Borde)
> Dado un producto con precio 0.0, cuando se hace POST /api/productos, entonces se retorna 201 ya que stock es nullable.

### Escenario 5 — POST con body inválido (Negative Path)
> Dado un body que no es JSON válido, cuando se hace POST /api/productos, entonces se retorna 400 Bad Request.

### Escenario 6 — POST sin stock (Caso de Borde)
> Dado un producto sin campo stock (campo nullable), cuando se hace POST /api/productos, entonces se retorna 201.

---

## Estructura de tests

```
src/test/
├── resources/
│   └── application.properties        ← H2 en memoria (reemplaza PostgreSQL en tests)
└── java/com/prog4/ejercicio10/
    ├── Ejercicio10ApplicationTests.java
    └── controller/
        └── ProductoControllerTest.java  ← 6 tests con @WebMvcTest + MockMvc
```

## Cómo correr los tests

```bash
./mvnw test
```

No necesitás tener PostgreSQL corriendo. Los tests usan H2 en memoria automáticamente.
# EJERCICIO-11
