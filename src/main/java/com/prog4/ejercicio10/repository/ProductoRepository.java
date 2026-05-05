package com.prog4.ejercicio10.repository;

import com.prog4.ejercicio10.entity.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// CrudRepository<Entidad, TipoDeLaClavePrimaria>
// Provee automaticamente: save(), findAll(), findById(), deleteById(), etc.
@Repository
public interface ProductoRepository extends CrudRepository<Producto, Long> {
    // No se necesita implementacion: Spring Data JPA la genera automaticamente
}
