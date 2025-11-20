package com.application.persistence.repository;

import com.application.persistence.entity.pqrs.Peticion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeticionRepository extends JpaRepository<Peticion, Long> {
    List<Peticion> findByActivoTrueOrderByFechaDesc();
}
