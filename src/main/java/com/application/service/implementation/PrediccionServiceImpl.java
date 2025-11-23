package com.application.service.implementation;

import com.application.presentation.dto.venta.request.VentaRequest;
import com.application.service.interfaces.PrediccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class PrediccionServiceImpl implements PrediccionService {

    private final Classifier modelo;
    private final Instances estructura;

    public PrediccionServiceImpl() throws Exception {

        // Cargar modelo WEKA
        modelo = (Classifier) SerializationHelper.read(
                getClass().getResourceAsStream("/modelo/costa_de_oro_model.model")
        );

        // Cargar estructura del dataset ARFF
        estructura = new Instances(
                new BufferedReader(new InputStreamReader(
                        getClass().getResourceAsStream("/modelo/costa_de_oro_weka.arff")
                ))
        );

        estructura.setClassIndex(0); // venta_total
    }

    @Override
    public double predecir(VentaRequest request) throws Exception {

        if (modelo == null || estructura == null) {
            throw new IllegalStateException("Modelo de predicción no disponible");
        }

        // Validar datos de entrada
        if (request.anno() < 2020 || request.anno() > 2030) {
            throw new IllegalArgumentException("Año fuera del rango válido");
        }

        if (request.mes() < 1 || request.mes() > 12) {
            throw new IllegalArgumentException("Mes fuera del rango válido");
        }

        // Crear una instancia siguiendo la estructura ARFF
        Instance instancia = new DenseInstance(estructura.numAttributes());
        instancia.setDataset(estructura);

        // Orden del ARFF:
        // 0 → venta_total (class) → missing
        // 1 → anno
        // 2 → mes
        // 3 → cantidad_productos
        // 4 → total_unidades
        // 5 → precio_promedio

        instancia.setMissing(0);

        instancia.setValue(1, request.anno());
        instancia.setValue(2, request.mes());
        instancia.setValue(3, request.cantidadProductos());
        instancia.setValue(4, request.totalUnidades());
        instancia.setValue(5, request.precioPromedio());

        // Predicción
        return modelo.classifyInstance(instancia);
    }

}