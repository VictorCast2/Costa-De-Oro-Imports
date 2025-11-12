import {
    activarGlassmorphism,
    inicialHeart,
    initCart,
    rederigirFav,
    finalizarCompra,
    verProductos,
} from "./main.js";

document.addEventListener("DOMContentLoaded", () => {
    activarGlassmorphism();

    inicialHeart();

    initCart();

    rederigirFav();

    finalizarCompra();

    verProductos();

    //filtrar los productos
    const sidebar = document.getElementById("sidebar-filtros");
    const closeBtn = document.getElementById("close-sidebar");
    const openBtn = document.getElementById("btn-filtros");

    // Crear overlay si no existe
    let overlay = document.querySelector(".sidebar-overlay");
    if (!overlay) {
        overlay = document.createElement("div");
        overlay.className = "sidebar-overlay";
        document.body.appendChild(overlay);
    }

    // ✅ Añadir listeners solo si los elementos existen
    if (openBtn && sidebar && overlay) {
        openBtn.addEventListener("click", () => {
            sidebar.classList.add("active");
            overlay.classList.add("active");
        });
    }

    if (closeBtn && sidebar && overlay) {
        closeBtn.addEventListener("click", () => {
            sidebar.classList.remove("active");
            overlay.classList.remove("active");
        });
    }

    if (overlay && sidebar) {
        overlay.addEventListener("click", () => {
            sidebar.classList.remove("active");
            overlay.classList.remove("active");
        });
    }

    // Seleccionamos el botón de "Ordenar por"
    const ordenarBtn = document.querySelector(
        ".menu__item:first-child .menu__toggle"
    );
    // Obtenemos todos los checkboxes del submenu "Ordenar por"
    const ordenarCheckboxes = document.querySelectorAll(
        '.menu__item:first-child .submenu input[type="checkbox"]'
    );
    // Variable para almacenar el texto original
    const textoOriginal = ordenarBtn.textContent;

    // Agregamos evento a cada checkbox
    ordenarCheckboxes.forEach((checkbox) => {
        checkbox.addEventListener("change", (e) => {
            // Desmarcar todos los demás checkboxes
            ordenarCheckboxes.forEach((cb) => {
                if (cb !== checkbox) cb.checked = false;
            });

            // Si se selecciona uno, actualizar el texto del botón
            if (checkbox.checked) {
                const textoSeleccionado = checkbox.parentElement.textContent.trim();
                ordenarBtn.textContent = `${textoOriginal} ${textoSeleccionado}`;
            } else {
                // Si se deselecciona, volver al texto original
                ordenarBtn.textContent = textoOriginal;
            }
        });
    });

    // Toggle de los menús desplegables
    const menuToggles = document.querySelectorAll(".menu__toggle");

    menuToggles.forEach((toggle) => {
        toggle.addEventListener("click", (e) => {
            e.preventDefault();
            const menuItem = toggle.closest(".menu__item");
            if (!menuItem) return;

            const isFiltros = menuItem.id === "filtros-aplicados";
            const wasActive = menuItem.classList.contains("active");

            // Si es "Filtros aplicados", solo hacer toggle sin cerrar otros
            if (isFiltros) {
                menuItem.classList.toggle("active");
                return;
            }

            // Cerrar todos los demás MENOS "Filtros aplicados"
            document.querySelectorAll(".menu__item").forEach((item) => {
                if (item.id === "filtros-aplicados") return;
                item.classList.remove("active");
            });

            // Activar o desactivar el menú clicado
            if (!wasActive) {
                menuItem.classList.add("active");
            } else {
                menuItem.classList.remove("active");
            }
        });
    });

    // rango de precio
    const minRange = document.getElementById("minRange");
    const maxRange = document.getElementById("maxRange");
    const rangeMinDisplay = document.getElementById("rangeMin");
    const rangeMaxDisplay = document.getElementById("rangeMax");
    const sliderContainer = document.querySelector(".slider-container");

    // Formatear números con separadores de miles y símbolo de peso
    function formatCurrency(value) {
        return "$" + parseInt(value).toLocaleString("es-CO");
    }

    // Actualizar la visualización de los valores
    function updateValues() {
        let minVal = parseInt(minRange.value);
        let maxVal = parseInt(maxRange.value);

        // Evitar que los sliders se crucen
        if (maxVal - minVal < 100000) {
            if (event.target === minRange) {
                minVal = maxVal - 100000;
                minRange.value = minVal;
            } else {
                maxVal = minVal + 100000;
                maxRange.value = maxVal;
            }
        }

        // Actualizar los valores mostrados
        rangeMinDisplay.textContent = formatCurrency(minVal);
        rangeMaxDisplay.textContent = formatCurrency(maxVal);

        // Actualizar la barra de progreso visual
        const minPercent = (minVal / minRange.max) * 100;
        const maxPercent = 100 - (maxVal / maxRange.max) * 100;

        sliderContainer.style.setProperty("--range-min", minPercent + "%");
        sliderContainer.style.setProperty("--range-max", maxPercent + "%");
    }

    // Event listeners
    minRange.addEventListener("input", updateValues);
    maxRange.addEventListener("input", updateValues);

    // Inicializar valores al cargar
    updateValues();

    // Opcional: Obtener los valores cuando necesites aplicar el filtro
    function getPriceRange() {
        return {
            min: parseInt(minRange.value),
            max: parseInt(maxRange.value),
        };
    }

    //Buscar por texto en pais,marca y categoria
    document.querySelectorAll(".filter-input").forEach((input) => {
        input.addEventListener("input", (e) => {
            const filter = e.target.value.toLowerCase();
            const submenu = e.target.closest(".submenu");
            const items = submenu.querySelectorAll("li label");

            items.forEach((label) => {
                const text = label.textContent.toLowerCase();
                label.parentElement.style.display = text.includes(filter) ? "" : "none";
            });
        });
    });

    //FILTROS APLICADOS
    const sidebarMenu = document.querySelector(".sidebar__menu");
    const filtrosAplicados = document.querySelector("#filtros-aplicados");
    const filtrosLista = filtrosAplicados
        ? filtrosAplicados.querySelector(".submenu")
        : null;
    const filtrosToggle = filtrosAplicados
        ? filtrosAplicados.querySelector(".menu__toggle")
        : null;
    const limpiarTodoDiv = document.querySelector(".limpiar__todo");
    const limpiarTodoBtn = document.getElementById("limpiar-todo");

    if (!sidebarMenu || !filtrosAplicados || !filtrosLista) {
        console.error("No se encontró sidebar, filtros-aplicados o su lista.");
    } else {
        // Detecta si un checkbox está dentro del menú "Ordenar por"
        function isInOrdenarPor(checkbox) {
            const menuItem = checkbox.closest(".menu__item");
            if (!menuItem) return false;
            const toggle = menuItem.querySelector(".menu__toggle");
            if (!toggle) return false;
            const txt = toggle.textContent.trim().toLowerCase();
            return txt.includes("ordenar por");
        }

        // Detecta si un checkbox está dentro del propio bloque "Filtros aplicados"
        function isInFiltrosAplicados(checkbox) {
            return Boolean(checkbox.closest("#filtros-aplicados"));
        }

        // Abre el menú de filtros aplicados
        function abrirFiltrosAplicados() {
            filtrosAplicados.classList.add("active");
            filtrosLista.style.display = "block";
        }

        function cerrarFiltrosAplicados() {
            filtrosAplicados.classList.remove("active");
            filtrosLista.style.display = "none";
        }

        // Obtiene todos los checkboxes fuente (excepto "Ordenar por" y "Filtros aplicados")
        function getSourceCheckboxes() {
            const all = Array.from(
                sidebarMenu.querySelectorAll(".submenu input[type='checkbox']")
            );
            return all.filter(
                (cb) => !isInOrdenarPor(cb) && !isInFiltrosAplicados(cb)
            );
        }

        // Actualiza los filtros aplicados visualmente
        function actualizarFiltrosAplicados() {
            const source = getSourceCheckboxes();
            const seleccionados = source
                .filter((cb) => cb.checked)
                .map((cb) => {
                    const label = cb.closest("label");
                    if (!label) return cb.value || "";
                    const textNodes = Array.from(label.childNodes).filter(
                        (n) => n.nodeType === Node.TEXT_NODE
                    );
                    return (
                        textNodes.map((n) => n.textContent.trim()).join(" ") ||
                        label.textContent.trim()
                    ).trim();
                })
                .filter(Boolean);

            // Limpiar lista
            filtrosLista.innerHTML = "";

            if (seleccionados.length > 0) {
                filtrosAplicados.style.display = "block";
                limpiarTodoDiv.style.display = "block"; // Mostrar el botón LIMPIAR
                abrirFiltrosAplicados();

                seleccionados.forEach((nombre) => {
                    const li = document.createElement("li");
                    li.innerHTML = `
                    <label>
                        <input type="checkbox" checked data-filtro="${nombre}">
                        ${nombre}
                    </label>
                `;
                    filtrosLista.appendChild(li);
                });
            } else {
                filtrosAplicados.style.display = "none";
                limpiarTodoDiv.style.display = "none"; // Ocultar el botón LIMPIAR
                cerrarFiltrosAplicados();
            }
        }

        // Evento al cambiar cualquier checkbox
        sidebarMenu.addEventListener("change", (e) => {
            const target = e.target;
            if (!target.matches("input[type='checkbox']")) return;

            if (isInOrdenarPor(target)) return; // Ignorar "Ordenar por"

            if (isInFiltrosAplicados(target)) {
                // Si se desmarca dentro de filtros aplicados, sincroniza el original
                const valor = target.dataset.filtro;
                const source = getSourceCheckboxes().find((cb) => {
                    const lbl = cb.closest("label");
                    if (!lbl) return false;
                    const textNodes = Array.from(lbl.childNodes).filter(
                        (n) => n.nodeType === Node.TEXT_NODE
                    );
                    const text = (
                        textNodes.map((n) => n.textContent.trim()).join(" ") ||
                        lbl.textContent.trim()
                    ).trim();
                    return text === valor;
                });
                if (source) source.checked = target.checked;
                actualizarFiltrosAplicados();
                return;
            }

            // Actualizar lista al marcar/desmarcar filtros reales
            actualizarFiltrosAplicados();
        });

        // Evita cerrar manualmente el bloque “Filtros aplicados”
        if (filtrosToggle) {
            filtrosToggle.addEventListener("click", (e) => {
                e.preventDefault();
                abrirFiltrosAplicados();
            });
        }

        // Mantiene abierto "Filtros aplicados" aunque se abra otro menú
        document.querySelectorAll(".menu__toggle").forEach((toggle) => {
            toggle.addEventListener("click", () => {
                abrirFiltrosAplicados();
            });
        });

        // --- LIMPIAR TODO --- //
        limpiarTodoBtn.addEventListener("click", () => {
            // Desmarcar todos los checkboxes excepto los de "Ordenar por"
            getSourceCheckboxes().forEach((cb) => (cb.checked = false));
            actualizarFiltrosAplicados();
        });

        // Inicializar estado al cargar
        actualizarFiltrosAplicados();
    }

    //boton de limpiar todo
});
