package com.prog4.ejercicio10.controller;

import com.prog4.ejercicio10.entity.Producto;
import com.prog4.ejercicio10.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    // Inyeccion de dependencia del repositorio
    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * POST /api/productos
     * Recibe un objeto Producto en el body (JSON) y lo guarda en la base de datos.
     *
     * Ejemplo de body:
     * {
     *   "nombre": "Laptop",
     *   "precio": 1500.00,
     *   "stock": 10
     * }
     */
    @PostMapping
    public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto) {
        Producto guardado = productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    /**
     * GET /api/productos
     * Devuelve la lista de todos los productos guardados en la base de datos.
     */
    @GetMapping
    public ResponseEntity<Iterable<Producto>> listarProductos() {
        Iterable<Producto> productos = productoRepository.findAll();
        return ResponseEntity.ok(productos);
    }
}
