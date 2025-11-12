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

    // ========== FUNCIONALIDAD DE SUBCATEGORÍAS ==========
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

            // Validar formato de subcategoría
            if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9\s]{2,30}$/.test(valor)) {
                mostrarErrorSubcategoria("La subcategoría debe tener entre 2 y 30 caracteres alfanuméricos");
                return;
            }

            if (subcategorias.includes(valor.toLowerCase())) {
                mostrarErrorSubcategoria("Esta subcategoría ya fue agregada");
                return;
            }

            subcategorias.push(valor);
            inputSub.value = "";
            actualizarInterfaz();
            limpiarErrorSubcategoria();
        }
    });

    function mostrarErrorSubcategoria(mensaje) {
        const errorMsg = inputSub.closest(".input__formulario").querySelector(".formulario__error");
        errorMsg.textContent = mensaje;
        errorMsg.style.display = "block";
        inputSub.style.border = "2px solid #fd1f1f";
    }

    function limpiarErrorSubcategoria() {
        const errorMsg = inputSub.closest(".input__formulario").querySelector(".formulario__error");
        errorMsg.style.display = "none";
        inputSub.style.border = "";
    }

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
                offset = lastTag.offsetLeft + lastTag.offsetWidth + 5;
            } else {
                offset = 15; // margen por defecto si no hay tags
            }

            // Mueve el contenedor de los tags dentro del input
            contenedorTags.style.position = "absolute";
            contenedorTags.style.left = "30px";
            contenedorTags.style.top = "22px";

            // Mueve visualmente el input al mismo nivel, sin cambiar su ancho ni padding
            inputSub.style.textIndent = offset + "px";
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

    // ========== EDITOR DE DESCRIPCIÓN ==========
    const toolbarOptions = [
        ['bold', 'italic', 'underline'],
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],
        ['link', 'clean']
    ];

    const quill = new Quill('#editor-container', {
        modules: {
            toolbar: toolbarOptions
        },
        theme: 'snow',
        placeholder: 'Describe la categoría...'
    });

    // Actualizar campo oculto cuando cambie el contenido
    quill.on('text-change', function() {
        const contenidoTextoPlano = quill.getText().trim();
        document.getElementById('descripcionHidden').value = contenidoTextoPlano;

        // Validación visual en tiempo real
        const errorDescripcion = document.querySelector('.error--descripcion');
        const boxDescripcion = document.querySelector('.formulario__boxdescripcion');

        if (contenidoTextoPlano.length > 0) {
            errorDescripcion.style.display = 'none';
            boxDescripcion.classList.remove('error-border');
        }
    });

    // ========== MANEJO DE IMAGEN ==========
    const boxImagen = document.querySelector('.formulario__boximagen');
    const inputFile = document.getElementById('fileInput');
    const previewContainer = document.getElementById('previewContainer');
    const errorFormato = document.querySelector('.error--formato');
    const errorVacio = document.querySelector('.error--vacio');
    const formatosPermitidos = ["image/jpeg", "image/png", "image/webp"];

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
                e.stopPropagation();
                previewContainer.style.display = 'none';
                previewContainer.innerHTML = '';
                boxImagen.classList.remove('imagen-activa');
                removeBtn.remove();
                inputFile.value = '';
            });
        }, 2000);
    });

    // ========== VALIDACIONES ==========
    const fieldsProducto = {
        categoriaprincipal: {
            regex: /^([A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)(\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)*$/,
            errorMessage: "La categoria principal debe tener al menos 3 letras donde la primera letra tiene que ser mayuscula y no contener números."
        }
    };

    // Validación en tiempo real para categoría principal
    const inputCategoria = document.getElementById("categoriaprincipal");
    const categoriaBox = inputCategoria.closest(".formulario__box");
    const checkIcon = categoriaBox.querySelector(".ri-check-line");
    const errorIcon = categoriaBox.querySelector(".ri-close-line");
    const errorMessage = categoriaBox.parentNode.querySelector(".formulario__error");

    inputCategoria.addEventListener("input", () => {
        validarCategoriaPrincipal();
    });

    function validarCategoriaPrincipal() {
        const value = inputCategoria.value.trim();
        const label = categoriaBox.querySelector("label.box__label");

        if (value === "") {
            categoriaBox.classList.remove("input-error", "input-success");
            checkIcon.style.display = "none";
            errorIcon.style.display = "none";
            errorMessage.style.display = "none";
            inputCategoria.style.border = "";
            if (label) label.classList.remove("error", "success");
        } else if (fieldsProducto.categoriaprincipal.regex.test(value)) {
            checkIcon.style.display = "inline-block";
            errorIcon.style.display = "none";
            errorMessage.style.display = "none";
            inputCategoria.style.border = "2px solid #0034de";
            categoriaBox.classList.remove("input-error");
            categoriaBox.classList.add("input-success");
            if (label) label.classList.remove("error");
            if (label) label.classList.add("success");
        } else {
            checkIcon.style.display = "none";
            errorIcon.style.display = "inline-block";
            errorMessage.style.display = "block";
            inputCategoria.style.border = "2px solid #fd1f1f";
            categoriaBox.classList.add("input-error");
            categoriaBox.classList.remove("input-success");
            if (label) label.classList.add("error");
            if (label) label.classList.remove("success");
        }
    }

    // ========== VALIDACIÓN DE IMAGEN ==========
    function validarImagen() {
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

        return true;
    }

    // ========== VALIDACIÓN DE DESCRIPCIÓN ==========
    function validarDescripcion() {
        const contenido = quill.getText().trim();
        const errorDescripcion = document.querySelector('.error--descripcion');
        const boxDescripcion = document.querySelector('.formulario__boxdescripcion');

        errorDescripcion.style.display = 'none';
        boxDescripcion.classList.remove('error-border');

        if (contenido === '' || contenido.length === 0) {
            errorDescripcion.style.display = 'block';
            boxDescripcion.classList.add('error-border');
            return false;
        }

        return true;
    }

    // ========== VALIDACIÓN DE ESTADO ==========
    const radiosEstado = document.querySelectorAll('input[name="activo"]');
    const errorEstado = document.querySelector('.error--estado');
    const radiosCustom = document.querySelectorAll('.radio__custom');

    // Quitar el error cuando el usuario selecciona un estado
    radiosEstado.forEach(radio => {
        radio.addEventListener('change', () => {
            errorEstado.style.display = 'none';
            radiosCustom.forEach(r => r.classList.remove('error'));
        });
    });

    function validarEstado() {
        const estadoSeleccionado = document.querySelector('input[name="activo"]:checked');

        if (!estadoSeleccionado) {
            errorEstado.style.display = 'block';
            radiosCustom.forEach(r => r.classList.add('error'));
            return false;
        }

        errorEstado.style.display = 'none';
        radiosCustom.forEach(r => r.classList.remove('error'));
        return true;
    }

    // ========== VALIDACIÓN FINAL DEL FORMULARIO ==========
    const addform = document.getElementById("formularioProducto");

    addform.addEventListener("submit", function (e) {
        e.preventDefault();
        let formularioValido = true;

        // Validar categoría principal
        const valorCategoria = inputCategoria.value.trim();
        if (valorCategoria === "" || !fieldsProducto.categoriaprincipal.regex.test(valorCategoria)) {
            formularioValido = false;
            checkIcon.style.display = "none";
            errorIcon.style.display = "inline-block";
            errorMessage.style.display = "block";
            inputCategoria.style.border = "2px solid #fd1f1f";
            inputCategoria.classList.add("input-error");
            const label = categoriaBox.querySelector("label.box__label");
            if (label) label.classList.add("error");
        }else {
            checkIcon.style.display = "inline-block";
            errorIcon.style.display = "none";
            errorMessage.style.display = "none";
            inputCategoria.style.border = "2px solid #0034de";
            inputCategoria.classList.remove("input-error");
            const label = categoriaBox.querySelector("label.box__label");
            if (label) label.classList.remove("error");
        }

        // Validar subcategorías
        if (subcategorias.length === 0) {
            formularioValido = false;
            mostrarErrorSubcategoria("Debes agregar al menos una subcategoría antes de continuar");
        } else {
            limpiarErrorSubcategoria();
        }

        // Validar imagen
        if (!validarImagen()) {
            formularioValido = false;
        }

        // Validar descripción
        if (!validarDescripcion()) {
            formularioValido = false;
        }

        // Validar estado
        if (!validarEstado()) {
            formularioValido = false;
        }

        if (!formularioValido) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Por favor complete todos los campos correctamente",
                customClass: {
                    title: 'swal-title',
                    popup: 'swal-popup'
                }
            });
            return;
        }

        // Si todo está válido, enviar el formulario
        addform.submit();
    });

});