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

    //Filtro de solicitudes (por texto,tipo de solicitud y estado)
    (() => {
        const searchInput = document.getElementById("inputSearch");
        const selectSolicitud = document.getElementById("filtro-solicitud");
        const selectEstado = document.getElementById("filtro-estado");
        const cards = document.querySelectorAll(".content__card");
        const title = document.querySelector(".content__title");
        const contenedorCards = document.getElementById("contenedor-cards");

        if (!searchInput || !selectSolicitud || !selectEstado || !cards.length || !title || !contenedorCards) return;

        // Crear mensaje "No se encontraron solicitudes"
        const mensajeVacio = document.createElement("div");
        mensajeVacio.classList.add("content__card", "card__vacia");
        mensajeVacio.innerHTML = `
        <div class="card__information">
            <h3 class="information__title">No se encontraron solicitudes</h3>
            <p class="information__text">Prueba ajustando los filtros o busca con otras palabras clave.</p>
        </div>
        `;
        mensajeVacio.style.display = "none";
        contenedorCards.appendChild(mensajeVacio);

        const normalizarTexto = (texto) =>
            texto
                .toLowerCase()
                .normalize("NFD") // elimina tildes
                .replace(/[\u0300-\u036f]/g, "");

        const filtrarCards = () => {
            const texto = normalizarTexto(searchInput.value.trim());
            const solicitudSeleccionada = normalizarTexto(selectSolicitud.value.trim());
            const estadoSeleccionado = normalizarTexto(selectEstado.value.trim());

            let visibles = 0;

            cards.forEach((card) => {
                if (card.classList.contains("card__vacia")) return;

                const titulo = normalizarTexto(card.querySelector(".information__title")?.textContent || "");
                const textoInfo = normalizarTexto(card.querySelector(".information__text")?.textContent || "");
                const fecha = normalizarTexto(card.querySelector(".information__date")?.textContent || "");

                const solicitudBtn = card.querySelector(
                    ".button__item.Peticion, .button__item.Queja, .button__item.Reclamo, .button__item.Sugerencia"
                );
                const estadoBtn = card.querySelector(
                    ".button__item.Pendiente, .button__item.EnProceso, .button__item.Resuelto"
                );

                const solicitud = normalizarTexto(solicitudBtn ? solicitudBtn.textContent.trim() : "");
                const estado = normalizarTexto(estadoBtn ? estadoBtn.textContent.trim() : "");

                // filtro del input mediante texto
                const coincideTexto =
                    texto === "" ||
                    titulo.includes(texto) ||
                    textoInfo.includes(texto) ||
                    fecha.includes(texto);

                const coincideSolicitud = solicitudSeleccionada === "todos" || solicitud === solicitudSeleccionada;
                const coincideEstado = estadoSeleccionado === "todos" || estado === estadoSeleccionado;

                const mostrar = coincideTexto && coincideSolicitud && coincideEstado;
                card.style.display = mostrar ? "" : "none";

                if (mostrar) visibles++;
            });

            mensajeVacio.style.display = visibles === 0 ? "block" : "none";
            title.textContent = `Solicitudes (${visibles})`;
        };

        searchInput.addEventListener("input", filtrarCards);
        selectSolicitud.addEventListener("change", filtrarCards);
        selectEstado.addEventListener("change", filtrarCards);

        filtrarCards();
    })();

    //pagination del dashboard de pqrs
    const cards = document.querySelectorAll(".content__card:not(.card__vacia)");
    const paginationContainer = document.querySelector(".pagination__button");
    const prevButton = document.querySelector(".pasar--anterior");
    const nextButton = document.querySelector(".pasar--siguiente");

    const cardsPerPage = 6;
    let currentPage = 1;
    const totalPages = Math.ceil(cards.length / cardsPerPage);
    let permitirScroll = false;

    // Crear botones de número dinámicamente
    const createPaginationButtons = () => {
        // Elimina números previos si existen
        const oldButtons = paginationContainer.querySelectorAll(".button__item");
        oldButtons.forEach(btn => btn.remove());

        for (let i = 1; i <= totalPages; i++) {
            const button = document.createElement("button");
            button.classList.add("button__item");
            button.textContent = i;
            if (i === currentPage) button.classList.add("active");
            button.addEventListener("click", () => {
                currentPage = i;
                permitirScroll = true;
                updatePagination();
            });
            // Insertar los botones numéricos antes del botón "Siguiente"
            paginationContainer.insertBefore(button, nextButton);
        }
    };

    // Mostrar las cards según la página actual
    const showCards = () => {
        cards.forEach((card, index) => {
            card.style.display = "none";
            const start = (currentPage - 1) * cardsPerPage;
            const end = start + cardsPerPage;
            if (index >= start && index < end) {
                card.style.display = "block";
            }
        });
    };

    // Actualizar paginación visual y lógica
    const updatePagination = () => {
        showCards();
        paginationContainer.querySelectorAll(".button__item").forEach(btn => {
            btn.classList.remove("active");
            if (parseInt(btn.textContent) === currentPage) {
                btn.classList.add("active");
            }
        });

        // Desactivar botones prev/next cuando sea necesario
        prevButton.disabled = currentPage === 1;
        nextButton.disabled = currentPage === totalPages;

        // Si se cambia de página, mantener el scroll cerca del paginador
        if (permitirScroll) {
            const offsetTop = paginationContainer.getBoundingClientRect().top + window.scrollY - 100;
            window.scrollTo({
                top: offsetTop,
                behavior: "smooth"
            });
            permitirScroll = false;
        }
    };

    // Eventos de Anterior y Siguiente
    prevButton.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            permitirScroll = true;
            updatePagination();
        }
    });

    nextButton.addEventListener("click", () => {
        if (currentPage < totalPages) {
            currentPage++;
            permitirScroll = true;
            updatePagination();
        }
    });

    // Inicializar
    createPaginationButtons();
    updatePagination();

    // === Seleccionamos elementos ===
    const modal = document.getElementById("modalDetalle");
    const cerrarModal = document.getElementById("cerrarModal");
    const cardsmodal = document.querySelectorAll(".content__card");

    function abrirModal() {
        modal.style.display = "flex";
    }

    function cerrarModalVentana() {
        modal.style.display = "none";
    }

    document.addEventListener("click", (e) => {

        // ---- Manejo del dropdown (abrir/cerrar) ----
        const dropdowns = document.querySelectorAll(".dropdown");
        dropdowns.forEach((dropdown) => {
            const toggle = dropdown.querySelector(".dropdown__toggle");

            if (toggle && toggle.contains(e.target)) {
                dropdown.classList.toggle("show");
            }
            else if (!dropdown.contains(e.target)) {
                dropdown.classList.remove("show");
            }
        });

        // ---- Eliminar (SweetAlert) ----
        if (e.target.closest(".eliminar")) {
            e.stopPropagation();

            // Cerrar el dropdown padre inmediatamente
            const dropdownPadre = e.target.closest(".dropdown");
            if (dropdownPadre) dropdownPadre.classList.remove("show");

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

                    //Llamada a la api de eliminar si quieres jose
                }
            });

            return;
        }

        // ---- Abrir modal al hacer clic en la carta (excepto si es dropdown o eliminar) ----
        const card = e.target.closest(".content__card");
        if (card && !e.target.closest(".dropdown") && !e.target.closest(".eliminar")) {
            abrirModal();
            return;
        }

        // ---- Abrir modal al hacer clic en "Ver" del dropdown ----
        if (e.target.closest(".dropdown__item.ocultar")) {
            e.stopPropagation();

            // Cierra el dropdown antes de abrir la modal
            const dropdownAbierto = e.target.closest(".dropdown");
            if (dropdownAbierto) dropdownAbierto.classList.remove("show");

            abrirModal();
            return;
        }

        // ---- Cerrar modal al hacer clic fuera ----
        if (e.target === modal) {
            cerrarModalVentana();
        }
    });

    // ---- Cerrar modal con la X ----
    if (cerrarModal) {
        cerrarModal.addEventListener("click", cerrarModalVentana);
    }


    

});



