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

    // funcionamiento de las dos categorias en el formulario
    const inputSub = document.getElementById("Subcategoria");
    const listaSub = document.getElementById("subcategoriaLista");

    // Creamos contenedor visual para tags dentro del input
    const contenedorTags = document.createElement("div");
    contenedorTags.id = "subcategoriaTags";
    inputSub.parentNode.insertBefore(contenedorTags, inputSub);

    let subcategorias = [];
    let listaVisible = false;

    inputSub.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            const valor = inputSub.value.trim();
            if (valor === "") return;
            if (subcategorias.includes(valor.toLowerCase())) return;

            subcategorias.push(valor.toLowerCase());
            inputSub.value = "";
            actualizarInterfaz();
        }
    });


    // Mostrar la lista al hacer clic o enfocar el input
    inputSub.addEventListener("focus", () => {
        listaVisible = true;
        listaSub.classList.add("visible");
        listaSub.classList.remove("oculto");
    });

    // Ocultar la lista cuando haces clic fuera del input o de la lista
    document.addEventListener("click", (e) => {
        if (!inputSub.contains(e.target) && !listaSub.contains(e.target)) {
            listaVisible = false;
            listaSub.classList.remove("visible");
            listaSub.classList.add("oculto");
        }
    });

    function actualizarInterfaz() {
        // Actualiza lista desplegable
        listaSub.innerHTML = "";
        subcategorias.forEach((sub) => {
            const p = document.createElement("p");
            p.innerHTML = `${sub} <i class="ri-close-line"></i>`;
            p.querySelector("i").addEventListener("click", () => eliminarSubcategoria(sub));
            listaSub.appendChild(p);
        });

        // Actualiza tags dentro del input
        contenedorTags.innerHTML = "";
        const visibles = subcategorias.slice(0, 2);
        visibles.forEach((sub) => {
            const tag = document.createElement("div");
            tag.className = "sub-tag";
            tag.innerHTML = `${sub} <i class="ri-close-line"></i>`;
            tag.querySelector("i").addEventListener("click", () => eliminarSubcategoria(sub));
            contenedorTags.appendChild(tag);
        });

        if (subcategorias.length > 2) {
            const extra = document.createElement("div");
            extra.className = "sub-tag";
            extra.textContent = `+${subcategorias.length - 2}`;
            contenedorTags.appendChild(extra);
        }

        // Mostrar/ocultar lista
        if (subcategorias.length === 0) {
            listaSub.classList.add("oculto");
        } else if (listaVisible) {
            listaSub.classList.add("visible");
        }

        // Actualizar el input oculto
        document.getElementById("subcategoriasHidden").value = subcategorias.join(",");

        requestAnimationFrame(() => {
            // Medimos el ancho real ocupado por los tags
            let offset = 0;
            if (contenedorTags.children.length > 0) {
                const lastTag = contenedorTags.lastElementChild;
                offset = lastTag.offsetLeft + lastTag.offsetWidth + 5; // 
            } else {
                offset = 15; // margen por defecto si no hay tags
            }

            // Mueve el contenedor de los tags dentro del input
            contenedorTags.style.position = "absolute";
            contenedorTags.style.left = "30px"; // igual que tu label en CSS
            contenedorTags.style.top = "22px";

            // Mueve visualmente el input al mismo nivel, sin cambiar su ancho ni padding
            inputSub.style.textIndent = offset + "px"; // solo mueve el punto de escritura
            inputSub.focus();

            // coloca el cursor al final
            const len = inputSub.value.length;
            inputSub.setSelectionRange(len, len);
        });

        // Mantiene el label arriba si existen subcategorías
        if (subcategorias.length > 0) {
            inputSub.classList.add("has-value");
        } else {
            inputSub.classList.remove("has-value");
        }
    }

    function eliminarSubcategoria(valor) {
        subcategorias = subcategorias.filter((s) => s !== valor);
        actualizarInterfaz();
    }

    function obtenerSubcategorias() {
        return subcategorias;
    }

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



    //Validaciones del formulario de agregar producto
    const fieldsProducto = {
        categoriaprincipal: {
            regex: /^([A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)(\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)*$/,
            errorMessage: "La categoria principal debe tener al menos 3 letras donde la primera letra tiene que ser mayuscula y no contener números."
        },
        Subcategoria: {
            regex: /^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\s]{2,30}$/,
            errorMessage: "Debes agregar al menos una subcategoría antes de continuar"
        },
    };

    // Asignamos validaciones de escritura en tiempo real
    Object.keys(fieldsProducto).forEach(fieldId => {
        const input = document.getElementById(fieldId);
        const inputBox = input.closest(".formulario__box");
        const checkIcon = inputBox.querySelector(".ri-check-line");
        const errorIcon = inputBox.querySelector(".ri-close-line");
        const errorMessage = inputBox.parentNode.querySelector(".formulario__error");

        input.addEventListener("input", () => {
            const value = input.value.trim();
            const label = inputBox.querySelector("label.box__label");

            if (value === "") {
                inputBox.classList.remove("input-error");
                checkIcon.style.display = "none";
                errorIcon.style.display = "none";
                errorMessage.style.display = "none";
                input.style.border = "";
                if (label) label.classList.remove("error");
            } else if (fieldsProducto[fieldId].regex.test(value)) {
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
            radiosCustom.forEach(r => r.classList.remove('error'));
        });
    });

    // Validación de imagen
    function validarImagenes() {
        const file = inputFile.files[0];
        errorFormato.style.display = "none";
        errorVacio.style.display = "none";
        boxImagen.style.border = "1px solid #ddd";

        if (!file) {
            errorVacio.style.display = "block";
            boxImagen.style.border = "2px solid #e53935";
            return false;
        }

        if (!formatosPermitidos.includes(file.type)) {
            errorFormato.style.display = "block";
            boxImagen.style.border = "2px solid #e53935";
            return false;
        }

        boxImagen.style.border = "1px solid #ddd";
        return true;
    }

    // Validación de descripción del producto
    const boxDescripcion = document.querySelector('.formulario__boxdescripcion');
    const errorDescripcion = document.querySelector('.error--descripcion');

    function validarDescripcion() {
        const contenido = quill.getText().trim();
        errorDescripcion.style.display = 'none';
        boxDescripcion.classList.remove('error-border');

        if (contenido === '' || contenido.length === 0) {
            errorDescripcion.style.display = 'block';
            boxDescripcion.classList.add('error-border');
            return false;
        }

        return true;
    }

    quill.on('text-change', () => {
        const contenido = quill.getText().trim();
        if (contenido.length > 0) {
            errorDescripcion.style.display = 'none';
            boxDescripcion.classList.remove('error-border');
        }
    });

    // --- VALIDACIÓN GENERAL DEL FORMULARIO ---
    const addform = document.getElementById("formularioProducto");

    addform.addEventListener("submit", function (e) {
        let formularioValido = true;
        let selectsValidos = true;

        // Validar inputs individuales (texto directo)
        Object.keys(fieldsProducto).forEach(fieldId => {
            const input = document.getElementById(fieldId);
            const regex = fieldsProducto[fieldId].regex;
            const inputBox = input.closest(".formulario__box");
            const checkIcon = inputBox.querySelector(".ri-check-line");
            const errorIcon = inputBox.querySelector(".ri-close-line");
            const errorMessage = inputBox.parentNode.querySelector(".formulario__error");
            const label = inputBox.querySelector("label.box__label");

            if (input.value.trim() === "") {
                formularioValido = false;
                checkIcon.style.display = "none";
                errorIcon.style.display = "inline-block";
                errorMessage.style.display = "block";
                input.style.border = "2px solid #fd1f1f";
                if (label) label.classList.add("error");
                inputBox.classList.add("input-error");
            } else if (!regex.test(input.value.trim())) {
                // Campo con texto pero inválido → mostrar error
                formularioValido = false;
                checkIcon.style.display = "none";
                errorIcon.style.display = "inline-block";
                errorMessage.style.display = "block";
                input.style.border = "2px solid #fd1f1f";
                if (label) label.classList.add("error");
                inputBox.classList.add("input-error");
            } else {
                // Campo válido
                checkIcon.style.display = "inline-block";
                errorIcon.style.display = "none";
                errorMessage.style.display = "none";
                input.style.border = "2px solid #0034de";
                if (label) label.classList.remove("error");
                inputBox.classList.remove("input-error");
            }
        });

        // --- VALIDAR SUBCATEGORÍAS (MODO DIRECTO O TAGS) ---
        const inputSub = document.getElementById("Subcategoria");
        const hiddenSub = document.getElementById("subcategoriasHidden");
        const subBox = inputSub.closest(".formulario__box");
        const subCheck = subBox.querySelector(".ri-check-line");
        const subErrorIcon = subBox.querySelector(".ri-close-line");
        const subErrorMsg = subBox.parentNode.querySelector(".formulario__error");

        const valorDirecto = inputSub.value.trim();
        const valorTags = hiddenSub.value.trim();

        if (valorDirecto === "" && valorTags === "") {
            formularioValido = false;
            subCheck.style.display = "none";
            subErrorIcon.style.display = "inline-block";
            subErrorMsg.style.display = "block";
            inputSub.style.border = "2px solid #fd1f1f";
            subBox.classList.add("input-error");
        } else {
            subErrorMsg.style.display = "none";
            subBox.classList.remove("input-error");
            subErrorIcon.style.display = "none";
            subCheck.style.display = "inline-block";
            inputSub.style.border = "2px solid #0034de";
        }


        // Validar estado (radio buttons)
        const estadoSeleccionado = Array.from(radiosEstado).some(radio => radio.checked);

        if (!estadoSeleccionado) {
            formularioValido = false;
            errorEstado.style.display = 'block';
            radiosCustom.forEach(r => r.classList.add('error'));
        } else {
            radiosCustom.forEach(r => r.classList.remove('error'));
        }

        // Validar imagen
        if (!validarImagenes()) {
            formularioValido = false;
        }

        // Validar descripción
        if (!validarDescripcion()) {
            formularioValido = false;
        }

        // Mostrar error general
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

    // Al cargar la página, mostrar éxito si existe
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