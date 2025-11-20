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

    //inputs de fecha y hora con js son mas bonitos que los nativos
    flatpickr("#fechaemision", {
        dateFormat: "Y-m-d", // formato YYYY-MM-DD
        minDate: "today",   // no dejar elegir fechas pasadas

    });

    //input de precios
    function formatCOP(value) {
        const number = Number(value.replace(/\D/g, '')); // eliminar caracteres no numéricos
        if (isNaN(number)) return '';
        return number.toLocaleString('es-CO', {
            style: 'currency',
            currency: 'COP',
            maximumFractionDigits: 0
        });
    }

    function setupMoneyInput(inputOrId) {

        // si se pasa un ID como string, se obtiene el elemento
        const input = typeof inputOrId === "string"
            ? document.getElementById(inputOrId)
            : inputOrId;

        if (!input) return;

        input.addEventListener('input', e => {
            const value = e.target.value.replace(/\D/g, '');
            e.target.value = value ? formatCOP(value) : '';
        });

        input.addEventListener('keydown', e => {
            const currentValue = Number(input.value.replace(/\D/g, '')) || 0;

            if (e.key === 'ArrowUp') {
                e.preventDefault();
                input.value = formatCOP(currentValue + 1000);
            }

            if (e.key === 'ArrowDown') {
                e.preventDefault();
                input.value = formatCOP(Math.max(0, currentValue - 1000));
            }
        });
    }

    // aplicar a ambos inputs
    setupMoneyInput('preciocompra');
    setupMoneyInput('subtotal');


    //Validaciones del formulario de agregar producto
    const fieldsProducto = {
        numerofactura: { regex: /^#[A-Za-z0-9]+$/, errorMessage: "El numero de la factura debe iniciar con el símbolo # y contener solo letras o números después." },
        fechaemision: { regex: /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/, errorMessage: "Por favor eliga una fecha de emision" },
        cantidadproductos: { regex: /^[1-9][0-9]*$/, errorMessage: "ingresa una cantidad de productos" },
        preciocompra: { regex: /^(?!0+(?:[.,]0+)?$)\d+(?:[.,]\d{1,2})?$/, errorMessage: "ingresa un precio de compra " },
        subtotal: { regex: /^(?!0+(?:[.,]0+)?$)\d+(?:[.,]\d{1,2})?$/, errorMessage: "ingresa un subtotal de compra " }
    };

    // obtenemos los select
    const selectProductos = document.getElementById("Productos");
    const errorProductos = document.querySelector(".error--productos");

    Object.keys(fieldsProducto).forEach(fieldId => {
        const input = document.getElementById(fieldId);
        const inputBox = input.closest(".formulario__box");
        const checkIcon = inputBox.querySelector(".ri-check-line");
        const errorIcon = inputBox.querySelector(".ri-close-line");
        const errorMessage = inputBox.nextElementSibling;

        input.addEventListener("input", () => {
            let value = input.value.trim();

            if (fieldId === "preciocompra" || fieldId === "subtotal") {
                value = value.replace(/[^\d]/g, ''); // elimina $, puntos, etc.
            }

            const label = inputBox.querySelector("box__label");

            if (value === "") {
                inputBox.classList.remove("input-error");
                checkIcon.style.display = "none";
                errorIcon.style.display = "none";
                errorMessage.style.display = "none";
                input.style.border = "";
                if (label) label.classList.remove("error"); //lo reseteas
            } else if (fieldsProducto[fieldId].regex.test(value)) {
                checkIcon.style.display = "inline-block";
                errorIcon.style.display = "none";
                errorMessage.style.display = "none";
                input.style.border = "2px solid #0034de";
                inputBox.classList.remove("input-error");
                if (label) label.classList.remove("error"); //quitar rojo cuando es válido
            } else {
                checkIcon.style.display = "none";
                errorIcon.style.display = "inline-block";
                errorMessage.style.display = "block";
                input.style.border = "2px solid #fd1f1f";
                inputBox.classList.add("input-error");
                if (label) label.classList.add("error"); // marcar rojo cuando es inváli
            }
        });
    });

    // Ocultar advertencias y errores de select al interactuar
    [selectProductos].forEach(select => {
        select.addEventListener("change", () => {

            if (select.selectedIndex > 0) {
                select.style.border = "2px solid #0034de";
            } else {
                select.style.border = "";
            }

            if (select === selectProductos && select.selectedIndex > 0) {
                errorProductos.style.display = "none";
            }

        });
    });

    //agregar nuevas filas de productos
    const contenedorFilas = document.getElementById("contenedor-filas");
    const btnAgregarFila = document.querySelector(".btn-agregar-fila");

    // Guardamos la fila original
    const filaBase = document.querySelector(".fila-producto");

    btnAgregarFila.addEventListener("click", () => {
        const nuevaFila = filaBase.cloneNode(true);

        // limpiar inputs
        nuevaFila.querySelectorAll("input").forEach(input => {
            input.value = "";
            input.style.border = "";
        });

        // limpiar selects
        nuevaFila.querySelectorAll("select").forEach(select => {
            select.selectedIndex = 0;
            select.style.border = "";
        });

        // ocultar errores e iconos
        nuevaFila.querySelectorAll(".formulario__error").forEach(e => e.style.display = "none");
        nuevaFila.querySelectorAll(".ri-check-line").forEach(i => i.style.display = "none");
        nuevaFila.querySelectorAll(".ri-close-line").forEach(i => i.style.display = "none");

        // Insertar fila
        contenedorFilas.appendChild(nuevaFila);

        // ACTIVAR FORMAT COP otra vez
        nuevaFila.querySelectorAll(".validar-numerico").forEach(input => setupMoneyInput(input));

        // ACTIVAR VALIDACIONES otra vez
        activarValidacionesFila(nuevaFila);
    });

    function activarValidacionesFila(fila) {
        const inputs = fila.querySelectorAll(".validar-input");
        const selects = fila.querySelectorAll(".validar-select");

        // VALIDACIÓN DE INPUTS
        inputs.forEach(input => {
            const idCampo = input.getAttribute("id").replace(/[0-9]/g, "");

            input.addEventListener("input", () => validarCampoDinamico(input, idCampo));
        });

        // VALIDACIÓN DE SELECT
        selects.forEach(select => {
            select.addEventListener("change", () => {
                if (select.selectedIndex > 0) {
                    select.style.border = "2px solid #0034de";
                    select.parentElement.nextElementSibling.style.display = "none";
                } else {
                    select.style.border = "2px solid #fd1f1f";
                    select.parentElement.nextElementSibling.style.display = "block";
                }
            });
        });
    }

    function validarCampoDinamico(input, idCampo) {
        const reglas = fieldsProducto[idCampo];
        if (!reglas) return;

        let value = input.value.trim();

        // eliminar $ y puntos para validar números
        if (idCampo === "preciocompra" || idCampo === "subtotal") {
            value = value.replace(/[^\d]/g, "");
        }

        const box = input.closest(".formulario__box");
        const checkIcon = box.querySelector(".ri-check-line");
        const closeIcon = box.querySelector(".ri-close-line");
        const errorMessage = box.nextElementSibling;

        if (value === "") {
            input.style.border = "";
            checkIcon.style.display = "none";
            closeIcon.style.display = "none";
            errorMessage.style.display = "none";
            return;
        }

        if (reglas.regex.test(value)) {
            input.style.border = "2px solid #0034de";
            checkIcon.style.display = "inline-block";
            closeIcon.style.display = "none";
            errorMessage.style.display = "none";
        } else {
            input.style.border = "2px solid #fd1f1f";
            checkIcon.style.display = "none";
            closeIcon.style.display = "inline-block";
            errorMessage.style.display = "block";
        }
    }

    activarValidacionesFila(filaBase);


    const addform = document.getElementById("formularioProducto");

    addform.addEventListener("submit", function (e) {
        let formularioValido = true;
        let selectsValidos = true;


        // 2. Validar inputs (solo si el checkbox está marcado)
        Object.keys(fieldsProducto).forEach(fieldId => {
            const input = document.getElementById(fieldId);
            const regex = fieldsProducto[fieldId].regex;


            const inputBox = input.closest(".formulario__box");
            const checkIcon = inputBox.querySelector(".ri-check-line");
            const errorIcon = inputBox.querySelector(".ri-close-line");
            const errorMessage = inputBox.nextElementSibling;
            let value = input.value.trim();

            if (fieldId === "preciocompra" || fieldId === "subtotal") {
                value = value.replace(/[^\d]/g, '');
            }

            if (!regex.test(value)) {
                formularioValido = false;
                checkIcon.style.display = "none";
                errorIcon.style.display = "inline-block";
                errorMessage.style.display = "block";
                input.style.border = "2px solid #fd1f1f";
                const label = inputBox.querySelector("box__label");
                if (label) label.classList.add("error"); // marcar error
                inputBox.classList.add("input-error");
            } else {
                const label = inputBox.querySelector("box__label");
                if (label) label.classList.remove("error"); // quitar error si es válido
            }
        });

        const ProductosSeleccionada = selectProductos.selectedIndex > 0;

        if (!ProductosSeleccionada) {
            selectsValidos = false;
            errorProductos.style.display = "block";
            selectProductos.style.border = "2px solid #fd1f1f";
        }

        // Mostrar error si algo está mal
        if (!formularioValido || !selectsValidos) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Por favor rellene el formulario correctamente",
                customClass: {
                    title: 'swal-title',
                    popup: 'swal-popup'
                }
            });
            e.preventDefault();
            return;
        }

        // Caso éxito
        sessionStorage.setItem("loginSuccess", "true");
    });

    // Al cargar la página, revisamos si hay bandera de login
    window.addEventListener("DOMContentLoaded", () => {
        if (sessionStorage.getItem("loginSuccess") === "true") {
            Swal.fire({
                title: "Registro exitoso",
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