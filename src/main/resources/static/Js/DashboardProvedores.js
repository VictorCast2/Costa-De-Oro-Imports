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

    //llevar a la pagina de nueva compra
    document.querySelectorAll(".dropdown__item.compra").forEach(boton => {
        boton.addEventListener("click", () => {
            window.location.href = "NuevaCompra.html";
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
                    window.location.href = "/admin/provedores/update";
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


    // Paginación de las cards de proveedores
    const cards = document.querySelectorAll(".principal__container .container__card");
    const paginationContainer = document.querySelector(".pagination__button");
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
        // Limpiar botones previos
        paginationContainer.querySelectorAll(".button__item").forEach(btn => btn.remove());

        // Crear mínimo un botón
        for (let i = 1; i <= (totalPages === 0 ? 1 : totalPages); i++) {
            const btn = document.createElement("button");
            btn.classList.add("button__item");
            btn.textContent = i;

            if (i === 1) btn.classList.add("active");

            btn.addEventListener("click", () => {
                currentPage = i;
                showPage(currentPage);
            });

            // Insertar antes del botón siguiente
            paginationContainer.insertBefore(btn, nextButton);
        }
    }

    // Botón anterior
    prevButton.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            showPage(currentPage);
        }
    });

    // Botón siguiente
    nextButton.addEventListener("click", () => {
        if (currentPage < totalPages) {
            currentPage++;
            showPage(currentPage);
        }
    });

    // Inicializar
    createPaginationButtons();
    showPage(currentPage);

    // Selecciona el <p> donde irá el número dinámico
    const productoTexto = document.querySelector(".producto__texto");

    // Actualiza el texto automáticamente
    if (productoTexto) {
        const total = cards.length;
        productoTexto.textContent = `Hay ${total} proveedores registrados`;
    }
});