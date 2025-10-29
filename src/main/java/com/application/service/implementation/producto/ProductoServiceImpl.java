package com.application.service.implementation.producto;

import com.application.persistence.entity.categoria.Categoria;
import com.application.persistence.entity.categoria.SubCategoria;
import com.application.persistence.entity.producto.Producto;
import com.application.persistence.entity.producto.enums.ETipo;
import com.application.persistence.repository.CategoriaRepository;
import com.application.persistence.repository.ProductoRepository;
import com.application.persistence.repository.SubCategoriaRepository;
import com.application.presentation.dto.general.response.GeneralResponse;
import com.application.presentation.dto.producto.request.ProductoCreateRequest;
import com.application.presentation.dto.producto.response.ProductoResponse;
import com.application.service.interfaces.ImagenService;
import com.application.service.interfaces.producto.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SubCategoriaRepository subCategoriaRepository;
    private final ImagenService imagenService;

    /**
     * Obtiene un producto por su ID.
     * Este método es de uso interno para otros métodos del servicio.
     *
     * @param id ID del producto a buscar
     * @return La entidad producto encontrada
     * @throws EntityNotFoundException si la producto no existe
     */
    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error: El producto con id: " + id + " no existe"));
    }

    /**
     * Obtiene un producto como DTO de respuesta por su ID.
     * Este método es para la presentación del producto en la pagina de
     * Producto-Descripción
     *
     * @param id ID del producto a buscar
     * @return DTO con la información del producto, incluyendo categorías.
     */
    @Override
    public ProductoResponse getProductoResponseById(Long id) {
        Producto producto = this.getProductoById(id);
        return this.toResponse(producto);
    }

    /**
     * Obtiene todos los productos registrados (activos e inactivos).
     * Uso común para el panel administrativo.
     *
     * @return Lista de DTOs con datos básicos de cada producto
     */
    @Override
    public List<ProductoResponse> getProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productos.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Obtiene todos los productos que están activos (disponibles para la venta).
     * Este método es para la vista del E-commerce
     *
     * @return Lista de DTOs con productos activos
     */
    @Override
    public List<ProductoResponse> getProductosActivos() {
        List<Producto> productos = productoRepository.findByActivoTrue();
        return productos.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Obtiene todos los productos que están activos y sean distintos de
     * Etipo.UNIDAD,
     * Este método es para la vista de Pack para listar cajas, pack y combos
     * que estén disponibles para la venta
     *
     * @return Lista de DTOs con productos activos que no sean individuales
     */
    @Override
    public List<ProductoResponse> getPacksActivos() {
        List<Producto> productoList = productoRepository.findByeTipoNotAndActivoTrue(ETipo.UNIDAD);
        return productoList.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Obtiene los productos que pertenecen a una categoría específica.
     * Este método sera de uso común para la pagina de Productos, en el filtro
     *
     * @param id ID de la categoría
     * @return Lista de DTOs con productos que pertenecen a la categoría indicada
     */
    @Override
    public List<ProductoResponse> getProductoByCategoriaId(Long id) {
        List<Producto> productos = productoRepository.findByCategoria_CategoriaId(id);
        return productos.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Obtiene los productos más vendidos en orden descendente (más ventas a menor ventas) y con estado activo.
     * Este método sera usado en la pagina de Index.html para mostrar los productos destacados
     *
     * @return Lista de DTO con los 16 productos con mayor cantidad de ventas y con estado activo
     */
    @Override
    public List<ProductoResponse> getProductosMasVendidosActivos() {
        List<Producto> productos = productoRepository.findProductosMasVendidosActivos();
        return productos.stream()
                .map(this::toResponse)
                .limit(16)
                .toList();
    }

    /**
     * Obtiene los productos más vendidos por su categoria en orden descendente y con estado activo.
     * Este método sera usado en la pagina de Index.html para mostrar los productos según su categoriaId
     *
     * @param categoriaId ID de la categoria
     * @return Lista de DTO con 12 productos pertenecientes a la categoria indicada, con mayor ventas y estado activo
     */
    @Override
    public List<ProductoResponse> getProductosMasVendidosByCategoriaIdActivos(Long categoriaId) {
        List<Producto> productos = productoRepository.findProductosMasVendidosByCategoriaIdActivos(categoriaId);
        return productos.stream()
                .map(this::toResponse)
                .limit(12)
                .toList();
    }

    /**
     * Obtiene los productos más vendidos de varias categorías en orden descendente y con estado activo.
     * Este método sera usado en la pagina de Index.html para mostrar los productos de varias categorías
     *
     * @param categoriaIds IDs de las categorías
     * @return Lista de DTO con 12 productos pertenecientes a las categorías indicadas, con mayor ventas y estado activo
     */
    @Override
    public List<ProductoResponse> getProductosMasVendidosByCategoriaIdsActivos(List<Long> categoriaIds) {
        List<Producto> productos = productoRepository.findProductosMasVendidosByCategoriaIdsActivos(categoriaIds);
        return productos.stream()
                .map(this::toResponse)
                .limit(12)
                .toList();
    }

    /**
     * Crea un nuevo producto a partir de un DTO de creación.
     *
     * @param productoRequest DTO con los datos del nuevo producto
     * @return Respuesta con mensaje de confirmación
     */
    @Override
    @Transactional
    public GeneralResponse createProducto(@NotNull ProductoCreateRequest productoRequest) {

        String imagen = this.imagenService.asignarImagen(productoRequest.imagen(), "imagen-producto");

        Producto producto = Producto.builder()
                .codigoProducto(productoRequest.codigoProducto())
                .imagen(imagen)
                .nombre(productoRequest.nombre())
                .marca(productoRequest.marca())
                .pais(productoRequest.pais())
                .eTipo(productoRequest.tipo())
                .precio(productoRequest.precio())
                .stock(productoRequest.stock())
                .descripcion(productoRequest.descripcion())
                .activo(productoRequest.activo())
                .build();

        Long categoriaId = productoRequest.categoriaId();
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("La categoria con id: " + categoriaId + " no exite"));
        categoria.addProducto(producto);

        Long subCategoriaId = productoRequest.subCategoriaId();
        SubCategoria subCategoria = subCategoriaRepository.findById(subCategoriaId)
                        .orElseThrow(() -> new EntityNotFoundException("La subcategoria con id: " + subCategoriaId + " no existe"));

        if (!subCategoria.getCategoria().getCategoriaId().equals(categoriaId)) {
            throw new IllegalArgumentException("La subcategoria seleccionada no pertenece a la categoría " + categoria.getNombre());
        }

        subCategoria.addProducto(producto);

        productoRepository.save(producto);

        return new GeneralResponse("Producto creado exitosamente");
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param productoRequest DTO con los datos actualizados
     * @param id              ID del producto a actualizar
     * @return Respuesta con mensaje de confirmación
     * @throws EntityNotFoundException si el producto no existe
     */
    @Override
    @Transactional
    public GeneralResponse updateProducto(@NotNull ProductoCreateRequest productoRequest, Long id) {

        Producto producto = this.getProductoById(id);

        String imagen = this.imagenService.asignarImagen(productoRequest.imagen(), "imagen-producto");

        producto.setCodigoProducto(productoRequest.codigoProducto());
        producto.setImagen(imagen);
        producto.setNombre(productoRequest.nombre());
        producto.setMarca(productoRequest.marca());
        producto.setPais(productoRequest.pais());
        producto.setETipo(productoRequest.tipo());
        producto.setPrecio(productoRequest.precio());
        producto.setStock(productoRequest.stock());
        producto.setDescripcion(productoRequest.descripcion());
        producto.setActivo(productoRequest.activo());

        producto.getCategoria().deleteProducto(producto);
        producto.getSubCategoria().deleteProducto(producto);

        Long categoriaId = productoRequest.categoriaId();
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("La categoria con id: " + categoriaId + " no exite"));
        categoria.addProducto(producto);

        Long subCategoriaId = productoRequest.subCategoriaId();
        SubCategoria subCategoria = subCategoriaRepository.findById(subCategoriaId)
                .orElseThrow(() -> new EntityNotFoundException("La subcategoria con id: " + subCategoriaId + " no existe"));

        if (!subCategoria.getCategoria().getCategoriaId().equals(categoriaId)) {
            throw new IllegalArgumentException("La subcategoria seleccionada no pertenece a la categoría " + categoria.getNombre());
        }

        subCategoria.addProducto(producto);

        productoRepository.save(producto);

        return new GeneralResponse("Producto actualizado exitosamente");
    }

    /**
     * Cambia el estado del producto.
     *
     * @param id ID del producto a cambiar su estado
     * @return DTO con mensaje de confirmación según el estado del producto
     * @throws EntityNotFoundException si el producto no existe
     */
    @Override
    public GeneralResponse changeEstadoProducto(Long id) {
        Producto producto = this.getProductoById(id);

        boolean nuevoEstado = !producto.isActivo();
        producto.setActivo(nuevoEstado);
        productoRepository.save(producto);

        String mensaje = nuevoEstado
                ? "Producto Habilitado exitosamente"
                : "Producto deshabilitado exitosamente";

        return new GeneralResponse(mensaje);
    }

    /**
     * Elimina un producto y rompe sus relaciones con categoría y subcategoria para evitar
     * errores de integridad referencial en la base de datos.
     *
     * @param id ID del producto a eliminar
     * @return Respuesta con mensaje de confirmación
     * @throws EntityNotFoundException si el producto no existe
     */
    @Override
    @Transactional
    public GeneralResponse deleteProducto(Long id) {

        Producto producto = this.getProductoById(id);

        producto.getCategoria().deleteProducto(producto);
        producto.getSubCategoria().deleteProducto(producto);

        productoRepository.delete(producto);
        return new GeneralResponse("Producto eliminado exitosamente");
    }

    /**
     * Convierte una entidad Producto a su DTO de respuesta, incluyendo
     * categoria y subcategoria.
     * Para uso interno del servicio en los métodos de búsqueda
     *
     * @param producto Entidad producto a convertir
     * @return DTO con la información completa del producto
     */
    @Override
    public ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getProductoId(),
                producto.getCodigoProducto(),
                producto.getImagen(),
                producto.getNombre(),
                producto.getMarca(),
                producto.getPais(),
                producto.getETipo(),
                producto.getPrecio(),
                producto.getPrecioRegular(),
                producto.getStock(),
                producto.getDescripcion(),
                producto.isActivo(),
                producto.getCategoria().getNombre(),
                producto.getSubCategoria() != null ? producto.getSubCategoria().getNombre() : "Sin subcategoría"
        );
    }
}