document.addEventListener("DOMContentLoaded", () => {
    // Efecto glassmorphism solo al hacer scroll
    const header = document.querySelector(".content__header");

    window.addEventListener("scroll", () => {
        if (window.scrollY > 10) {
            header.classList.add("scrolled");
        } else {
            header.classList.remove("scrolled");
        }
    });

    //Menú desplegable del perfil
    const subMenu = document.getElementById("SubMenu");
    const profileImage = document.getElementById("user__admin");

    if (subMenu && profileImage) {
        profileImage.addEventListener("click", function (e) {
            e.stopPropagation();
            subMenu.classList.toggle("open__menu");
        });

        document.addEventListener("click", function (e) {
            if (!subMenu.contains(e.target) && !profileImage.contains(e.target)) {
                subMenu.classList.remove("open__menu");
            }
        });
    }

    // Notificaciones admin
    const notifIcon = document.getElementById("notifIcon");
    const notifMenu = document.getElementById("notifMenu");

    notifIcon.addEventListener("click", (e) => {
        e.stopPropagation();
        notifMenu.classList.toggle("open");
    });

    document.addEventListener("click", (e) => {
        if (!notifMenu.contains(e.target) && !notifIcon.contains(e.target)) {
            notifMenu.classList.remove("open");
        }
    });

    // ==============================================
    //   CARGAR DATOS DESDE EL BACKEND
    // ==============================================
    async function cargarDatosBackend() {
        try {
            const resp = await fetch("/api/graficas/ventas-totales");
            return await resp.json();
        } catch (e) {
            console.error("Error cargando API de gráficas", e);
            return null;
        }
    }

    cargarDatosBackend().then(datos => {

        if (!datos) return;

        const meses = [
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        ];

        // ==============================================
        //   CREAR ESTRUCTURA EXACTA COMO LA TENÍAS
        // ==============================================
        const ventas = {};
        datos.anios.forEach(a => {
            ventas[a] = datos.totalesPorAnio[a];
        });

        // Crear puntos mensuales dinámicamente
        const data = [];
        for (const anio in ventas) {
            ventas[anio].forEach((valor, i) => {
                data.push({
                    x: `${anio}-${String(i + 1).padStart(2, "0")}`,
                    y: valor,
                    mes: meses[i],
                    anio: anio
                });
            });
        }

        // Total anual (idéntico al tuyo)
        function totalAnual(año) {
            return ventas[año].reduce((acc, v) => acc + v, 0);
        }

        // ==============================================
        //   OPCIONES DE LA GRÁFICA (IDÉNTICAS)
        // ==============================================
        var options = {
            chart: {
                type: 'area',
                height: 450,
                zoom: { enabled: true, type: 'x' },
                toolbar: { show: false },
                foreColor: "#333",
                fontFamily: "Geist, Urbanist, sans-serif"
            },

            dataLabels: { enabled: false },

            series: [{
                name: "Ventas Totales",
                data: data
            }],

            stroke: { width: 3, curve: "smooth" },
            markers: { size: 5 },

            fill: {
                type: "gradient",
                gradient: {
                    shade: "light",
                    type: "vertical",
                    shadeIntensity: 0.5,
                    gradientToColors: ["#00c6ff"],
                    inverseColors: false,
                    opacityFrom: 0.6,
                    opacityTo: 0,
                    stops: [0, 90, 100]
                }
            },

            xaxis: {
                type: "category",
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
                    formatter: function (value) {
                        if (!value) return "";
                        const mes = value.substring(5, 7);
                        const año = value.substring(0, 4);
                        return mes === "01" ? año : "";
                    }
                },
                tickAmount: datos.anios.length * 2
            },

            yaxis: {
                title: { text: "Ventas Totales" }
            },

            tooltip: {
                custom: function ({ series, seriesIndex, dataPointIndex, w }) {

                    const point = w.config.series[seriesIndex].data[dataPointIndex];
                    const mes = point.mes;
                    const año = point.anio;
                    const valor = point.y;

                    // Tooltip original
                    if (seriesIndex === 0) {

                        const total = totalAnual(año).toLocaleString("es-CO");

                        return `
                            <div style="
                                background: #ffffff;
                                border-radius: 12px;
                                border: 1px solid #e5e5e5;
                                font-family: Geist, Urbanist, sans-serif;
                                overflow: hidden;
                                min-width: 180px;
                            ">
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

                                <div style="
                                    padding: 8px 12px 10px 12px;
                                    display: flex;
                                    flex-direction: column;
                                    gap: 4px;
                                ">

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
                                        <span style="font-weight: 700;">$${valor.toLocaleString("es-CO")}</span>
                                    </div>

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
                                        <span style="font-weight: 700;">$${total}</span>
                                    </div>

                                </div>
                            </div>
                        `;
                    }

                    // Tooltip predicción
                    if (seriesIndex === 1) {

                        const totalProyeccion = w.config.series[1].data
                            .reduce((acc, p) => acc + p.y, 0)
                            .toLocaleString("es-CO");

                        return `
                            <div style="
                                background: #ffffff;
                                border-radius: 12px;
                                border: 1px solid #00bcd4;
                                font-family: Geist, Urbanist, sans-serif;
                                overflow: hidden;
                                min-width: 180px;
                            ">
                                <div style="
                                    background: #e8fafd;
                                    padding: 8px 12px;
                                    font-size: 14px;
                                    font-weight: 600;
                                    color: #006f80;
                                    border-bottom: 1px solid #b2edf5;
                                ">
                                    ${mes} ${año}
                                </div>

                                <div style="
                                    padding: 8px 12px 10px 12px;
                                    display: flex;
                                    flex-direction: column;
                                    gap: 4px;
                                ">

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
                                            background: #00bcd4;
                                            border-radius: 50%;
                                        "></div>

                                        <span style="font-weight: 500;">Proyección mensual:</span>
                                        <span style="font-weight: 700;">$${valor.toLocaleString("es-CO")}</span>
                                    </div>

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
                                            background: #00bcd4;
                                            border-radius: 50%;
                                        "></div>

                                        <span style="font-weight: 500;">Proyección total:</span>
                                        <span style="font-weight: 700;">$${totalProyeccion}</span>
                                    </div>

                                </div>
                            </div>
                        `;
                    }
                }
            }
        };

        // Crear gráfico
        var chart = new ApexCharts(document.querySelector("#chartVentas"), options);
        chart.render();

        // ============================================
        // GENERAR PREDICCIÓN 2026 (MISMA LÓGICA)
        // ============================================
        document.getElementById("btnPrediccion").addEventListener("click", async () => {

            const overlay = document.getElementById("loadingPrediccion");
            const progress = document.getElementById("progressBar");
            const btn = document.getElementById("btnPrediccion");

            // Año siguiente al último disponible en el dataset
            const ultimoAnio = Math.max(...datos.anios);
            const anioPrediccion = ultimoAnio + 1;

            // Evitar que se generen predicciones duplicadas
            if (options.series.some(s => s.name === `Predicción ${anioPrediccion}`)) {
                alert("La predicción ya fue generada.");
                return;
            }

            overlay.style.display = "flex";
            btn.disabled = true;

            let width = 0;
            const intervalo = setInterval(() => {
                width += 2;
                progress.style.width = width + "%";
            }, 40);

            // Array con los resultados de los 12 meses
            let prediccionesMensuales = [];

            try {
                for (let mes = 1; mes <= 12; mes++) {

                    // 🚀 Datos de entrada para el modelo WEKA
                    const request = {
                        anno: anioPrediccion,
                        mes: mes,
                        cantidadProductos: 50,
                        totalUnidades: 50,
                        precioPromedio: 25000.00
                    };

                    // Llamada al backend Spring
                    const response = await fetch("/api/prediccion/generar", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify(request)
                    });

                    if (!response.ok) throw new Error("Error en el servidor al predecir");

                    const prediccion = await response.json();

                    // Se guarda el valor predicho
                    prediccionesMensuales.push({
                        x: `${anioPrediccion}-${String(mes).padStart(2, "0")}`,
                        y: Math.round(prediccion),
                        mes: mes,
                        anio: anioPrediccion
                    });
                }

                // Mostrar serie en ApexCharts
                options.series.push({
                    name: `Predicción ${anioPrediccion}`,
                    data: prediccionesMensuales
                });

                chart.updateSeries(options.series);

            } catch (error) {
                console.error(error);
                alert("Error generando la predicción.");
            }

            clearInterval(intervalo);
            overlay.style.display = "none";
            progress.style.width = "0%";
            btn.disabled = false;
        });

    });

});