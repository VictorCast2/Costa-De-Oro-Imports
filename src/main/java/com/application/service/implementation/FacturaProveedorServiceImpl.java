package com.application.service.implementation;

import com.application.persistence.entity.factura.DetalleFactura;
import com.application.persistence.entity.factura.FacturaProveedor;
import com.application.persistence.entity.producto.Producto;
import com.application.persistence.entity.usuario.Usuario;
import com.application.persistence.repository.DetalleFacturaRepositorly;
import com.application.persistence.repository.FacturaProveedorRepository;
import com.application.persistence.repository.ProductoRepository;
import com.application.persistence.repository.UsuarioRepository;
import com.application.presentation.dto.factura.request.DetalleFacturaRequest;
import com.application.presentation.dto.factura.request.FacturaProveedorRequest;
import com.application.presentation.dto.factura.response.FacturaProductoProveedorResponse;
import com.application.presentation.dto.general.response.BaseResponse;
import com.application.service.interfaces.FacturaProveedorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FacturaProveedorServiceImpl implements FacturaProveedorService {

    private final FacturaProveedorRepository facturaProveedorRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    // Obtener productos del proveedor para el select
    @Override
    public List<FacturaProductoProveedorResponse> getProductosByProveedorId(Long proveedorId) {
        return productoRepository.findProductosActivosByProveedorId(proveedorId);
    }

    // Crear factura de compra
    @Override
    public BaseResponse crearFacturaCompra(FacturaProveedorRequest request) {
        try {
            // Validar que el número de factura no exista
            if (facturaProveedorRepository.existsByNumeroFactura(request.numeroFactura())) {
                return new BaseResponse("Ya existe una factura con el número: " + request.numeroFactura(), false);
            }

            // Obtener el proveedor
            Usuario proveedor = usuarioRepository.findById(request.proveedorId())
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + request.proveedorId()));

            // Crear la factura
            FacturaProveedor factura = FacturaProveedor.builder()
                    .numeroFactura(request.numeroFactura())
                    .fechaEmision(request.fechaEmision())
                    .fechaRegistro(LocalDateTime.now())
                    .total(request.totalCompra())
                    .activo(true)
                    .usuario(proveedor)
                    .build();

            // Procesar los detalles de la compra
            for (DetalleFacturaRequest detalleRequest : request.detalles()) {
                // Validar que el producto pertenezca al proveedor
                Producto producto = productoRepository
                        .findByIdAndProveedorId(detalleRequest.productoId(), request.proveedorId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Producto no encontrado o no pertenece al proveedor: " + detalleRequest.productoId()));

                // Crear detalle de factura
                DetalleFactura detalleFactura = DetalleFactura.builder()
                        .cantidad(detalleRequest.cantidad())
                        .precioCompra(detalleRequest.precioCompra())
                        .subtotal(detalleRequest.subtotal())
                        .producto(producto)
                        .factura(factura)
                        .build();

                // Agregar detalle a la factura
                factura.addDetalleFactura(detalleFactura);

                // Actualizar stock del producto
                producto.setStock(producto.getStock() + detalleRequest.cantidad());
                productoRepository.save(producto);
            }

            // Guardar la factura (cascade guardará los detalles)
            facturaProveedorRepository.save(factura);

            return new BaseResponse("Factura de compra registrada exitosamente", true);

        } catch (EntityNotFoundException e) {
            return new BaseResponse(e.getMessage(), false);
        } catch (Exception e) {
            return new BaseResponse("Error al registrar la factura: " + e.getMessage(), false);
        }
    }

    // Obtener facturas por proveedor
    @Override
    public List<FacturaProveedor> getFacturasByProveedorId(Long proveedorId) {
        return facturaProveedorRepository.findByUsuarioUsuarioIdOrderByFechaRegistroDesc(proveedorId);
    }
}
