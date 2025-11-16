package com.application.persistence.repository;

import com.application.persistence.entity.factura.FacturaProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaProveedorRepository extends JpaRepository<FacturaProveedor, Long> {
}
