USE railway;

-- TABLA CATEGORIA
CREATE TABLE IF NOT EXISTS `categoria` (
                                           `activo` bit(1) NOT NULL,
    `categoria_id` bigint NOT NULL AUTO_INCREMENT,
    `nombre` varchar(175) NOT NULL,
    `descripcion` varchar(500) NOT NULL,
    `imagen` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`categoria_id`),
    UNIQUE KEY `uk_categoria_nombre` (`nombre`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA SUBCATEGORIA
CREATE TABLE IF NOT EXISTS `sub_categorias` (
                                                `categoria_id` bigint DEFAULT NULL,
                                                `subcategoria_id` bigint NOT NULL AUTO_INCREMENT,
                                                `nombre` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`subcategoria_id`),
    KEY `fk_sub-Categoria_categoria` (`categoria_id`),
    CONSTRAINT `fk_sub-Categoria_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`categoria_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA PRODUCTO
CREATE TABLE IF NOT EXISTS `producto` (
                                          `activo` bit(1) NOT NULL,
    `precio` double NOT NULL,
    `precio_regular` double DEFAULT 0,
    `stock` int NOT NULL,
    `categoria_id` bigint DEFAULT NULL,
    `producto_id` bigint NOT NULL AUTO_INCREMENT,
    `subcategoria_id` bigint DEFAULT NULL,
    `descripcion` varchar(700) DEFAULT NULL,
    `codigo_producto` varchar(255) DEFAULT NULL,
    `imagen` varchar(255) DEFAULT NULL,
    `marca` varchar(255) DEFAULT NULL,
    `nombre` varchar(255) DEFAULT NULL,
    `pais` varchar(255) DEFAULT NULL,
    `tipo` enum('CAJA','COMBO','PACK','UNIDAD') DEFAULT NULL,
    PRIMARY KEY (`producto_id`),
    UNIQUE KEY `uk_producto_codigo` (`codigo_producto`),
    KEY `fk_producto_categoria` (`categoria_id`),
    KEY `fk_producto_subcategoria` (`subcategoria_id`),
    CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`categoria_id`),
    CONSTRAINT `fk_producto_subcategoria` FOREIGN KEY (`subcategoria_id`) REFERENCES `sub_categorias` (`subcategoria_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA ROL
CREATE TABLE IF NOT EXISTS `rol` (
                                     `rol_id` bigint NOT NULL AUTO_INCREMENT,
                                     `rol` enum('ADMIN','INVITADO','PERSONA_CONTACTO','PERSONA_JURIDICA','PERSONA_NATURAL') DEFAULT NULL,
    PRIMARY KEY (`rol_id`),
    UNIQUE KEY `UKgidd9huji2j14xop37v9dc7li` (`rol`)
    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA EMPRESA
CREATE TABLE IF NOT EXISTS `empresa` (
                                         `activo` bit(1) NOT NULL,
    `empresa_id` bigint NOT NULL AUTO_INCREMENT,
    `nit` varchar(12) NOT NULL,
    `telefono` varchar(20) NOT NULL,
    `correo` varchar(100) NOT NULL,
    `razon_social` varchar(175) NOT NULL,
    `ciudad` varchar(255) NOT NULL,
    `direccion` varchar(255) NOT NULL,
    `imagen` varchar(255) DEFAULT NULL,
    `e_sector` enum('BAR','CAFE','CATERING','HOTEL','NIGHTCLUB','RESTAURANT') NOT NULL,
    PRIMARY KEY (`empresa_id`),
    UNIQUE KEY `uk_empresa_nit` (`nit`),
    UNIQUE KEY `uk_empresa_razon_social` (`razon_social`),
    UNIQUE KEY `uk_empresa_telefono` (`telefono`),
    UNIQUE KEY `uk_empresa_correo` (`correo`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA USUARIO
CREATE TABLE IF NOT EXISTS `usuario` (
                                         `account_non_expired` bit(1) DEFAULT NULL,
    `account_non_locked` bit(1) DEFAULT NULL,
    `credentials_non_expired` bit(1) DEFAULT NULL,
    `is_enabled` bit(1) DEFAULT NULL,
    `empresa_id` bigint DEFAULT NULL,
    `rol_id` bigint DEFAULT NULL,
    `usuario_id` bigint NOT NULL AUTO_INCREMENT,
    `numero_identificacion` varchar(15) DEFAULT NULL,
    `telefono` varchar(20) DEFAULT NULL,
    `correo` varchar(100) NOT NULL,
    `direccion` varchar(100) DEFAULT NULL,
    `password` varchar(100) DEFAULT NULL,
    `apellidos` varchar(175) DEFAULT NULL,
    `nombres` varchar(175) DEFAULT NULL,
    `imagen` varchar(255) DEFAULT NULL,
    `tipo_identificacion` enum('CC','CE','DE','NIT','PP','TE','TI') DEFAULT NULL,
    PRIMARY KEY (`usuario_id`),
    UNIQUE KEY `uk_usuario_correo` (`correo`),
    UNIQUE KEY `uk_usuario_telefono` (`telefono`),
    UNIQUE KEY `UKpxkkfbhkbchlb7g4ybf8e4aqj` (`empresa_id`),
    KEY `fk_usuario_rol` (`rol_id`),
    CONSTRAINT `fk_usuario_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`empresa_id`),
    CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`rol_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA COMPRA
CREATE TABLE IF NOT EXISTS `compra` (
                                        `iva` double NOT NULL,
                                        `subtotal` double NOT NULL,
                                        `total` double NOT NULL,
                                        `compra_id` bigint NOT NULL AUTO_INCREMENT,
                                        `fecha` datetime(6) DEFAULT NULL,
    `usuario_id` bigint DEFAULT NULL,
    `cupon_descuento` varchar(255) DEFAULT NULL,
    `estado` enum('CANCELADO','PAGADO','PENDIENTE','RECHAZADO') DEFAULT NULL,
    `metodo_pago` enum('DAVIPLATA','MERCADO_PAGO','NEQUI','PAYPAL','PSE','TARJETA_CREDITO','TARJETA_DEBITO','TRANSFERENCIA') DEFAULT NULL,
    PRIMARY KEY (`compra_id`),
    KEY `fk_compra_usuario` (`usuario_id`),
    CONSTRAINT `fk_compra_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA DETALLE_VENTA
CREATE TABLE IF NOT EXISTS `detalle_venta` (
                                               `cantidad` int NOT NULL,
                                               `precio_unitario` int DEFAULT NULL,
                                               `subtotal` int NOT NULL,
                                               `compra_id` bigint DEFAULT NULL,
                                               `detalle_venta_id` bigint NOT NULL AUTO_INCREMENT,
                                               `producto_id` bigint DEFAULT NULL,
                                               PRIMARY KEY (`detalle_venta_id`),
    KEY `fk_detalleVenta_compra` (`compra_id`),
    KEY `fk_detalleVenta_producto` (`producto_id`),
    CONSTRAINT `fk_detalleVenta_compra` FOREIGN KEY (`compra_id`) REFERENCES `compra` (`compra_id`),
    CONSTRAINT `fk_detalleVenta_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`producto_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA HISTORIA
CREATE TABLE IF NOT EXISTS `historia` (
                                          `fecha` date NOT NULL,
                                          `is_enabled` bit(1) DEFAULT NULL,
    `historia_id` bigint NOT NULL AUTO_INCREMENT,
    `titulo` varchar(100) NOT NULL,
    `descripcion` varchar(1000) NOT NULL,
    `historia_completa` varchar(10000) NOT NULL,
    `imagen` varchar(255) NOT NULL,
    PRIMARY KEY (`historia_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- TABLA COMENTARIO
CREATE TABLE IF NOT EXISTS `comentario` (
    `activo` bit(1) NOT NULL,
    `calificacion` int NOT NULL,
    `fecha` date NOT NULL,
    `comentario_id` bigint NOT NULL AUTO_INCREMENT,
    `historia_id` bigint DEFAULT NULL,
    `usuario_id` bigint DEFAULT NULL,
    `titulo` varchar(100) NOT NULL,
    `mensaje` varchar(1000) NOT NULL,
    PRIMARY KEY (`comentario_id`),
    KEY `fk_comentario_historia` (`historia_id`),
    KEY `fk_comentario_usuario` (`usuario_id`),
    CONSTRAINT `fk_comentario_historia` FOREIGN KEY (`historia_id`) REFERENCES `historia` (`historia_id`),
    CONSTRAINT `fk_comentario_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`usuario_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
