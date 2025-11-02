import { activarGlassmorphism, inicialHeart, initCart, rederigirFav } from "./main.js";

document.addEventListener('DOMContentLoaded', () => {

    /* console.log("JS cargado correctamente");
    console.log("favTableBody:", favTableBody);
    console.log("prevPageBtn:", prevPageBtn);
    console.log("nextPageBtn:", nextPageBtn); */

    // Usar la funcion activarGlassmorphism
    activarGlassmorphism();

    inicialHeart();

    initCart();

    //rellenar la tabla mediante localStogare 
    const cartTableBody = document.getElementById("favoritos-body");
    const paginationText = document.getElementById("pagination-text");
    const prevPageBtn = document.getElementById("prev-page");
    const nextPageBtn = document.getElementById("next-page");
    const paginationButtonsContainer = document.getElementById("pagination-buttons");
    const thereSpan = document.getElementById("there?");
    const totalEl = document.getElementById("content__precio");

    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    let currentPage = 1;
    const rowsPerPage = 4;

    // --- Calcular total del carrito ---
    function updateCartTotal() {
        const subtotal = cart.reduce((acc, item) => acc + (parseInt(item.price) * item.qty), 0);
        totalEl.textContent = `$${parseInt(subtotal).toLocaleString('es-CO')}`;

        // Obtener cupón (siempre será número aunque empiece en $0.00)
        const cuponEl = document.getElementById("cupon__envio");
        const cuponValue = parseInt(cuponEl.textContent.replace(/[^0-9.-]+/g, "")) || 0;

        // Envío (siempre gratis en tu HTML actual → 0)
        const envio = 0;

        // Calcular total final
        const totalFinal = subtotal - cuponValue + envio;

        // Actualizar el total en pantalla
        const totalAllEl = document.getElementById("total__all");
        totalAllEl.textContent = `$${parseInt(totalFinal).toLocaleString('es-CO')}`;
    }

    // --- Render tabla ---
    function renderTable() {
        cartTableBody.innerHTML = "";

        if (cart.length === 0) {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td colspan="6" style="text-align:center; padding:20px; font-weight:600;">
                    No hay productos en el carrito
                </td>
            `;
            cartTableBody.appendChild(tr);

            paginationText.textContent = `Mostrando 0-0 de 0`;
            paginationButtonsContainer.innerHTML = "";
            prevPageBtn.disabled = true;
            nextPageBtn.disabled = true;
            thereSpan.textContent = `Hay 0 productos en el carrito.`;

            // Si no hay productos, el total debe ser 0
            updateCartTotal();
            return;
        }

        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        const paginatedItems = cart.slice(start, end);

        paginatedItems.forEach((item, index) => {
            const globalIndex = start + index; // posición real en el array
            const subtotal = parseInt(item.price) * item.qty;

            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td><img src="${item.img}" width="50"/></td>
                <td>${item.name}</td>
                <td>
                    <div class="quantity-control" data-index="${globalIndex}">
                        <button class="minus">−</button>
                        <span class="qty">${item.qty}</span>
                        <button class="plus" ${item.stock !== null && item.qty >= item.stock ? 'disabled style="opacity:0.5; cursor:not-allowed;"' : ''}>+</button> <!-- VALIDAR STOCK -->
                    </div>
                </td>
                <td>$${parseInt(item.price).toLocaleString('es-CO')}</td>
                <td>$${parseInt(subtotal).toLocaleString('es-CO')}</td>
                <td>
                    <button class="content__icon btn-delete" data-index="${globalIndex}">
                        <i class="ri-delete-bin-6-line" title="Eliminar"></i>
                    </button>
                </td>
            `;
            cartTableBody.appendChild(tr);
        });

        paginationText.textContent = `Mostrando ${start + 1}-${Math.min(end, cart.length)} de ${cart.length}`;
        thereSpan.textContent = `Hay ${cart.length} productos en el carrito.`;

        renderPaginationButtons();

        // Actualizar el total del carrito
        updateCartTotal();
    }

    // --- Render botones de paginación ---
    function renderPaginationButtons() {
        paginationButtonsContainer.innerHTML = "";
        const totalPages = Math.ceil(cart.length / rowsPerPage);

        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement("button");
            btn.textContent = i;
            btn.classList.add("pagination__btn");
            if (i === currentPage) btn.classList.add("active");

            btn.addEventListener("click", () => {
                currentPage = i;
                renderTable();
            });

            paginationButtonsContainer.appendChild(btn);
        }

        prevPageBtn.disabled = currentPage === 1;
        nextPageBtn.disabled = currentPage === totalPages;
    }

    // --- Eventos flechas ---
    prevPageBtn.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            renderTable();
        }
    });

    nextPageBtn.addEventListener("click", () => {
        if (currentPage < Math.ceil(cart.length / rowsPerPage)) {
            currentPage++;
            renderTable();
        }
    });

    // --- Delegación: eliminar + actualizar cantidad ---
    cartTableBody.addEventListener("click", (e) => {
        // Eliminar producto
        const btn = e.target.closest(".btn-delete");
        if (btn) {
            const index = parseInt(btn.getAttribute("data-index"));
            Swal.fire({
                title: "¿Estás seguro?",
                text: "¡No podrás revertir esto!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Cancelar",
                confirmButtonText: "Sí, eliminar",
                customClass: {
                    title: 'swal-title',
                    popup: 'swal-popup'
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    cart.splice(index, 1);
                    localStorage.setItem("cart", JSON.stringify(cart));

                    if ((currentPage - 1) * rowsPerPage >= cart.length && currentPage > 1) {
                        currentPage--;
                    }

                    renderTable();

                    Swal.fire({
                        title: "¡Eliminado!",
                        text: "El producto ha sido eliminado.",
                        icon: "success",
                        customClass: {
                            title: 'swal-title',
                            popup: 'swal-popup'
                        }
                    });

                }
            });
            return;
        }

        // Aumentar / disminuir cantidad
        if (e.target.classList.contains("plus") || e.target.classList.contains("minus")) {
            const control = e.target.closest(".quantity-control");
            const index = parseInt(control.getAttribute("data-index"));

            if (e.target.classList.contains("plus")) {
                if (cart[index].stock !== null && cart[index].qty >= cart[index].stock) {
                    Swal.fire({
                        title: "Stock insuficiente",
                        text: `No hay más stock disponible. Máximo: ${cart[index].stock} unidades`,
                        icon: "warning",
                        confirmButtonText: "Entendido",
                        customClass: {
                            title: 'swal-title',
                            popup: 'swal-popup'
                        }
                    });
                    return;
                }
                cart[index].qty++;
            } else if (e.target.classList.contains("minus") && cart[index].qty > 1) {
                cart[index].qty--;
            }

            localStorage.setItem("cart", JSON.stringify(cart));
            renderTable();
        }
    });

    // --- Render inicial ---
    renderTable();

    //rederigir a Favorito
    rederigirFav();

    // Mostrar mensaje al cargar si se registró correctamente
    window.addEventListener("DOMContentLoaded", () => {
        if (sessionStorage.getItem("loginSuccess") === "true") {
            Swal.fire({
                title: "Registro de fecha y hora exito",
                icon: "success",
                timer: 3000,
                draggable: true,
                timerProgressBar: true,
                customClass: {
                    title: 'swal-title',
                    popup: 'swal-popup'
                }
            });
            sessionStorage.removeItem("loginSuccess");
        }
    });

});