
export function activarGlassmorphism() {
    // Efecto glassmorphism solo al hacer scroll
    const header = document.querySelector('.header');

    window.addEventListener('scroll', () => {
        if (window.scrollY > 10) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
    });
}


export function addProductToCart({ name, price, img, qty = 1, openDrawer = true }) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    const existing = cart.find(p => p.name === name);
    if (existing) {
        existing.qty += qty;  // 🔥 ahora suma la cantidad correcta
    } else {
        cart.push({ name, price, img, qty });
    }

    localStorage.setItem("cart", JSON.stringify(cart));

    // Toast de confirmación
    Toastify({
        text: `${qty} x ${name} agregado al carrito`,
        duration: 2000,
        close: true,
        gravity: "top",
        position: "right",
        stopOnFocus: true,
        style: {
            background: "linear-gradient(to right, #00b09b, #96c93d)"
        }
    }).showToast();

    // Disparamos evento para que initCart refresque la UI
    document.dispatchEvent(new CustomEvent("cartUpdated", { detail: { openDrawer } }));
}

// Exporta initCart tal y como la tienes, pero agregando un listener para 'cartUpdated'
export function initCart() {
    const drawer = document.getElementById("cart-drawer");
    const overlay = document.getElementById("cart-overlay");
    const closeBtn = document.getElementById("cart-close");
    const cartList = document.getElementById("cart-list");
    const subtotalEl = document.getElementById("cart-subtotal");
    const cartCount = document.getElementById("cart-count");
    const cartIcon = document.getElementById("cart-icon");
    const seguirComprandoBtn = document.getElementById("seguir-comprando");

    // Footer
    const cartFooter = document.querySelector(".cart-footer");
    const subtotalContainer = cartFooter.querySelector(".cart-subtotal");
    const btnSeguir = cartFooter.querySelector("button:nth-child(2)");
    const btnFinalizar = cartFooter.querySelector("button:nth-child(3)");

    // Botón cerrar dinámico
    let btnCerrar = document.createElement("button");
    btnCerrar.className = "btn-primary";
    btnCerrar.textContent = "Cerrar";
    btnCerrar.addEventListener("click", closeCart);

    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    // Refrescar UI al cargar
    updateCart();

    // ESCUCHAR cuando otro módulo agregue algo al carrito
    document.addEventListener("cartUpdated", (e) => {
        cart = JSON.parse(localStorage.getItem("cart")) || [];
        updateCart();
        // Si quien disparó quiere abrir el drawer, lo hacemos
        if (e?.detail?.openDrawer) openCart();
    });

    // Agregar productos desde los íconos en cada card (usa la función pública)
    document.querySelectorAll(".card-icons .ri-shopping-cart-line").forEach(icon => {
        icon.addEventListener("click", () => {
            const card = icon.closest(".card");
            const name = card.querySelector(".description").textContent.trim();
            const price = parseFloat(card.querySelector(".price").textContent.replace("$", ""));
            const img = card.querySelector("img").src;

            // Llamamos la función pública que ya maneja localStorage, toast y evento
            addProductToCart({ name, price, img, openDrawer: true });
        });
    });

    // Abrir y cerrar drawer
    function openCart() {
        drawer.classList.add("is-open");
        overlay.classList.add("is-open");
    }

    function closeCart() {
        drawer.classList.remove("is-open");
        overlay.classList.remove("is-open");
    }

    closeBtn.addEventListener("click", closeCart);
    overlay.addEventListener("click", closeCart);
    cartIcon.addEventListener("click", openCart);

    //cerrar el carrito cuando quiera seguir comprando
    if (seguirComprandoBtn) {
        seguirComprandoBtn.addEventListener("click", closeCart);
    }

    // Guardar en localStorage
    function saveCart() {
        localStorage.setItem("cart", JSON.stringify(cart));
    }

    // Actualizar contador del header
    function updateCartCount() {
        const totalItems = cart.reduce((acc, p) => acc + p.qty, 0);
        cartCount.textContent = totalItems > 0 ? totalItems : "0";
    }

    // Actualizar UI del carrito
    function updateCart() {
        cartList.innerHTML = "";
        let subtotal = 0;

        if (cart.length === 0) {
            cartList.innerHTML = `<p class="empty-cart">No hay productos en el carrito.</p>`;

            // Ocultar subtotal cuando no haya productos
            subtotalContainer.style.display = "none";

            // Ocultar botones de compra
            btnSeguir.style.display = "none";
            btnFinalizar.style.display = "none";

            // Agregar botón cerrar si no está
            if (!cartFooter.contains(btnCerrar)) {
                cartFooter.appendChild(btnCerrar);
            }
        } else {
            cart.forEach((item, index) => {
                subtotal += item.price * item.qty;

                const li = document.createElement("li");
                li.className = "cart-item";
                li.innerHTML = `
                    <img src="${item.img}" alt="">
                    <div class="info">
                        <p class="name">${item.name}</p>
                        <div class="quantity-control" data-index="${index}">
                            <button class="minus">−</button>
                            <span class="qty">${item.qty}</span>
                            <button class="plus">+</button>
                        </div>
                        <span class="price">$${item.price.toFixed(2)}</span>
                    </div>
                    <button class="remove" data-index="${index}">
                        <i class="ri-delete-bin-6-line"></i>
                    </button>
                `;
                cartList.appendChild(li);
            });

            subtotalEl.textContent = "$" + subtotal.toFixed(2);

            //  Mostrar subtotal solo si hay productos
            subtotalContainer.style.display = "flex";

            // Mostrar botones de compra
            btnSeguir.style.display = "block";
            btnFinalizar.style.display = "block";

            // Eliminar botón cerrar si existe
            if (cartFooter.contains(btnCerrar)) {
                cartFooter.removeChild(btnCerrar);
            }

            // Eventos de + y -
            document.querySelectorAll(".quantity-control").forEach(control => {
                const i = control.dataset.index;

                // Botón menos
                control.querySelector(".minus").addEventListener("click", () => {
                    if (cart[i].qty > 0) {
                        cart[i].qty--;
                    }

                    if (cart[i].qty === 0) {
                        cart.splice(i, 1);
                    }

                    saveCart();
                    updateCart();
                });

                // Botón más
                control.querySelector(".plus").addEventListener("click", () => {
                    cart[i].qty++;
                    saveCart();
                    updateCart();
                });
            });

            // Eliminar producto manualmente
            document.querySelectorAll(".cart-item .remove").forEach(btn => {
                btn.addEventListener("click", e => {
                    const i = e.currentTarget.dataset.index;
                    cart.splice(i, 1);
                    saveCart();
                    updateCart();
                });
            });
        }

        // Siempre actualizar contador del header
        updateCartCount();
    }
}

export function inicialHeart() {
    const favCount = document.getElementById("fav-count");

    // Recuperar productos guardados
    let favoritos = JSON.parse(localStorage.getItem("favoritos")) || [];
    let count = favoritos.length;
    favCount.textContent = count;

    // Seleccionar todos los íconos de corazón
    const addToFavCount = document.querySelectorAll(".card-icons .ri-heart-line");

    addToFavCount.forEach(icon => {
        icon.addEventListener("click", () => {
            const card = icon.closest(".card");

            // Extraer datos del producto
            const producto = {
                imagen: card.querySelector("img").src,
                nombre: card.querySelector(".description").textContent.trim(),
                precio: card.querySelector(".price").textContent.trim(),
                estado: "En Stock"
            };

            // Evitar duplicados
            if (!favoritos.some(p => p.nombre === producto.nombre)) {
                favoritos.push(producto);

                // Actualizar contador
                count = favoritos.length;
                favCount.textContent = count;

                // Guardar en localStorage
                localStorage.setItem("favoritos", JSON.stringify(favoritos));

                Toastify({
                    text: "Agregado a favoritos",
                    duration: 2000,
                    style: {
                        background: "linear-gradient(to right, #ff416c, #ff4b2b)",
                    }
                }).showToast();
            }
        });
    });
}

export function rederigirFav() {
    document.getElementById("go-fav").addEventListener("click", () => {
        window.location.href = "Favorito.html";
    });
}

export function finalizarCompra() {
    //cuando presiona el boton de finalizar lo lleva
    // para el carrito junto con el producto agregado al carrito
    const btnFinalizarcompra = document.getElementById("finalizar-compra");

    btnFinalizarcompra.addEventListener("click", () => {
        window.location.href = "Carrito.html"; // redirige a la página del carrito
    });
}

export function desplegablePerfil() {
    /* Menú desplegable del perfil */
    const subMenu = document.getElementById("SubMenu");
    const profileImage = document.querySelector(".ri-user-line");

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
}

export function carruselProductos() {
    // mostrar los productos con carrusel
    document.querySelectorAll(".flex").forEach(carrusel => {
        const track = carrusel.querySelector(".flex__productos-track");
        const prevBtn = carrusel.querySelector(".arrow--left");
        const nextBtn = carrusel.querySelector(".arrow--right");

        const cardWidth = 300; // ancho de cada card
        const gap = 40;        // espacio entre cards
        const visibles = 4;    // cuántos se muestran a la vez

        let posicion = 0;
        const totalProductos = track.querySelectorAll(".card").length;
        const maxPosicion = (totalProductos - visibles) * (cardWidth + gap);

        const paso = visibles * (cardWidth + gap); // mueve 4 productos

        nextBtn.addEventListener("click", () => {
            if (posicion < maxPosicion) {
                posicion += paso;
                if (posicion > maxPosicion) posicion = maxPosicion; // no pasar límite
                track.style.transform = `translateX(-${posicion}px)`;
            }
        });

        prevBtn.addEventListener("click", () => {
            if (posicion > 0) {
                posicion -= paso;
                if (posicion < 0) posicion = 0; // no pasar inicio
                track.style.transform = `translateX(-${posicion}px)`;
            }
        });
    });
}

export function verProductos() {
    // mandar los productos a la pagina ver
    const viewIcons = document.querySelectorAll(".card-icons .ri-eye-line");

    viewIcons.forEach(icon => {
        icon.addEventListener("click", (e) => {
            const card = e.target.closest(".card");

            // Capturamos los datos de la card
            const productData = {
                name: card.dataset.name,
                price: card.dataset.price,
                oldPrice: card.dataset.oldprice,
                image: card.dataset.image,
                category: card.dataset.category,
                subcategory: card.dataset.subcategory
            };

            // Guardar en localStorage
            localStorage.setItem("selectedProduct", JSON.stringify(productData));

            // Redirigir a Ver.html
            window.location.href = "Ver.html";
        });
    });

}

document.addEventListener('DOMContentLoaded', () => {

    if (!localStorage.getItem("mayorDeEdadAceptado")) {
        Swal.fire({
            html: `
        <div class="contenedor-imagen-modal">
            <img src="/static/Assets/Img/logos/costaoroimport.png" 
            alt="Mayor de edad" 
            class="mi-imagen-modal">
        </div>
        <h2 class="swal2-title">¿ Eres Mayor De Edad ?</h2>
        <p class="texto-advertencia">
            "Prohíbese El Expendio De Bebidas Embriagantes A Menores De Edad"<br>
            "El Exceso De Alcohol Es Perjudicial Para La Salud"
        </p>
        `,
            showCancelButton: true,
            confirmButtonText: 'Sí, Soy Mayor De Edad',
            cancelButtonText: 'No, Soy Menor De Edad',
            allowOutsideClick: false,
            allowEscapeKey: false,
            backdrop: `rgba(0,0,0,0.8)`,
            width: '1000px',
            customClass: {
                popup: 'swal-popup'
            }
        }).then((result) => {
            if (result.isConfirmed) {
                // Guardamos en localStorage que ya aceptó
                localStorage.setItem("mayorDeEdadAceptado", "true");

            } else {
                // Si no acepta, lo manda a Google
                window.location.href = "https://www.google.com";
            }
        });
    }

    //desplegar menu del usuario
    desplegablePerfil();

    //rederigir a Favorito
    rederigirFav();

    //Llamamos a la funcion
    activarGlassmorphism();

    //Carrusel del inicio
    const slides = document.querySelectorAll(".carousel__slide");
    const prevBtn = document.querySelector("[data-direction='prev']");
    const nextBtn = document.querySelector("[data-direction='next']");
    const dotsContainer = document.querySelector(".carousel__dots");
    const heroCarousel = document.querySelector(".hero__carousel");
    let currentSlide = 0;
    let autoPlayInterval;

    // Crear los puntos dinámicamente según el número de slides
    slides.forEach((_, index) => {
        const dot = document.createElement("span");
        dot.classList.add("carousel__dot");
        dot.dataset.index = index;
        dotsContainer.appendChild(dot);
    });

    const dots = document.querySelectorAll(".carousel__dot");

    // Mostrar slide por índice
    function showSlide(index) {
        slides.forEach((slide, i) => {
            slide.classList.toggle("active", i === index);
            dots[i].classList.toggle("active", i === index);
        });

        const activeSlide = slides[index];
        const theme = activeSlide.getAttribute("data-theme");

        // Cambiar fondo o estilos de tema según el data-theme
        heroCarousel.classList.remove("hero--paulaner", "hero--tequila", "hero--budweiser", "hero--whiskys", "hero--packmixtos");
        heroCarousel.classList.add(`hero--${theme}`);

        currentSlide = index;
    }

    // Funciones para avanzar y retroceder
    function nextSlide() {
        const nextIndex = (currentSlide + 1) % slides.length;
        showSlide(nextIndex);
    }

    function prevSlide() {
        const prevIndex = (currentSlide - 1 + slides.length) % slides.length;
        showSlide(prevIndex);
    }

    // Autoplay
    function startAutoplay() {
        autoPlayInterval = setInterval(nextSlide, 6000);
    }

    function stopAutoplay() {
        clearInterval(autoPlayInterval);
    }

    // Eventos de botones
    nextBtn.addEventListener("click", () => {
        nextSlide();
        stopAutoplay();
        startAutoplay();
    });

    prevBtn.addEventListener("click", () => {
        prevSlide();
        stopAutoplay();
        startAutoplay();
    });

    // Eventos de los puntos
    dots.forEach(dot => {
        dot.addEventListener("click", () => {
            const index = parseInt(dot.dataset.index);
            showSlide(index);
            stopAutoplay();
            startAutoplay();
        });
    });

    // Inicialización
    showSlide(0);
    startAutoplay();

    //duplicacion de los logos
    const slider = document.querySelector('.logos__slide');
    if (!slider.nextElementSibling || !slider.nextElementSibling.classList.contains('logos__slide')) {
        const clone = slider.cloneNode(true);
        slider.parentElement.appendChild(clone);
    }

    //invocar el iniciar carrito y corazon
    inicialHeart();

    initCart();

    //finalizar compra
    finalizarCompra();

    // mandar los productos a la pagina ver
    verProductos();

    // mostrar los productos con carrusel
    carruselProductos();

    //cambiar anio del footer automaticamente
    document.getElementById("anio__pagina").textContent = new Date().getFullYear();

});