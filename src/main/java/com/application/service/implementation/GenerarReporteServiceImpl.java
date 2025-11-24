package com.application.service.implementation;

import com.application.persistence.repository.ReportesNativeRepository;
import com.application.presentation.dto.reporte.CuentasPorPagar.projection.CuentasPagarProjection;
import com.application.presentation.dto.reporte.CuentasPorPagar.response.CuentasPagarResponse;
import com.application.presentation.dto.reporte.CuentasPorPagar.response.TotalGeneralCuentasPagarResponse;
import com.application.presentation.dto.reporte.RentabilidadPorProducto.projection.RentabilidadProductoProjection;
import com.application.presentation.dto.reporte.RentabilidadPorProducto.response.RentabilidadProductoResponse;
import com.application.presentation.dto.reporte.RentabilidadPorProducto.response.TotalGeneralRentabilidadResponse;
import com.application.presentation.dto.reporte.ReporteCompra.projection.ReporteCompraProjection;
import com.application.presentation.dto.reporte.ReporteCompra.response.ReporteCompraResponse;
import com.application.presentation.dto.reporte.ReporteCompra.response.TotalGeneralComprasResponse;
import com.application.presentation.dto.reporte.VentasPorClienteProducto.projection.VentasClienteProductoProjection;
import com.application.presentation.dto.reporte.VentasPorClienteProducto.response.VentasClienteProductoResponse;
import com.application.presentation.dto.reporte.ventasPorCliente.projection.VentasClienteProjection;
import com.application.presentation.dto.reporte.ventasPorCliente.response.TotalGeneralResponse;
import com.application.presentation.dto.reporte.ventasPorCliente.response.VentasClienteResponse;
import com.application.presentation.dto.reporte.ventasPorProducto.projection.VentasProductoProjection;
import com.application.presentation.dto.reporte.ventasPorProducto.response.VentasProductoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenerarReporteServiceImpl {

    private final ReportesNativeRepository reportesNativeRepository;

    public List<VentasClienteResponse> obtenerVentasPorClientes() {
        int anioActual = Year.now().getValue();

        List<VentasClienteProjection> projections =
                reportesNativeRepository.findVentasPorClientes(anioActual);

        return projections.stream()
                .map(proj -> new VentasClienteResponse(
                        proj.getIdentificacion(),
                        proj.getCliente(),
                        proj.getNumeroComprobantes(),
                        proj.getValorBruto(),
                        proj.getImpuestoCargo(),
                        proj.getTotal()
                ))
                .toList();
    }

    public TotalGeneralResponse obtenerTotalGeneralVentas() {
        int anioActual = Year.now().getValue();

        Object[] totales = reportesNativeRepository.findTotalesGeneralesVentas(anioActual);

        if (totales != null && totales.length >= 3) {
            BigDecimal valorBrutoTotal = totales[0] != null ?
                    new BigDecimal(totales[0].toString()) : BigDecimal.ZERO;
            BigDecimal impuestoCargoTotal = totales[1] != null ?
                    new BigDecimal(totales[1].toString()) : BigDecimal.ZERO;
            BigDecimal totalGeneral = totales[2] != null ?
                    new BigDecimal(totales[2].toString()) : BigDecimal.ZERO;

            return new TotalGeneralResponse(valorBrutoTotal, impuestoCargoTotal, totalGeneral);
        }

        return new TotalGeneralResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public List<VentasProductoResponse> obtenerVentasPorProducto() {
        int anioActual = Year.now().getValue();

        List<VentasProductoProjection> projections =
                reportesNativeRepository.findVentasPorProducto(anioActual);

        return projections.stream()
                .map(proj -> new VentasProductoResponse(
                        proj.getCodigoProducto(),
                        proj.getNombreProducto(),
                        proj.getReferenciaFabrica(),
                        proj.getCantidadVendida(),
                        proj.getValorBruto(),
                        proj.getImpuestoCargo(),
                        proj.getTotal()
                ))
                .toList();
    }

    public TotalGeneralResponse obtenerTotalGeneralVentasProducto() {
        int anioActual = Year.now().getValue();

        Object[] totales = reportesNativeRepository.findTotalesGeneralesVentasProducto(anioActual);

        if (totales != null && totales.length >= 3) {
            BigDecimal valorBrutoTotal = totales[0] != null ?
                    new BigDecimal(totales[0].toString()) : BigDecimal.ZERO;
            BigDecimal impuestoCargoTotal = totales[1] != null ?
                    new BigDecimal(totales[1].toString()) : BigDecimal.ZERO;
            BigDecimal totalGeneral = totales[2] != null ?
                    new BigDecimal(totales[2].toString()) : BigDecimal.ZERO;

            return new TotalGeneralResponse(valorBrutoTotal, impuestoCargoTotal, totalGeneral);
        }

        return new TotalGeneralResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public List<VentasClienteProductoResponse> obtenerVentasPorClienteProducto() {
        int anioActual = Year.now().getValue();

        List<VentasClienteProductoProjection> projections =
                reportesNativeRepository.findVentasPorClienteProducto(anioActual);

        return projections.stream()
                .map(proj -> new VentasClienteProductoResponse(
                        proj.getIdentificacion(),
                        proj.getCliente(),
                        proj.getCodigoProducto(),
                        proj.getNombreProducto(),
                        proj.getReferenciaFabrica(),
                        proj.getCantidadVendida(),
                        proj.getValorBruto(),
                        proj.getImpuestoCargo(),
                        proj.getTotal()
                ))
                .toList();
    }

    public TotalGeneralResponse obtenerTotalGeneralVentasClienteProducto() {
        int anioActual = Year.now().getValue();

        Object[] totales = reportesNativeRepository.findTotalesGeneralesVentasClienteProducto(anioActual);

        if (totales != null && totales.length >= 3) {
            BigDecimal valorBrutoTotal = totales[0] != null ?
                    new BigDecimal(totales[0].toString()) : BigDecimal.ZERO;
            BigDecimal impuestoCargoTotal = totales[1] != null ?
                    new BigDecimal(totales[1].toString()) : BigDecimal.ZERO;
            BigDecimal totalGeneral = totales[2] != null ?
                    new BigDecimal(totales[2].toString()) : BigDecimal.ZERO;

            return new TotalGeneralResponse(valorBrutoTotal, impuestoCargoTotal, totalGeneral);
        }

        return new TotalGeneralResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public List<RentabilidadProductoResponse> obtenerRentabilidadPorProducto() {
        int anioActual = Year.now().getValue();

        List<RentabilidadProductoProjection> projections =
                reportesNativeRepository.findRentabilidadPorProducto(anioActual);

        return projections.stream()
                .map(proj -> new RentabilidadProductoResponse(
                        proj.getTipo(),
                        proj.getCodigoProducto(),
                        proj.getNombreProducto(),
                        proj.getReferenciaFabrica(),
                        proj.getCategoria(),
                        proj.getCantidadVendida(),
                        proj.getVentasBrutasTotales(),
                        proj.getCostosTotales(),
                        proj.getUtilidadPesos(),
                        proj.getPorcentajeUtilidad(),
                        proj.getPorcentajeRentabilidad()
                ))
                .toList();
    }

    public TotalGeneralRentabilidadResponse obtenerTotalGeneralRentabilidad() {
        int anioActual = Year.now().getValue();

        Object[] totales = reportesNativeRepository.findTotalesGeneralesRentabilidad(anioActual);

        if (totales != null && totales.length >= 2) {
            BigDecimal ventasBrutasTotales = totales[0] != null ?
                    new BigDecimal(totales[0].toString()) : BigDecimal.ZERO;
            BigDecimal costosTotales = totales[1] != null ?
                    new BigDecimal(totales[1].toString()) : BigDecimal.ZERO;

            return new TotalGeneralRentabilidadResponse(ventasBrutasTotales, costosTotales);
        }

        return new TotalGeneralRentabilidadResponse(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public List<CuentasPagarResponse> obtenerCuentasPorPagar() {
        int anioActual = Year.now().getValue();

        List<CuentasPagarProjection> projections =
                reportesNativeRepository.findCuentasPorPagar(anioActual);

        return projections.stream()
                .map(proj -> new CuentasPagarResponse(
                        proj.getIdentificacion(),
                        proj.getProveedor(),
                        proj.getCiudad(),
                        proj.getSucursal(),
                        proj.getProductosSuministrados(),
                        proj.getFacturasRegistradas(),
                        proj.getTotalFacturado(),
                        proj.getComprasRealizadas()
                ))
                .collect(Collectors.toList());
    }

    public TotalGeneralCuentasPagarResponse obtenerTotalGeneralCuentasPagar() {
        int anioActual = Year.now().getValue();

        Object[] totales = reportesNativeRepository.findTotalesGeneralesCuentasPagar(anioActual);

        if (totales != null && totales.length >= 4) {
            Long totalProveedores = totales[0] != null ?
                    Long.valueOf(totales[0].toString()) : 0L;
            Long totalProductos = totales[1] != null ?
                    Long.valueOf(totales[1].toString()) : 0L;
            BigDecimal totalFacturadoAnual = totales[2] != null ?
                    new BigDecimal(totales[2].toString()) : BigDecimal.ZERO;
            Long totalCompras = totales[3] != null ?
                    Long.valueOf(totales[3].toString()) : 0L;

            return new TotalGeneralCuentasPagarResponse(
                    totalProveedores, totalProductos, totalFacturadoAnual, totalCompras
            );
        }

        return new TotalGeneralCuentasPagarResponse(0L, 0L, BigDecimal.ZERO, 0L);
    }

    public List<ReporteCompraResponse> obtenerReporteCompras() {
        int anioActual = Year.now().getValue();

        List<ReporteCompraProjection> projections =
                reportesNativeRepository.findReporteCompras(anioActual);

        return projections.stream()
                .map(proj -> new ReporteCompraResponse(
                        proj.getIdentificacion(),
                        proj.getCliente(),
                        proj.getCiudad(),
                        proj.getSucursal(),
                        proj.getTotalCompras(),
                        proj.getTotalSubtotal(),
                        proj.getTotalIva(),
                        proj.getTotalComprado(),
                        proj.getPromedioCompra(),
                        proj.getUltimaCompra(),
                        proj.getMetodoPagoFrecuente()
                ))
                .collect(Collectors.toList());
    }

    public TotalGeneralComprasResponse obtenerTotalGeneralCompras() {
        int anioActual = Year.now().getValue();

        Object[] totales = reportesNativeRepository.findTotalesGeneralesCompras(anioActual);

        if (totales != null && totales.length >= 6) {
            Long totalCompras = totales[0] != null ?
                    Long.valueOf(totales[0].toString()) : 0L;
            Long totalClientes = totales[1] != null ?
                    Long.valueOf(totales[1].toString()) : 0L;
            BigDecimal totalSubtotal = totales[2] != null ?
                    new BigDecimal(totales[2].toString()) : BigDecimal.ZERO;
            BigDecimal totalIva = totales[3] != null ?
                    new BigDecimal(totales[3].toString()) : BigDecimal.ZERO;
            BigDecimal totalComprado = totales[4] != null ?
                    new BigDecimal(totales[4].toString()) : BigDecimal.ZERO;
            BigDecimal promedioCompra = totales[5] != null ?
                    new BigDecimal(totales[5].toString()) : BigDecimal.ZERO;

            return new TotalGeneralComprasResponse(
                    totalCompras, totalClientes, totalSubtotal, totalIva, totalComprado, promedioCompra
            );
        }

        return new TotalGeneralComprasResponse(0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

}

