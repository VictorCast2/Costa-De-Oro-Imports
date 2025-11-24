package com.application.presentation.controller.admin;

import com.application.presentation.dto.reporte.CuentasPorPagar.response.CuentasPagarResponse;
import com.application.presentation.dto.reporte.CuentasPorPagar.response.TotalGeneralCuentasPagarResponse;
import com.application.presentation.dto.reporte.RentabilidadPorProducto.response.RentabilidadProductoResponse;
import com.application.presentation.dto.reporte.RentabilidadPorProducto.response.TotalGeneralRentabilidadResponse;
import com.application.presentation.dto.reporte.ReporteCompra.response.ReporteCompraResponse;
import com.application.presentation.dto.reporte.ReporteCompra.response.TotalGeneralComprasResponse;
import com.application.presentation.dto.reporte.VentasPorClienteProducto.response.VentasClienteProductoResponse;
import com.application.presentation.dto.reporte.ventasPorCliente.response.TotalGeneralResponse;
import com.application.presentation.dto.reporte.ventasPorCliente.response.VentasClienteResponse;
import com.application.presentation.dto.reporte.ventasPorProducto.response.VentasProductoResponse;
import com.application.service.implementation.GenerarReporteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GenerarReporteRestController {

    private final GenerarReporteServiceImpl generarReporteService;

    @GetMapping("/ventas-clientes")
    public ResponseEntity<Map<String, Object>> getVentasPorClientes() {
        List<VentasClienteResponse> ventas = generarReporteService.obtenerVentasPorClientes();
        TotalGeneralResponse totalGeneral = generarReporteService.obtenerTotalGeneralVentas();

        Map<String, Object> response = new HashMap<>();
        response.put("ventasClientes", ventas);
        response.put("totalGeneral", totalGeneral);
        response.put("anio", java.time.Year.now().getValue());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ventas-producto")
    public ResponseEntity<Map<String, Object>> getVentasPorProducto() {
        List<VentasProductoResponse> ventas = generarReporteService.obtenerVentasPorProducto();
        TotalGeneralResponse totalGeneral = generarReporteService.obtenerTotalGeneralVentasProducto();

        Map<String, Object> response = new HashMap<>();
        response.put("ventasProducto", ventas);
        response.put("totalGeneral", totalGeneral);
        response.put("anio", java.time.Year.now().getValue());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ventas-cliente-producto")
    public ResponseEntity<Map<String, Object>> getVentasPorClienteProducto() {
        List<VentasClienteProductoResponse> ventas = generarReporteService.obtenerVentasPorClienteProducto();
        TotalGeneralResponse totalGeneral = generarReporteService.obtenerTotalGeneralVentasClienteProducto();

        Map<String, Object> response = new HashMap<>();
        response.put("ventasClienteProducto", ventas);
        response.put("totalGeneral", totalGeneral);
        response.put("anio", java.time.Year.now().getValue());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rentabilidad-producto")
    public ResponseEntity<Map<String, Object>> getRentabilidadPorProducto() {
        List<RentabilidadProductoResponse> rentabilidad = generarReporteService.obtenerRentabilidadPorProducto();
        TotalGeneralRentabilidadResponse totalGeneral = generarReporteService.obtenerTotalGeneralRentabilidad();

        Map<String, Object> response = new HashMap<>();
        response.put("rentabilidadProducto", rentabilidad);
        response.put("totalGeneral", totalGeneral);
        response.put("anio", java.time.Year.now().getValue());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cuentas-pagar")
    public ResponseEntity<Map<String, Object>> getCuentasPorPagar() {
        List<CuentasPagarResponse> cuentasPagar = generarReporteService.obtenerCuentasPorPagar();
        TotalGeneralCuentasPagarResponse totalGeneral = generarReporteService.obtenerTotalGeneralCuentasPagar();

        Map<String, Object> response = new HashMap<>();
        response.put("cuentasPagar", cuentasPagar);
        response.put("totalGeneral", totalGeneral);
        response.put("anio", java.time.Year.now().getValue());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reporte-compras")
    public ResponseEntity<Map<String, Object>> getReporteCompras() {
        List<ReporteCompraResponse> reporteCompras = generarReporteService.obtenerReporteCompras();
        TotalGeneralComprasResponse totalGeneral = generarReporteService.obtenerTotalGeneralCompras();

        Map<String, Object> response = new HashMap<>();
        response.put("reporteCompras", reporteCompras);
        response.put("totalGeneral", totalGeneral);
        response.put("anio", java.time.Year.now().getValue());

        return ResponseEntity.ok(response);
    }
}
