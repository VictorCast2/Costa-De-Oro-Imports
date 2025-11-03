package com.application.service.implementation.compra;

import com.application.configuration.custom.CustomUserPrincipal;
import com.application.persistence.entity.compra.Compra;
import com.application.persistence.entity.compra.DetalleVenta;
import com.application.persistence.entity.compra.enums.EEstado;
import com.application.persistence.entity.producto.Producto;
import com.application.persistence.entity.usuario.Usuario;
import com.application.persistence.repository.CompraRepository;
import com.application.persistence.repository.DetalleVentaRepository;
import com.application.persistence.repository.ProductoRepository;
import com.application.persistence.repository.UsuarioRepository;
import com.application.presentation.dto.compra.request.CompraCreateRequest;
import com.application.presentation.dto.compra.request.DetalleVentaRequest;
import com.application.presentation.dto.compra.response.CompraResponse;
import com.application.presentation.dto.compra.response.DetalleVentaResponse;
import com.application.service.interfaces.CompraService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    /**
     * @param compraId
     * @return
     */
    @Override
    public Compra getCompraById(Long compraId) {
        return compraRepository.findById(compraId)
                .orElseThrow(() -> new EntityNotFoundException("La compra con id: " + compraId + " no existe."));
    }

    /**
     * @param compraRequest
     * @return
     */
    @Override
    @Transactional
    public CompraResponse createCompra(CustomUserPrincipal principal, CompraCreateRequest compraRequest) {

        String correo = principal.getCorreo();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("ERROR: el correo '" + correo + "' no existe"));

        Compra compra = new Compra();

        double subtotal = 0;

        for (DetalleVentaRequest detalleVentaRequest : compraRequest.detalleVentaRequests()) {

            Long productoId = detalleVentaRequest.productoId();
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new EntityNotFoundException("Error: El producto con id: " + productoId + " no existe"));

            if (producto.getStock() < detalleVentaRequest.cantidad()) {
                throw new IllegalArgumentException("No hay stock suficiente del producto " + producto.getNombre());
            }

            int sub = (int) (producto.getPrecio() * detalleVentaRequest.cantidad());
            subtotal += sub;

            DetalleVenta detalleVenta = DetalleVenta.builder()
                    .precioUnitario((int) producto.getPrecio())
                    .cantidad(detalleVentaRequest.cantidad())
                    .subtotal(sub)
                    .build();

            producto.setStock(producto.getStock() - detalleVentaRequest.cantidad());

            producto.addDetalleVenta(detalleVenta);
            compra.addDetalleVenta(detalleVenta);

            productoRepository.save(producto);
        }

        double iva = subtotal * 0.19;
        double total = subtotal + iva;

        compra.setEMetodoPago(compraRequest.metodoPago());
        compra.setSubtotal(subtotal);
        compra.setCuponDescuento(compraRequest.cuponDescuento());
        compra.setIva(iva);
        compra.setTotal(total);
        compra.setFecha(LocalDateTime.now());
        compra.setEEstado(EEstado.PENDIENTE);

        usuario.addCompra(compra);

        usuarioRepository.save(usuario);
        compraRepository.save(compra);

        return this.toResponse(compra);
    }

    /**
     * @param compraId
     * @param estado
     */
    @Override
    public void updateEstadoCompra(Long compraId, EEstado estado) {
        Compra compra = this.getCompraById(compraId);
        compra.setEEstado(estado);
        compraRepository.save(compra);
    }

    /**
     * @param compra
     * @return
     */
    @Override
    public CompraResponse toResponse(Compra compra) {
        return new CompraResponse(
                compra.getCompraId(),
                compra.getUsuario().getUsuarioId(),
                compra.getEMetodoPago(),
                compra.getSubtotal(),
                compra.getCuponDescuento(),
                compra.getIva(),
                compra.getTotal(),
                compra.getFecha(),
                compra.getEEstado(),
                compra.getDetalleVentas().stream()
                        .map(detalleVenta ->
                                new DetalleVentaResponse(
                                        detalleVenta.getDetalleVentaId(),
                                        detalleVenta.getProducto().getProductoId(),
                                        detalleVenta.getPrecioUnitario(),
                                        detalleVenta.getCantidad(),
                                        detalleVenta.getSubtotal()
                                )
                        )
                        .toList()
        );
    }
}
