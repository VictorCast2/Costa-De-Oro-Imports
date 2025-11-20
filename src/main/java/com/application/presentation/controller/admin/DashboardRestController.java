package com.application.presentation.controller.admin;

import com.application.presentation.dto.grafica.GraficaIngresosGastosResponse;
import com.application.presentation.dto.grafica.columnasApiladas.StockComprasResponse;
import com.application.presentation.dto.grafica.historicoVentas.HistoricoVentasResponse;
import com.application.presentation.dto.grafica.ventasCiudades.MapaVentasResponse;
import com.application.presentation.dto.grafica.ventasTotales.VentasTotalesResponse;
import com.application.service.interfaces.GraficaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graficas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardRestController {

    private final GraficaService graficaService;

    @GetMapping("/ingresos-gastos")
    public ResponseEntity<GraficaIngresosGastosResponse> obtenerDatosGraficaIngresosGastos() {
        try {
            GraficaIngresosGastosResponse datos = graficaService.obtenerDatosGraficaIngresosGastos();
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/ventas-totales")
    public ResponseEntity<VentasTotalesResponse> obtenerVentasTotales() {
        try {
            VentasTotalesResponse datos = graficaService.obtenerVentasTotalesUltimos12Meses();
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stock-compras")
    public ResponseEntity<StockComprasResponse> obtenerDatosStockCompras() {
        try {
            StockComprasResponse datos = graficaService.obtenerDatosStockCompras();
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/mapa-ventas")
    public ResponseEntity<MapaVentasResponse> obtenerDatosMapaVentas() {
        try {
            MapaVentasResponse datos = graficaService.obtenerVentasParaMapa();
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/historico-ventas")
    public ResponseEntity<HistoricoVentasResponse> obtenerHistoricoVentas() {
        try {
            HistoricoVentasResponse datos = graficaService.obtenerHistoricoVentas();
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
