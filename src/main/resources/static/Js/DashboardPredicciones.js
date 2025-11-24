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
    let proyeccion2026 = [];
    let chart; // Variable global para el gráfico

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
            proyeccion2026 = data.proyeccion2026 || [];

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
        const data = [];
        for (const anio in ventas) {
            ventas[anio].forEach((valor, i) => {
                data.push({
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
                type: 'area',
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
                    gradientToColors: ["#00c6ff"], // color que se difumina hacia abajo
                    inverseColors: false,
                    opacityFrom: 0.6,  // intensidad arriba (desde la línea)
                    opacityTo: 0,      // intensidad abajo
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
                title: { text: "Ventas Totales (Miles $)" }
            },

            // ========== TOOLTIP PERSONALIZADO ==========
            tooltip: {
                custom: function ({ series, seriesIndex, dataPointIndex, w }) {

                    const point = w.config.series[seriesIndex].data[dataPointIndex];

                    const mes = point.mes;
                    const año = point.anio;
                    const valor = point.y;

                    // CASE 1 ========= SERIE ORIGINAL (2020-2025) ==========
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

                    // CASE 2 ========= SERIE PREDICCIÓN 2026 ==========
                    if (seriesIndex === 1) {

                        const totalProyeccion = w.config.series[1].data
                            .reduce((acc, p) => acc + p.y, 0)
                            .toLocaleString("es-CO");

                        return `
                    <div style="
                        background: #ffffff;
                        border-radius: 12px;
                        border: 1px solid #00bcd4;   /* borde distinto solo para diferenciar */
                        font-family: Geist, Urbanist, sans-serif;
                        overflow: hidden;
                        min-width: 180px;
                    ">

                        <!-- Header -->
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

                        <!-- Contenido -->
                        <div style="
                            padding: 8px 12px 10px 12px;
                            display: flex;
                            flex-direction: column;
                            gap: 4px;
                        ">

                            <!-- Proyección mensual -->
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
                                <span style="font-weight: 700;">$${valor.toLocaleString("es-CO")}K</span>
                            </div>

                            <!-- Proyección total -->
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
                                <span style="font-weight: 700;">$${totalProyeccion}K</span>
                            </div>

                        </div>
                    </div>
                `;
                    }
                }
            }
        };

        chart = new ApexCharts(document.querySelector("#chartVentas"), options);
        chart.render();

        // ======================== GENERAR PREDICCIÓN 2026 ========================
        document.getElementById("btnPrediccion").addEventListener("click", async () => {
            const overlay = document.getElementById("loadingPrediccion");
            const progress = document.getElementById("progressBar");
            const btn = document.getElementById("btnPrediccion");

            // Evitar doble clic si ya existe la predicción
            if (options.series.some(s => s.name === "Predicción 2026")) {
                console.log('La predicción 2026 ya está generada');
                return;
            }

            // Mostrar overlay y deshabilitar botón
            overlay.style.display = "flex";
            btn.disabled = true;

            let width = 0;
            const interval = setInterval(() => {
                if (width >= 90) {
                    clearInterval(interval);
                    generarPrediccionReal();
                } else {
                    width += 2;
                    progress.style.width = width + "%";
                }
            }, 40);
        });

        async function generarPrediccionReal() {
            try {
                console.log('Solicitando predicción 2026 al servidor...');
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
                    const data2026 = result.proyeccion.map(item => ({
                        x: `2026-${String(item.mes).padStart(2, "0")}`,
                        y: item.ventaPredicha,
                        mes: item.nombreMes,
                        anio: "2026"
                    }));

                    console.log('Datos de predicción 2026:', data2026);

                    options.series.push({
                        name: "Predicción 2026",
                        data: data2026
                    });

                    chart.updateSeries(options.series);
                    console.log('Gráfica actualizada con predicción 2026');
                } else {
                    console.warn('El servidor reportó un error, usando cálculo local:', result.mensaje);
                    // Fallback a cálculo local si el servidor falla
                    generarPrediccionLocal();
                }

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

        function generarPrediccionLocal() {
            console.log('Generando predicción local...');
            const meses = [
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            ];

            let data2026 = [];
            for (let i = 0; i < 12; i++) {
                const promedio = (ventas[2023][i] + ventas[2024][i] + ventas[2025][i]) / 3;
                const proyeccion = Math.round(promedio * 1.12);
                data2026.push({
                    x: `2026-${String(i + 1).padStart(2, "0")}`,
                    y: proyeccion,
                    mes: meses[i],
                    anio: "2026"
                });
            }

            console.log('Predicción local generada:', data2026);
            options.series.push({ name: "Predicción 2026", data: data2026 });
            chart.updateSeries(options.series);
        }
    }

});