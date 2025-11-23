package com.application.service.implementation;

import com.application.presentation.dto.venta.request.VentaRequest;
import com.application.service.interfaces.PrediccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.classifiers.Classifier;
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

        DenseInstance instancia = new DenseInstance(estructura.numAttributes());
        instancia.setDataset(estructura);

        // 🎯 NUEVO → AÑO
        if (estructura.attribute("anoo") != null) {
            instancia.setValue(estructura.attribute("anoo"), request.anio());
        }

        instancia.setValue(estructura.attribute("mes"), request.mes());
        instancia.setValue(estructura.attribute("cantidad_productos"), request.cantidadProductos());
        instancia.setValue(estructura.attribute("total_unidades"), request.totalUnidades());
        instancia.setValue(estructura.attribute("precio_promedio"), request.precioPromedio());

        return modelo.classifyInstance(instancia);
    }

}