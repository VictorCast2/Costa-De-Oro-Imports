document.addEventListener("DOMContentLoaded", () => {

    // Efecto glassmorphism solo al hacer scroll
    const header = document.querySelector('.content__header');

    window.addEventListener('scroll', () => {
        if (window.scrollY > 10) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
    });

    //Menú desplegable del perfil
    const subMenu = document.getElementById("SubMenu");
    const profileImage = document.getElementById("user__admin");

    if (subMenu && profileImage) {
        profileImage.addEventListener("click", function (e) {
            e.stopPropagation(); // Evita que el click cierre el menú inmediatamente
            subMenu.classList.toggle("open__menu");
        });

        // Cerrar menú al hacer clic fuera
        document.addEventListener("click", function (e) {
            if (!subMenu.contains(e.target) && !profileImage.contains(e.target)) {
                subMenu.classList.remove("open__menu");
            }
        });
    }

    //abrir la notificaciones del admin
    const notifIcon = document.getElementById("notifIcon");
    const notifMenu = document.getElementById("notifMenu");

    notifIcon.addEventListener("click", (e) => {
        e.stopPropagation();
        notifMenu.classList.toggle("open");
    });

    // cerrar al hacer clic fuera
    document.addEventListener("click", (e) => {
        if (!notifMenu.contains(e.target) && !notifIcon.contains(e.target)) {
            notifMenu.classList.remove("open");
        }
    });

    // Variables globales
    let ventas = {};
    let chart; // Variable global para el gráfico
    let añoActual = new Date().getFullYear();
    let datosOriginales = []; // Guardar los datos originales

    // Inicializar la aplicación
    inicializarAplicacion();

    async function inicializarAplicacion() {
        await cargarDatosIniciales();
        inicializarGrafica();
    }

    async function cargarDatosIniciales() {
        try {
            const response = await fetch('/admin/prediccion/datos-grafica');
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            const data = await response.json();

            ventas = data.ventasPorAño;
            console.log('Datos cargados desde el servidor:', ventas);
        } catch (error) {
            console.error('Error cargando datos:', error);
            // Usar datos por defecto si hay error
            ventas = {
                2020: [120, 95, 140, 110, 180, 130, 200, 150, 220, 170, 260, 210],
                2021: [200, 180, 230, 190, 260, 240, 300, 250, 310, 280, 330, 290],
                2022: [150, 130, 170, 160, 210, 180, 250, 230, 270, 240, 260, 230],
                2023: [170, 160, 190, 180, 230, 200, 260, 240, 300, 270, 310, 290],
                2024: [200, 220, 210, 230, 260, 240, 300, 280, 330, 300, 350, 320],
                2025: [220, 210, 240, 200, 260, 230, 290, 250, 310, 280, 340, 300]
            };
            console.log('Usando datos simulados por fallo en la conexión');
        }
    }

    function inicializarGrafica() {
        const meses = [
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        ];

        // ========== CREAR PUNTOS MENSUALES ==========
        datosOriginales = [];
        for (const anio in ventas) {
            ventas[anio].forEach((valor, i) => {
                datosOriginales.push({
                    x: `${anio}-${String(i + 1).padStart(2, "0")}`, // Fecha real
                    y: valor,
                    mes: meses[i],
                    anio: anio
                });
            });
        }

        // ========== SUMAR TOTAL ANUAL ==========
        function totalAnual(año) {
            return ventas[año].reduce((acc, v) => acc + v, 0);
        }

        var options = {
            chart: {
                type: 'line',
                height: 450,
                zoom: { enabled: true, type: 'x' },
                toolbar: { show: false },
                foreColor: "#333",
                fontFamily: "Geist, Urbanist, sans-serif"
            },

            dataLabels: {
                enabled: false
            },

            series: [{
                name: "Ventas Totales",
                data: datosOriginales
            }],

            stroke: {
                width: 4,
                curve: "smooth",
                colors: ["#0080ff"]
            },
            markers: {
                size: 6,
                colors: ["#0080ff"],
                strokeColors: "#ffffff",
                strokeWidth: 2
            },

            fill: {
                type: "gradient",
                gradient: {
                    shade: "light",
                    type: "vertical",
                    shadeIntensity: 0.5,
                    gradientToColors: ["#0080ff"],
                    inverseColors: false,
                    opacityFrom: 0.8,
                    opacityTo: 0.2,
                    stops: [0, 90, 100]
                }
            },

            // ========== SOLO MOSTRAR AÑOS ==========
            xaxis: {
                type: "category",
                // Título debajo del eje X
                title: {
                    text: "Años",
                    offsetY: 5,
                    style: {
                        fontSize: "14px",
                        fontWeight: 600,
                        color: "#333",
                        fontFamily: "Geist, Urbanist, sans-serif"
                    }
                },
                labels: {
                    formatter: function (value, timestamp, index) {
                        if (!value) return ""; // evita errores

                        // Mostramos solo cuando es enero para no saturar
                        const mes = value.substring(5, 7);
                        const año = value.substring(0, 4);

                        if (mes === "01") return año;
                        return "";
                    }
                },
                tickAmount: Object.keys(ventas).length * 2
            },

            yaxis: {
                title: {
                    text: "Ventas Totales (Miles $)",
                    style: {
                        fontSize: "14px",
                        fontWeight: 600,
                        color: "#333",
                        fontFamily: "Geist, Urbanist, sans-serif"
                    }
                },
                labels: {
                    formatter: function (value) {
                        return `$${value.toLocaleString("es-CO")}K`;
                    }
                }
            },

            // ========== TOOLTIP PERSONALIZADO ==========
            tooltip: {
                custom: function ({ series, seriesIndex, dataPointIndex, w }) {

                    const point = w.config.series[seriesIndex].data[dataPointIndex];

                    const mes = point.mes;
                    const año = point.anio;
                    const valor = point.y;

                    const total = totalAnual(año).toLocaleString("es-CO");

                    return `
                    <div style="
                        background: #ffffff;
                        border-radius: 12px;
                        border: 1px solid #e5e5e5;
                        font-family: Geist, Urbanist, sans-serif;
                        overflow: hidden;
                        min-width: 180px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    ">

                        <!-- Header -->
                        <div style="
                            background: #f2f4f7;
                            padding: 8px 12px;
                            font-size: 14px;
                            font-weight: 600;
                            color: #333;
                            border-bottom: 1px solid #e1e1e1;
                        ">
                            ${mes} ${año}
                        </div>

                        <!-- Contenido -->
                        <div style="
                            padding: 8px 12px 10px 12px;
                            display: flex;
                            flex-direction: column;
                            gap: 4px;
                        ">

                            <!-- Ventas del mes -->
                            <div style="
                                display: flex;
                                align-items: center;
                                gap: 6px;
                                font-size: 14px;
                                color: #222;
                            ">
                                <div style="
                                    width: 10px;
                                    height: 10px;
                                    background: #0080ff;
                                    border-radius: 50%;
                                "></div>

                                <span style="font-weight: 500;">Ventas del mes:</span>
                                <span style="font-weight: 700;">$${valor.toLocaleString("es-CO")}K</span>
                            </div>

                            <!-- Total del año -->
                            <div style="
                                display: flex;
                                align-items: center;
                                gap: 6px;
                                font-size: 14px;
                                color: #444;
                            ">
                                <div style="
                                    width: 10px;
                                    height: 10px;
                                    background: #0080ff;
                                    border-radius: 50%;
                                "></div>

                                <span style="font-weight: 500;">Total del año:</span>
                                <span style="font-weight: 700;">$${total}K</span>
                            </div>

                        </div>
                    </div>
                `;
                }
            },

            // ========== RESPONSIVE ==========
            responsive: [{
                breakpoint: 768,
                options: {
                    chart: {
                        height: 350
                    },
                    stroke: {
                        width: 3
                    },
                    markers: {
                        size: 4
                    }
                }
            }]
        };

        chart = new ApexCharts(document.querySelector("#chartVentas"), options);
        chart.render();

        // ======================== GENERAR PREDICCIÓN ========================
        document.getElementById("btnPrediccion").addEventListener("click", async () => {
            const overlay = document.getElementById("loadingPrediccion");
            const progress = document.getElementById("progressBar");
            const btn = document.getElementById("btnPrediccion");

            // Mostrar overlay y deshabilitar botón
            overlay.style.display = "flex";
            btn.disabled = true;

            let width = 0;
            const interval = setInterval(() => {
                if (width >= 90) {
                    clearInterval(interval);
                    generarPrediccion();
                } else {
                    width += 2;
                    progress.style.width = width + "%";
                }
            }, 40);
        });

        async function generarPrediccion() {
            try {
                console.log('Generando predicción...');

                // Obtener el último año de los datos existentes
                const ultimoAño = obtenerUltimoAño();
                const añoPrediccion = ultimoAño + 1;

                console.log('Último año en datos:', ultimoAño);
                console.log('Año de predicción:', añoPrediccion);

                // Generar predicción para el siguiente año
                const prediccionData = await generarPrediccionSiguienteAño(añoPrediccion);

                // Combinar datos originales con predicción
                const datosCombinados = [...datosOriginales, ...prediccionData];

                console.log('Datos combinados:', datosCombinados);

                // Actualizar la serie existente con los datos combinados
                options.series[0] = {
                    name: "Ventas Totales + Predicción",
                    data: datosCombinados,
                    stroke: {
                        width: 4,
                        curve: "smooth",
                        colors: ["#0080ff"]
                    },
                    markers: {
                        size: 6,
                        colors: ["#0080ff"],
                        strokeColors: "#ffffff",
                        strokeWidth: 2
                    },
                    fill: {
                        type: "gradient",
                        gradient: {
                            shade: "light",
                            type: "vertical",
                            shadeIntensity: 0.5,
                            gradientToColors: ["#0080ff"],
                            inverseColors: false,
                            opacityFrom: 0.8,
                            opacityTo: 0.2,
                            stops: [0, 90, 100]
                        }
                    }
                };

                chart.updateSeries(options.series);
                console.log('Gráfica actualizada con predicción');

                // Mostrar mensaje de éxito
                mostrarMensajeExito(`Predicción generada para ${añoPrediccion}`);

            } catch (error) {
                console.error('Error generando predicción:', error);
                // Fallback a cálculo local
                generarPrediccionLocal();
            } finally {
                // Ocultar overlay y resetear barra
                const overlay = document.getElementById("loadingPrediccion");
                const progress = document.getElementById("progressBar");
                const btn = document.getElementById("btnPrediccion");

                overlay.style.display = "none";
                progress.style.width = "0%";
                btn.disabled = false;
            }
        }

        function obtenerUltimoAño() {
            // Obtener el año más alto de los datos existentes
            const años = Object.keys(ventas).map(Number);
            return Math.max(...años);
        }

        async function generarPrediccionSiguienteAño(añoPrediccion) {
            try {
                console.log('Solicitando predicción del año', añoPrediccion, 'al servidor...');

                const response = await fetch('/admin/prediccion/prediccion-2026', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    throw new Error(`Error HTTP: ${response.status}`);
                }

                const result = await response.json();
                console.log('Respuesta del servidor:', result);

                if (result.success) {
                    const meses = [
                        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
                    ];

                    return result.proyeccion.map(item => ({
                        x: `${añoPrediccion}-${String(item.mes).padStart(2, "0")}`,
                        y: item.ventaPredicha,
                        mes: item.nombreMes,
                        anio: añoPrediccion.toString()
                    }));
                } else {
                    throw new Error(result.mensaje);
                }

            } catch (error) {
                console.warn('Error obteniendo predicción del servidor, usando cálculo local:', error);
                return generarPrediccionLocalSiguienteAño(añoPrediccion);
            }
        }

        function generarPrediccionLocalSiguienteAño(añoPrediccion) {
            const meses = [
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            ];

            let prediccionData = [];
            const años = Object.keys(ventas).map(Number).sort((a, b) => a - b);
            const ultimosAños = años.slice(-3); // Últimos 3 años para calcular tendencia

            for (let mes = 1; mes <= 12; mes++) {
                let suma = 0;
                let count = 0;

                // Calcular promedio de los últimos años para este mes
                ultimosAños.forEach(año => {
                    if (ventas[año] && ventas[año][mes - 1]) {
                        suma += ventas[año][mes - 1];
                        count++;
                    }
                });

                const promedio = count > 0 ? suma / count : 0;

                // Aplicar crecimiento del 8% basado en tendencia histórica
                const proyeccion = Math.round(promedio * 1.08);

                prediccionData.push({
                    x: `${añoPrediccion}-${String(mes).padStart(2, "0")}`,
                    y: proyeccion,
                    mes: meses[mes - 1],
                    anio: añoPrediccion.toString()
                });
            }

            console.log('Predicción local para año', añoPrediccion, ':', prediccionData);
            return prediccionData;
        }

        function generarPrediccionLocal() {
            // Fallback completo si todo falla
            console.log('Usando fallback completo de predicción...');

            const ultimoAño = obtenerUltimoAño();
            const añoPrediccion = ultimoAño + 1;
            const prediccionData = generarPrediccionLocalSiguienteAño(añoPrediccion);
            const datosCombinados = [...datosOriginales, ...prediccionData];

            options.series[0] = {
                name: "Ventas Totales + Predicción",
                data: datosCombinados,
                stroke: {
                    width: 4,
                    curve: "smooth",
                    colors: ["#0080ff"]
                },
                markers: {
                    size: 6,
                    colors: ["#0080ff"],
                    strokeColors: "#ffffff",
                    strokeWidth: 2
                }
            };

            chart.updateSeries(options.series);
            mostrarMensajeExito(`Predicción calculada para ${añoPrediccion} basada en tendencias históricas`);
        }

        function mostrarMensajeExito(mensaje) {
            // Crear un toast de éxito
            const toast = document.createElement('div');
            toast.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                background: #4CAF50;
                color: white;
                padding: 12px 20px;
                border-radius: 8px;
                font-family: 'Geist', sans-serif;
                font-weight: 500;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                z-index: 10000;
                animation: slideIn 0.3s ease;
            `;
            toast.textContent = mensaje;

            document.body.appendChild(toast);

            // Remover después de 3 segundos
            setTimeout(() => {
                toast.style.animation = 'slideOut 0.3s ease';
                setTimeout(() => {
                    document.body.removeChild(toast);
                }, 300);
            }, 3000);
        }

        // Agregar estilos CSS para las animaciones
        const style = document.createElement('style');
        style.textContent = `
            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            @keyframes slideOut {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
        `;
        document.head.appendChild(style);
    }

});