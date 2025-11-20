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

    //FILTRO DE PRODUCTOS (por texto y estado)
    (() => {
        const searchInput = document.getElementById("inputSearch");
        const stateSelect = document.getElementById("filtro-estado");
        const tableBody = document.getElementById("favoritos-body");

        if (!searchInput || !stateSelect || !tableBody) return;

        const tableRows = Array.from(tableBody.rows);

        const filtrarTabla = () => {
            const texto = searchInput.value.trim().toLowerCase();
            const estadoSeleccionado = stateSelect.value.trim().toLowerCase();
            const palabras = texto.split(/\s+/).filter(Boolean); // separa las palabras

            tableRows.forEach(row => {
                // Tomamos todo el texto de las celdas (sin los botones del menú)
                const celdas = Array.from(row.querySelectorAll("td"))
                    .filter(td => !td.classList.contains("menu__actions")) // evita el menú
                    .map(td => td.textContent.toLowerCase())
                    .join(" "); // une todo en un solo string

                const estado = row.querySelector('.td__estados')?.textContent.toLowerCase() || '';

                // Coincide si alguna palabra está presente en cualquier parte del texto del tr
                const coincideTexto = palabras.length === 0 || palabras.some(palabra => celdas.includes(palabra));
                const coincideEstado = estadoSeleccionado === "todos" || estado === estadoSeleccionado;

                // Mostrar u ocultar según los filtros
                row.hidden = !(coincideTexto && coincideEstado);
            });
        };

        searchInput.addEventListener('input', filtrarTabla);
        stateSelect.addEventListener('change', filtrarTabla);
    })();

    //pagination de la tabla 
    const filasPorPagina = 12;
    const tablaBody = document.getElementById("favoritos-body");
    const filas = tablaBody.querySelectorAll("tr");
    const totalFilas = filas.length;
    const totalPaginas = Math.ceil(totalFilas / filasPorPagina);

    const contenedorBotones = document.querySelector(".pagination__button");
    const textoPaginacion = document.querySelector(".span__description");

    let paginaActual = 1;

    // === Función para marcar los últimos dos menús visibles ===
    function marcarUltimosDosMenus() {
        // Seleccionar filas visibles
        const filasVisibles = Array.from(tablaBody.querySelectorAll("tr"))
            .filter(tr => tr.style.display !== "none");

        // Limpiar clases previas
        const todosMenus = tablaBody.querySelectorAll("td.menu__actions .dropdown__menu");
        todosMenus.forEach(menu => menu.classList.remove("up"));

        // Tomar las últimas dos filas visibles
        const ultimosDos = filasVisibles.slice(-2);
        ultimosDos.forEach(tr => {
            const menu = tr.querySelector("td.menu__actions .dropdown__menu");
            if (menu) menu.classList.add("up");
        });
    }

    // === Mostrar filas según página seleccionada ===
    function mostrarPagina(pagina) {
        const inicio = (pagina - 1) * filasPorPagina;
        const fin = inicio + filasPorPagina;

        filas.forEach((fila, index) => {
            fila.style.display = (index >= inicio && index < fin) ? "" : "none";
        });

        // Actualizar texto de paginación
        const inicioTexto = inicio + 1;
        const finTexto = Math.min(fin, totalFilas);
        textoPaginacion.textContent = `Mostrando del ${inicioTexto} al ${finTexto} de ${totalFilas} entradas`;

        // Actualizar botones activos
        const botonesPagina = contenedorBotones.querySelectorAll(".button__item");
        botonesPagina.forEach((btn, i) => {
            btn.classList.toggle("active", i + 1 === pagina);
        });

        // Control botones anterior / siguiente
        document.querySelector(".pasar--anterior").disabled = pagina === 1;
        document.querySelector(".pasar--siguiente").disabled = pagina === totalPaginas;

        // Después de actualizar las filas, marcamos los últimos dos menús visibles
        marcarUltimosDosMenus();
    }

    // === Crear botones dinámicamente ===
    function crearBotones() {
        const anterior = contenedorBotones.querySelector(".pasar--anterior");
        const siguiente = contenedorBotones.querySelector(".pasar--siguiente");

        contenedorBotones.querySelectorAll(".button__item").forEach(btn => btn.remove());

        for (let i = 1; i <= totalPaginas; i++) {
            const btn = document.createElement("button");
            btn.classList.add("button__item");
            btn.textContent = i;

            btn.addEventListener("click", () => {
                paginaActual = i;
                mostrarPagina(paginaActual);
            });

            siguiente.before(btn);
        }
    }

    // === Eventos de navegación ===
    document.querySelector(".pasar--anterior").addEventListener("click", () => {
        if (paginaActual > 1) {
            paginaActual--;
            mostrarPagina(paginaActual);
        }
    });

    document.querySelector(".pasar--siguiente").addEventListener("click", () => {
        if (paginaActual < totalPaginas) {
            paginaActual++;
            mostrarPagina(paginaActual);
        }
    });

    // === Inicializar paginación ===
    crearBotones();
    mostrarPagina(paginaActual);

    // verificar de forma dinamica cuantos registros hay en la tabla de productos
    const tablaBodyTotal = document.getElementById("favoritos-body");
    const textoProductos = document.querySelector(".producto__texto");

    if (tablaBodyTotal && textoProductos) {
        const totalFilas = tablaBodyTotal.querySelectorAll("tr").length;
        textoProductos.textContent = `Hay ${totalFilas} producto${totalFilas !== 1 ? 's' : ''} registrado${totalFilas !== 1 ? 's' : ''}`;
    }

});



