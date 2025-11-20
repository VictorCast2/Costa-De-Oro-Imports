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


    //Select de pais
    const paises = [
        // América del Norte
        "Estados Unidos", "México",

        // América Central y el Caribe
        "Guatemala", "Puerto Rico", "República Dominicana",

        // América del Sur
        "Argentina", "Brasil", "Chile", "Colombia",

        // Europa
        "Alemania", "Bélgica", "España",
        "Francia", "Irlanda", "Países Bajos", "Reino Unido", "Suiza",
        "Noruega", "Suecia", "Italia", "Rusia", "Ucrania", "Escocia"
    ];

    // Obtener el elemento select
    const selectPaises = document.getElementById("páises");

    // Agregar los países al select
    paises.forEach(pais => {
        const option = document.createElement("option");
        option.value = pais.toLowerCase().replace(/\s+/g, "_");
        option.textContent = pais;
        selectPaises.appendChild(option);
    });



    //Select de categoria principal y secundaria
    const categoriaPrincipal = document.getElementById("categoriaPrincipal");
    const categoriaSecundaria = document.getElementById("categoriaSecundaria");

    // Diccionario de subcategorías
    const subcategorias = {
        Vino: ["Vino Tinto", "Vino Blanco", "Vino Rosado", "Espumoso"],
        Cerveza: ["Rubia", "Negra", "Roja", "Artesanal"],
        Whisky: ["Blended Scotch", "Single Malt", "Blended Malt"],
        Tequila: ["Blanco", "Reposado", "Añejo"],
        Vodka: ["Saborizado", "Premium", "Artesanal", "Tradicional"],
        Ginebra: ["Seca", "Dulce", "Aromática"],
        Aguardiente: ["Antioqueño", "Cristal", "Sin azúcar"],
        Ron: ["Blanco", "Oscuro", "Añejo", "Saborizado"],
        Mezcal: ["Joven", "Reposado", "Añejo"]
    };

    // Cuando cambia la categoría principal
    categoriaPrincipal.addEventListener("change", function () {
        const seleccion = this.value;

        // Limpiar las opciones anteriores
        categoriaSecundaria.innerHTML = '<option disabled selected>Categoría secundaria</option>';

        // Agregar las nuevas según la categoría seleccionada
        if (subcategorias[seleccion]) {
            subcategorias[seleccion].forEach((sub) => {
                const option = document.createElement("option");
                option.value = sub;
                option.textContent = sub;
                categoriaSecundaria.appendChild(option);
            });
        }

        // Reiniciar el valor seleccionado (por si antes había una opción previa)
        categoriaSecundaria.selectedIndex = 0;
    });


    // === NUEVA SECCIÓN: MARCAS DEPENDIENTES ===
    // Obtener los elementos del DOM
    const selectPais = document.getElementById("páises");
    const selectMarca = document.getElementById("marca");

    // Diccionario de marcas según categoría principal, país y secundaria
    const marcas = {
        Vino: {
            Italia: {
                "Vino Tinto": ["Antinori", "Tignanello", "Frescobaldi", "Gaja"],
                "Vino Blanco": ["Santa Margherita", "Masi", "Ruffino"],
                "Vino Rosado": ["Bolla", "Castello Banfi"],
                "Espumoso": ["Prosecco", "Franciacorta"]
            },
            España: {
                "Vino Tinto": ["Marqués de Riscal", "Torres", "Vega Sicilia", "Protos"],
                "Vino Blanco": ["Faustino", "Campo Viejo"],
                "Vino Rosado": ["Codorníu", "Freixenet"],
                "Espumoso": ["Juvé & Camps", "Codorníu"]
            },
            Francia: {
                "Vino Tinto": ["Château Margaux", "Château Lafite Rothschild", "Louis Jadot"],
                "Vino Blanco": ["Joseph Drouhin", "Château d’Yquem"],
                "Vino Rosado": ["Moët & Chandon", "Dom Pérignon"],
                "Espumoso": ["Veuve Clicquot", "Perrier-Jouët"]
            },
            Chile: {
                "Vino Tinto": ["Concha y Toro", "Casillero del Diablo", "Montes", "Carmen"],
                "Vino Blanco": ["Santa Rita", "Undurraga", "Errazuriz"],
                "Vino Rosado": ["Tarapacá", "Cousiño Macul"],
                "Espumoso": ["Valdivieso", "Undurraga Brut"]
            },
            Argentina: {
                "Vino Tinto": ["Catena Zapata", "Luigi Bosca", "Rutini", "Trapiche"],
                "Vino Blanco": ["Norton", "Bianchi"],
                "Vino Rosado": ["El Enemigo", "Navarro Correas"],
                "Espumoso": ["Chandon", "Baron B"]
            }
        },

        Cerveza: {
            México: {
                Rubia: ["Corona", "Modelo", "Pacifico"],
                Negra: ["Negra Modelo"],
                Roja: ["Victoria"],
                Artesanal: ["Minerva", "Calavera"]
            },
            Alemania: {
                Rubia: ["Paulaner", "Beck’s"],
                Negra: ["Köstritzer"],
                Roja: ["Spaten", "Erdinger"],
                Artesanal: ["Weihenstephaner", "Bitburger"]
            },
            Bélgica: {
                Rubia: ["Leffe", "Stella Artois"],
                Negra: ["Chimay", "Rochefort"],
                Roja: ["Delirium Red"],
                Artesanal: ["Duvel", "La Chouffe"]
            }
        },

        Whisky: {
            Escocia: {
                "Blended Scotch": ["Johnnie Walker", "Chivas Regal", "Ballantine’s"],
                "Single Malt": ["Glenfiddich", "Macallan", "Lagavulin"],
                "Blended Malt": ["Monkey Shoulder", "Compass Box"]
            },
            Irlanda: {
                "Blended Scotch": ["Jameson"],
                "Single Malt": ["Bushmills"],
                "Blended Malt": ["Teeling"]
            },
            Estados_Unidos: {
                "Blended Scotch": ["Jack Daniel’s"],
                "Single Malt": ["Woodford Reserve"],
                "Blended Malt": ["Bulleit"]
            }
        },

        Tequila: {
            México: {
                Blanco: ["Jose Cuervo", "Don Julio Blanco", "1800 Silver"],
                Reposado: ["Herradura", "Cazadores", "Patrón Reposado"],
                Añejo: ["Don Julio Añejo", "Gran Centenario"]
            }
        },

        Vodka: {
            Rusia: {
                Tradicional: ["Russian Standard", "Beluga", "Moskovskaya"],
                Premium: ["Belvedere", "Stolichnaya Elit"],
                Saborizado: ["Nemiroff", "Zyr"],
                Artesanal: ["Hammer + Sickle"]
            },
            Polonia: {
                Tradicional: ["Wyborowa"],
                Premium: ["Chopin", "Belvedere"],
                Saborizado: ["Żubrówka"]
            }
        }
    };

    // Función para actualizar las marcas
    function actualizarMarcas() {
        const categoria = categoriaPrincipal.value;
        const paisSeleccionado = selectPais.options[selectPais.selectedIndex]?.text || "";
        const categoriaSec = categoriaSecundaria.value;

        // Limpiar el select de marcas
        selectMarca.innerHTML = '<option disabled selected>Marca</option>';

        // Verificar que haya selección en los tres
        if (categoria && paisSeleccionado && categoriaSec) {
            const lista = marcas[categoria]?.[paisSeleccionado]?.[categoriaSec];

            if (lista) {
                lista.forEach(marca => {
                    const option = document.createElement("option");
                    option.value = marca.toLowerCase().replace(/\s+/g, "_");
                    option.textContent = marca;
                    selectMarca.appendChild(option);
                });
            } else {
                const option = document.createElement("option");
                option.disabled = true;
                option.textContent = "No hay marcas disponibles";
                selectMarca.appendChild(option);
            }
        }
    }

    // Escuchar cambios en los tres selects
    categoriaPrincipal.addEventListener("change", actualizarMarcas);
    selectPais.addEventListener("change", actualizarMarcas);
    categoriaSecundaria.addEventListener("change", actualizarMarcas);

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

    function setupMoneyInput(id) {
        const input = document.getElementById(id);

        input.addEventListener('input', e => {
            const value = e.target.value.replace(/\D/g, '');
            if (value) {
                e.target.value = formatCOP(value);
            } else {
                e.target.value = '';
            }
        });

        input.addEventListener('keydown', e => {
            // Permitir subir o bajar de mil en mil con flechas ↑ ↓
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
    setupMoneyInput('precioRegular');
    setupMoneyInput('precioVenta');

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
        nombre: { regex: /^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]{3,}$/, errorMessage: "El nombre debe tener al menos 3 letras." },
        codigoProducto: { regex: /^#[A-Za-z0-9]+$/, errorMessage: "El código del producto debe iniciar con el símbolo # y contener solo letras o números después." },
        stock: { regex: /^[1-9][0-9]*$/, errorMessage: "Por favor, ingresa una cantidad de stock válida mayor que cero." },
        precioRegular: { regex: /^(?!0+(?:[.,]0+)?$)\d+(?:[.,]\d{1,2})?$/, errorMessage: "Por favor, ingresa un precio válido mayor que cero." },
        precioVenta: { regex: /^(?!0+(?:[.,]0+)?$)\d+(?:[.,]\d{1,2})?$/, errorMessage: "Por favor, ingresa un precio válido mayor que cero." }
    };

    // obtenemos los select
    const selectCategoriaPrincipal = document.getElementById("categoriaPrincipal");
    const errorCategoriaPrincipal = document.querySelector(".error--CategoriaPrincipal");
    const selectpáises = document.getElementById("páises");
    const errorpáises = document.querySelector(".error--páises");
    const selectCategoriaSecundaria = document.getElementById("categoriaSecundaria");
    const errorCategoriaSecundaria = document.querySelector(".error--CategoriaSecundaria");
    const selectmarca = document.getElementById("marca");
    const errormarca = document.querySelector(".error--marca");
    const selecttipoProducto = document.getElementById("tipoProducto");
    const errortipoProducto = document.querySelector(".error--tipoProducto");

    Object.keys(fieldsProducto).forEach(fieldId => {
        const input = document.getElementById(fieldId);
        const inputBox = input.closest(".formulario__box");
        const checkIcon = inputBox.querySelector(".ri-check-line");
        const errorIcon = inputBox.querySelector(".ri-close-line");
        const errorMessage = inputBox.nextElementSibling;

        input.addEventListener("input", () => {
            let value = input.value.trim();

            if (fieldId === "precioRegular" || fieldId === "precioVenta") {
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
    [selectCategoriaPrincipal, selectpáises, selectCategoriaSecundaria, selectmarca, selecttipoProducto].forEach(select => {
        select.addEventListener("change", () => {

            if (select.selectedIndex > 0) {
                select.style.border = "2px solid #0034de";
            } else {
                select.style.border = "";
            }

            if (select === selectCategoriaPrincipal && select.selectedIndex > 0) {
                errorCategoriaPrincipal.style.display = "none";
            }

            if (select === selectpáises && select.selectedIndex > 0) {
                errorpáises.style.display = "none";
            }

            if (select === selectCategoriaSecundaria && select.selectedIndex > 0) {
                errorCategoriaSecundaria.style.display = "none";
            }

            if (select === selectmarca && select.selectedIndex > 0) {
                errormarca.style.display = "none";
            }

            if (select === selecttipoProducto && select.selectedIndex > 0) {
                errortipoProducto.style.display = "none";
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

            if (fieldId === "precioRegular" || fieldId === "precioVenta") {
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

        const CategoriaPrincipalSeleccionada = selectCategoriaPrincipal.selectedIndex > 0;
        const paisSeleccionada = selectpáises.selectedIndex > 0;
        const categoriaSecundariaSeleccionada = selectCategoriaSecundaria.selectedIndex > 0;
        const marcaSeleccionada = selectmarca.selectedIndex > 0;
        const tipoProductoSeleccionada = selecttipoProducto.selectedIndex > 0;


        if (!CategoriaPrincipalSeleccionada) {
            selectsValidos = false;
            errorCategoriaPrincipal.style.display = "block";
            selectCategoriaPrincipal.style.border = "2px solid #fd1f1f";
        }

        if (!paisSeleccionada) {
            selectsValidos = false;
            errorpáises.style.display = "block";
            selectpáises.style.border = "2px solid #fd1f1f";
        }

        if (!categoriaSecundariaSeleccionada) {
            selectsValidos = false;
            errorCategoriaSecundaria.style.display = "block";
            selectCategoriaSecundaria.style.border = "2px solid #fd1f1f";
        }

        if (!marcaSeleccionada) {
            selectsValidos = false;
            errormarca.style.display = "block";
            selectmarca.style.border = "2px solid #fd1f1f";
        }

        if (!tipoProductoSeleccionada) {
            selectsValidos = false;
            errortipoProducto.style.display = "block";
            selecttipoProducto.style.border = "2px solid #fd1f1f";
        }

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