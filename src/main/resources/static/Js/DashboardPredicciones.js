
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


    // Abrir y cerrar menú
    const menuBtn = document.getElementById("menuPrediccionBtn");
    const menu = document.getElementById("prediccionMenu");

    menuBtn.addEventListener("click", () => {
        menu.style.display = menu.style.display === "block" ? "none" : "block";
    });

    // Cerrar si se hace clic fuera
    document.addEventListener("click", (e) => {
        if (!menuBtn.contains(e.target)) {
            menu.style.display = "none";
        }
    });

    //Grafica de proyeccion
    // ====== Generador pseudoaleatorio determinista ======
    function seededRandom(seed) {
        var x = Math.sin(seed) * 10000;
        return x - Math.floor(x);
    }

    // ====== GENERADOR DE DATOS CON SUBPICOS EN TODAS LAS LÍNEAS (DET.) ======
    function generateYearWiseSalesWithContinuousSubPeaks(baseYear, count, yrange, seed = 12345) {
        var series = [];
        var lastValue = 1200; // Valor inicial en $

        for (var i = 0; i < count; i++) {
            var year = baseYear + i;

            // Número de puntos principales (picos grandes) por año
            var mainPoints = 2;
            var mainStart = lastValue;

            for (var mp = 0; mp < mainPoints; mp++) {
                // Pico grande determinista
                var mainChange = Math.floor(seededRandom(seed + year * 10 + mp) * 350 - 200);
                var mainValue = mainStart + mainChange;

                if (mainValue > yrange.max) mainValue = yrange.max - seededRandom(seed + year * 20 + mp) * 50;
                if (mainValue < yrange.min) mainValue = yrange.min + seededRandom(seed + year * 30 + mp) * 50;

                // Subpicos deterministas
                var subPeaks = 10; // cantidad de micro-picos por tramo
                for (var sp = 0; sp < subPeaks; sp++) {
                    var microChange = Math.floor(seededRandom(seed + year * 40 + mp * 10 + sp) * 20 - 10);
                    var y = mainStart + ((mainValue - mainStart) * (sp + 1) / subPeaks) + microChange;

                    series.push([year + (mp + sp / subPeaks) / mainPoints, y]);
                }

                mainStart = mainValue;
            }

            lastValue = mainStart;
        }

        return series;
    }

    // ====== DATOS ======
    var salesData = generateYearWiseSalesWithContinuousSubPeaks(2017, 9, { min: 1000, max: 2500 });

    // ====== OPCIONES DEL CHART ======
    var optionsProye = {
        series: [{
            name: 'Ventas Totales',
            data: salesData
        }],
        chart: {
            type: 'area',
            stacked: false,
            height: 450,
            zoom: { type: 'x', enabled: true, autoScaleYaxis: true },
            toolbar: { show: false },
            fontFamily: 'Geist, sans-serif'
        },
        dataLabels: { enabled: false },
        stroke: { curve: 'straight', width: 3 },
        markers: { size: 0 },
        fill: {
            type: 'gradient',
            gradient: {
                shade: 'light',
                type: 'vertical',
                shadeIntensity: 1,
                gradientToColors: ['#00BFFF'],
                inverseColors: false,
                opacityFrom: 0.6,
                opacityTo: 0,
                stops: [0, 100]
            }
        },
        yaxis: {
            labels: { formatter: val => `$${val.toFixed(0)}`, style: { fontFamily: 'Geist, sans-serif', fontSize: '14px', fontWeight: 400 } },
            title: { text: 'Ventas Totales', style: { fontFamily: 'Geist, sans-serif', fontSize: '13px', fontWeight: 700 } }
        },
        xaxis: {
            type: 'numeric',
            title: { text: 'Años', style: { fontFamily: 'Geist, sans-serif', fontSize: '14px', fontWeight: 700 } },
            labels: { formatter: val => Math.floor(val), style: { fontFamily: 'Geist, sans-serif', fontSize: '13px' } }
        },
        tooltip: { y: { formatter: val => `$${val.toFixed(0)}`, style: { fontFamily: 'Geist, sans-serif' } } }
    };

    // ====== RENDERIZAR ======
    var chartProye = new ApexCharts(document.querySelector("#chartVentas"), optionsProye);
    chartProye.render();

    //Predecir compras


});