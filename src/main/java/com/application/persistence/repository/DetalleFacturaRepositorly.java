package com.application.persistence.repository;

import com.application.persistence.entity.factura.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleFacturaRepositorly extends JpaRepository<DetalleFactura, Long> {
}
