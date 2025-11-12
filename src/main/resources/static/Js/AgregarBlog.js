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


    // Imagen del producto
    const boxImagen = document.querySelector('.formulario__boximagen');
    const textoBox = document.querySelector('.boximagen__texto');
    const errorFormato = document.querySelector('.error--formato');
    const errorVacio = document.querySelector('.error--vacio');

    const formatosPermitidos = ["image/jpeg", "image/png", "image/webp"];

    // Crear dinámicamente input file oculto
    const inputFile = document.createElement('input');
    inputFile.type = 'file';
    inputFile.accept = 'image/*';
    inputFile.style.display = 'none';
    boxImagen.appendChild(inputFile);

    // Crear contenedor de previsualización
    const previewContainer = document.createElement('div');
    previewContainer.classList.add('preview-container');
    boxImagen.appendChild(previewContainer);

    // Al hacer clic en el contenedor, abrir explorador
    boxImagen.addEventListener('click', () => inputFile.click());

    // Escuchar cambio del input
    inputFile.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (!file) return;

        // Validar formato
        if (!formatosPermitidos.includes(file.type)) {
            errorFormato.style.display = 'block';
            errorVacio.style.display = 'none';
            previewContainer.innerHTML = '';
            previewContainer.style.display = 'none';
            boxImagen.classList.remove('imagen-activa');
            return;
        }

        // Si el formato es válido → ocultar errores
        errorFormato.style.display = 'none';
        errorVacio.style.display = 'none';
        boxImagen.style.border = '1px solid #ddd';

        // Crear barra de carga
        previewContainer.innerHTML = `<div class="progress-bar"></div>`;
        previewContainer.style.display = 'flex';
        boxImagen.classList.add('imagen-activa');

        const progressBar = previewContainer.querySelector('.progress-bar');

        // Simular carga
        setTimeout(() => {
            const img = document.createElement('img');
            img.src = URL.createObjectURL(file);

            previewContainer.innerHTML = '';
            previewContainer.appendChild(img);

            // Crear botón eliminar (solo si no existe)
            let removeBtn = boxImagen.querySelector('.remove-image');
            if (!removeBtn) {
                removeBtn = document.createElement('span');
                removeBtn.classList.add('remove-image');
                removeBtn.textContent = 'Eliminar imagen';
                boxImagen.appendChild(removeBtn);
            }

            // Acción eliminar
            removeBtn.addEventListener('click', (e) => {
                e.stopPropagation(); // evita que abra el explorador

                // Eliminar imagen y botón
                previewContainer.style.display = 'none';
                previewContainer.innerHTML = '';
                boxImagen.classList.remove('imagen-activa');
                removeBtn.remove();
                inputFile.value = ''; // limpiar el input
            });
        }, 2000);
    });

    //Descripcion de un producto
    const toolbarOptions = [
        [{ 'font': [] }, { 'size': [] }],           // Tipografía y tamaño
        ['bold', 'italic', 'underline', 'strike'],  // Estilo de texto
        [{ 'color': [] }, { 'background': [] }],    // Color del texto y fondo
        [{ 'script': 'sub' }, { 'script': 'super' }],// Sub/superíndice
        [{ 'header': '1' }, { 'header': '2' }, 'blockquote', 'code-block'],
        [{ 'list': 'ordered' }, { 'list': 'bullet' }],
        [{ 'indent': '-1' }, { 'indent': '+1' }],
        [{ 'align': [] }],
        ['link', 'image', 'video'],                 // Enlaces, imágenes, videos
        ['clean']                                   // Limpiar formato
    ];

    const quill = new Quill('#editor-container', {
        modules: { toolbar: toolbarOptions },
        theme: 'snow'
    });


    //inputs de fecha y hora con js son mas bonitos que los nativos
    flatpickr("#fechapublicacion", {
        dateFormat: "Y-m-d", // formato YYYY-MM-DD
        minDate: "today",   // no dejar elegir fechas pasadas

    });

    //Validaciones del formulario de agregar blog
    const fieldsBlog = {
        titulo: { regex: /^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]{3,}$/, errorMessage: "El titulo debe tener al menos 3 letras." },
        fechapublicacion: { regex: /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/, errorMessage: "Por favor eliga una fecha de publicacion" },
        historiacompleta: { regex: /^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]{3,}$/, errorMessage: "La historia completa debe tener al menos 3 letras y no contener números." },
    };


    Object.keys(fieldsBlog).forEach(fieldId => {
        const input = document.getElementById(fieldId);
        const inputBox = input.closest(".formulario__box");
        const checkIcon = inputBox.querySelector(".ri-check-line");
        const errorIcon = inputBox.querySelector(".ri-close-line");
        const errorMessage = inputBox.nextElementSibling;

        input.addEventListener("input", () => {
            const value = input.value.trim();
            const label = inputBox.querySelector("box__label");

            if (value === "") {
                inputBox.classList.remove("input-error");
                checkIcon.style.display = "none";
                errorIcon.style.display = "none";
                errorMessage.style.display = "none";
                input.style.border = "";
                if (label) label.classList.remove("error");
            } else if (fieldsBlog[fieldId].regex.test(value)) {
                checkIcon.style.display = "inline-block";
                errorIcon.style.display = "none";
                errorMessage.style.display = "none";
                input.style.border = "2px solid #0034de";
                inputBox.classList.remove("input-error");
                if (label) label.classList.remove("error");
            } else {
                checkIcon.style.display = "none";
                errorIcon.style.display = "inline-block";
                errorMessage.style.display = "block";
                input.style.border = "2px solid #fd1f1f";
                inputBox.classList.add("input-error");
                if (label) label.classList.add("error");
            }
        });
    });

    // Obtener radios y mensaje de error
    const radiosEstado = document.querySelectorAll('input[name="estado"]');
    const errorEstado = document.querySelector('.error--estado');
    const radiosCustom = document.querySelectorAll('.radio__custom');

    // Quitar el error cuando el usuario selecciona un estado
    radiosEstado.forEach(radio => {
        radio.addEventListener('change', () => {
            errorEstado.style.display = 'none';
            radiosCustom.forEach(r => r.classList.remove('error')); // quita borde rojo
        });
    });

    //Validacion de la imagen 
    function validarImagenes() {
        const file = inputFile.files[0];

        // Reiniciar errores y borde
        errorFormato.style.display = "none";
        errorVacio.style.display = "none";
        boxImagen.style.border = "1px solid #ddd"; // borde por defecto

        // Si no hay archivo seleccionado
        if (!file) {
            errorVacio.style.display = "block";
            boxImagen.style.border = "2px solid #e53935"; // borde rojo
            return false;
        }

        // Si el formato no es permitido
        if (!formatosPermitidos.includes(file.type)) {
            errorFormato.style.display = "block";
            boxImagen.style.border = "2px solid #e53935"; // borde rojo
            return false;
        }

        // Si todo está correcto → volver al borde normal
        boxImagen.style.border = "1px solid #ddd";
        return true;
    }


    // Descripción del producto (validación)
    const boxDescripcion = document.querySelector('.formulario__boxdescripcion');
    const errorDescripcion = document.querySelector('.error--descripcion');

    // Función de validación
    function validarDescripcion() {
        const contenido = quill.getText().trim(); // Obtiene solo texto plano (sin formato)

        // Reiniciar estado
        errorDescripcion.style.display = 'none';
        boxDescripcion.classList.remove('error-border');

        if (contenido === '' || contenido.length === 0) {
            errorDescripcion.style.display = 'block';
            boxDescripcion.classList.add('error-border');
            return false;
        }

        return true;
    }

    // Ocultar el error al escribir
    quill.on('text-change', () => {
        const contenido = quill.getText().trim();
        if (contenido.length > 0) {
            errorDescripcion.style.display = 'none';
            boxDescripcion.classList.remove('error-border');
        }
    });


    const addform = document.getElementById("formularioBlog");

    addform.addEventListener("submit", function (e) {
        let formularioValido = true;
        let selectsValidos = true;


        // 2. Validar inputs (solo si el checkbox está marcado)
        Object.keys(fieldsBlog).forEach(fieldId => {
            const input = document.getElementById(fieldId);
            const regex = fieldsBlog[fieldId].regex;
            const inputBox = input.closest(".formulario__box");
            const checkIcon = inputBox.querySelector(".ri-check-line");
            const errorIcon = inputBox.querySelector(".ri-close-line");
            const errorMessage = inputBox.nextElementSibling;
            const value = input.value.trim();

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

        // Validar estado (radio buttons)
        const estadoSeleccionado = Array.from(radiosEstado).some(radio => radio.checked);

        if (!estadoSeleccionado) {
            formularioValido = false;
            errorEstado.style.display = 'block';
            radiosCustom.forEach(r => r.classList.add('error')); // agrega borde rojo
        } else {
            radiosCustom.forEach(r => r.classList.remove('error'));
        }


        // Validar imagen antes de enviar
        if (!validarImagenes()) {
            formularioValido = false;
        }


        //validar descripcion antes de enviar
        if (!validarDescripcion()) {
            formularioValido = false;
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