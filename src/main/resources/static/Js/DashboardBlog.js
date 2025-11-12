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

    // FILTRO DE CARTAS (por texto y estado)
    (() => {
        const searchInput = document.getElementById("inputSearch");
        const stateSelect = document.getElementById("filtro-estado");
        const cards = document.querySelectorAll(".content__card");

        if (!searchInput || !stateSelect || !cards.length) return;

        const normalizar = texto =>
            texto
                .toLowerCase()
                .normalize("NFD")
                .replace(/[\u0300-\u036f]/g, "")
                .trim();

        const filtrarCartas = () => {
            const texto = normalizar(searchInput.value);
            const estadoSeleccionado = normalizar(stateSelect.value);
            const palabras = texto.split(/\s+/).filter(Boolean);

            cards.forEach(card => {
                const contenidoCarta = normalizar(card.textContent);
                const btnEstado = card.querySelector(".buttons__estado");

                const textoEstado = normalizar(btnEstado?.textContent || "");
                const claseEstado = [...(btnEstado?.classList || [])]
                    .map(normalizar)
                    .find(c => c === "activo" || c === "inactivo") || "";

                // Coincidencia de texto flexible, pero exacta para "activo" e "inactivo"
                const coincideTexto =
                    palabras.length === 0 ||
                    palabras.some(palabra => {
                        if (palabra === "activo" || palabra === "inactivo") {
                            // Coincidencia exacta solo para estos dos
                            return (
                                textoEstado === palabra ||
                                claseEstado === palabra
                            );
                        } else {
                            // Coincidencia parcial normal para el resto
                            return (
                                contenidoCarta.includes(palabra) ||
                                textoEstado.includes(palabra) ||
                                claseEstado.includes(palabra)
                            );
                        }
                    });

                // Coincidencia del select
                const coincideEstado =
                    estadoSeleccionado === "todos" ||
                    textoEstado === estadoSeleccionado ||
                    claseEstado === estadoSeleccionado;

                // Mostrar u ocultar carta
                card.style.display = coincideTexto && coincideEstado ? "flex" : "none";
            });
        };

        searchInput.addEventListener("input", filtrarCartas);
        stateSelect.addEventListener("change", filtrarCartas);
    })();


    // Manejar abrir/cerrar los menús
    document.addEventListener("click", function (e) {
        const dropdowns = document.querySelectorAll(".dropdown");
        dropdowns.forEach((dropdown) => {
            const toggle = dropdown.querySelector(".dropdown__toggle");
            const menu = dropdown.querySelector(".dropdown__menu");

            if (toggle.contains(e.target)) {
                dropdown.classList.toggle("show");
            } else {
                dropdown.classList.remove("show");
            }
        });
    });

    // ---- Modales con SweetAlert2 ----
    document.addEventListener("click", (e) => {
        // === ELIMINAR ===
        /* A esta modal se le tiene que pasar detele del controllador o algo asi xd no se de spring boot
        problema tuyo jose */
        if (e.target.closest(".eliminar")) {
            Swal.fire({
                title: "¿Estás seguro?",
                text: "Esta acción eliminará el registro permanentemente.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d33",
                cancelButtonColor: "#3085d6",
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar",
                customClass: {
                    title: 'swal-title',
                    popup: 'swal-popup'
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        title: "Eliminado",
                        text: "El registro ha sido eliminado exitosamente.",
                        icon: "success",
                        timer: 1500,
                        showConfirmButton: false,
                        customClass: {
                            title: 'swal-title',
                            popup: 'swal-popup'
                        }
                    });

                }
            });
        }

        // === EDITAR ===
        else if (e.target.closest(".editar")) {
            Swal.fire({
                title: "¿Estás seguro?",
                text: "¿Deseas editar este registro?",
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#6c757d",
                confirmButtonText: "Sí, editar",
                cancelButtonText: "Cancelar",
                customClass: {
                    title: 'swal-title',
                    popup: 'swal-popup'
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    /* Nota: Debe de enviarte a la pagina de EditarProducto.html con 
                    spring boot easy no? con un get en el controlldor easy no?
                    una vez lo hagas borrar este comentario por favor*/
                    window.location.href = "/EditarProducto.html";

                }
            });
        }

        // === OCULTAR ===
        /* A esta modal se le tiene que pasar algo del controllador para que oculte el producto en el index
        o algo asi xd no se de spring boot problema tuyo jose */
        else if (e.target.closest(".ocultar")) {
            Swal.fire({
                title: "¿Deseas ocultar este registro?",
                text: "Podrás mostrarlo nuevamente desde la sección de registros ocultos.",
                icon: "question",
                showCancelButton: true,
                confirmButtonText: "Sí, ocultar",
                cancelButtonText: "Cancelar",
                confirmButtonColor: "#6c757d",
                cancelButtonColor: "#3085d6",
                customClass: {
                    title: 'swal-title',
                    popup: 'swal-popup'
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        title: "Ocultado",
                        text: "El registro ha sido ocultado correctamente.",
                        icon: "info",
                        timer: 1500,
                        showConfirmButton: false,
                        customClass: {
                            title: 'swal-title',
                            popup: 'swal-popup'
                        }
                    });

                }
            });
        }
    });
    

    //pagination de las cartas del blog
    const cards = document.querySelectorAll(".blog__content .content__card");
    const paginationContainer = document.querySelector(".table__pagination .pagination__button");
    const prevButton = document.querySelector(".pasar--anterior");
    const nextButton = document.querySelector(".pasar--siguiente");

    const cardsPerPage = 9;
    let currentPage = 1;
    const totalPages = Math.ceil(cards.length / cardsPerPage);

    // Mostrar cartas por página
    function showPage(page) {
        const start = (page - 1) * cardsPerPage;
        const end = start + cardsPerPage;

        cards.forEach((card, index) => {
            card.style.display = index >= start && index < end ? "flex" : "none";
        });

        // Actualizar botón activo
        document.querySelectorAll(".button__item").forEach(btn => {
            btn.classList.toggle("active", parseInt(btn.textContent) === page);
        });
    }

    // Crear botones numéricos del paginador
    function createPaginationButtons() {
        // Limpiar botones anteriores si existen
        paginationContainer.querySelectorAll(".button__item").forEach(btn => btn.remove());

        // Crear siempre al menos un botón (aunque haya 9 o menos cartas)
        for (let i = 1; i <= (totalPages === 0 ? 1 : totalPages); i++) {
            const btn = document.createElement("button");
            btn.classList.add("button__item");
            btn.textContent = i;
            if (i === 1) btn.classList.add("active");

            btn.addEventListener("click", () => {
                currentPage = i;
                showPage(currentPage);
            });

            // Insertar antes del botón "Siguiente"
            paginationContainer.insertBefore(btn, nextButton);
        }
    }

    // Eventos de navegación
    prevButton.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            showPage(currentPage);
        }
    });

    nextButton.addEventListener("click", () => {
        if (currentPage < totalPages) {
            currentPage++;
            showPage(currentPage);
        }
    });

    // Inicializar
    createPaginationButtons();
    showPage(currentPage);


    // verificar de forma dinamica cuantos registros hay en los blog
    const contenedorBlogs = document.querySelector(".blog__content");
    const textoProductos = document.querySelector(".producto__texto");

    if (!contenedorBlogs || !textoProductos) return;

    function actualizarContador() {
        // Contar las cartas visibles (por si ocultas alguna)
        const cartas = Array.from(contenedorBlogs.querySelectorAll(".content__card"))
            .filter(card => getComputedStyle(card).display !== "none");

        const total = cartas.length;
        textoProductos.textContent = `Hay ${total} blog${total === 1 ? "" : "s"} registrado${total === 1 ? "" : "s"}`;
    }

    // Ejecutar al inicio
    actualizarContador();

    // Observar cambios en el contenedor (si se agregan o eliminan cartas)
    const observer = new MutationObserver(() => actualizarContador());
    observer.observe(contenedorBlogs, { childList: true, subtree: false });

});



