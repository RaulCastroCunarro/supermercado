-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.10-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para rupermercado
CREATE DATABASE IF NOT EXISTS `rupermercado` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `rupermercado`;

-- Volcando estructura para tabla rupermercado.categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla rupermercado.categoria: ~2 rows (aproximadamente)
DELETE FROM `categoria`;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` (`id`, `nombre`) VALUES
	(1, 'Droguería'),
	(2, 'Lácteos');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;

-- Volcando estructura para tabla rupermercado.producto
CREATE TABLE IF NOT EXISTS `producto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `precio` float unsigned NOT NULL DEFAULT 0,
  `imagen` tinytext NOT NULL DEFAULT 'https://image.flaticon.com/icons/png/512/372/372627.png',
  `descripcion` tinytext NOT NULL,
  `descuento` int(11) unsigned NOT NULL DEFAULT 0,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_modificacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `fecha_eliminacion` timestamp NULL DEFAULT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_categoria` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `FK-producto_has_usuario` (`id_usuario`),
  KEY `FK-categoria` (`id_categoria`),
  CONSTRAINT `FK-categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id`),
  CONSTRAINT `FK-producto_has_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla rupermercado.producto: ~7 rows (aproximadamente)
DELETE FROM `producto`;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` (`id`, `nombre`, `precio`, `imagen`, `descripcion`, `descuento`, `fecha_creacion`, `fecha_modificacion`, `fecha_eliminacion`, `id_usuario`, `id_categoria`) VALUES
	(1, 'Cafe', 20, 'https://as01.epimg.net/deporteyvida/imagenes/2018/06/19/portada/1529402043_039778_1529402207_noticia_normal.jpg', 'Es un cafe con elche', 5, '2019-12-26 13:09:39', '2020-01-08 09:29:31', NULL, 1, 1),
	(2, 'Leche', 80, 'https://upload.wikimedia.org/wikipedia/commons/0/0e/Milk_glass.jpg', 'Una montaña recien recolectada de nuestros ganaderos locales.', 10, '2019-12-26 13:09:39', '2020-01-08 09:29:34', NULL, 1, 2),
	(3, 'Pan', 9, 'http://www.hacerpan.net/ImagenesHacerPan/ImagenesHacerPan/pan_trigo.jpg', 'Hola', 15, '2019-12-26 13:09:39', '2020-01-08 09:29:36', NULL, 1, 1),
	(4, 'Patatas', 3.4, 'https://multisac.es/wp-content/uploads/2018/01/Saco-de-Rafia-trasparente-Marcado-Patata-de-Consumo.png', 'Patatas alavesas', 25, '2019-12-26 13:09:39', '2020-01-08 09:29:39', NULL, 2, 1),
	(5, 'Posn', 50, 'https://cdn.shopify.com/s/files/1/0229/0839/files/Untitled_design__1.png', 'Un posn muy rico ñam ñam', 15, '2019-12-26 13:09:39', '2020-01-08 09:29:41', '2020-01-03 11:30:49', 1, 2),
	(6, 'Apocalipsis', 40, 'https://image.flaticon.com/icons/png/512/372/372627.png', 'prueba', 20, '2020-01-03 12:01:23', '2020-01-08 09:29:43', NULL, 2, 2),
	(10, 'Tortillas', 8, 'http://img.desmotivaciones.es/201108/descarga2_12.jpg', 'xcghjkl', 0, '2020-01-03 12:22:18', '2020-01-08 09:30:12', NULL, 1, 1);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;

-- Volcando estructura para tabla rupermercado.rol
CREATE TABLE IF NOT EXISTS `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '1:Usuario normal 2:Administrador',
  `nombre` varchar(15) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla rupermercado.rol: ~2 rows (aproximadamente)
DELETE FROM `rol`;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES
	(2, 'administrador'),
	(1, 'usuario');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;

-- Volcando estructura para tabla rupermercado.usuario
CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL DEFAULT 'Default',
  `contrasenia` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `imagen` varchar(250) NOT NULL DEFAULT 'http://emser.es/wp-content/uploads/2016/08/usuario-sin-foto.png',
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_modificacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `fecha_eliminacion` timestamp NULL DEFAULT NULL,
  `id_rol` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `FK_rol` (`id_rol`),
  CONSTRAINT `FK_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla rupermercado.usuario: ~3 rows (aproximadamente)
DELETE FROM `usuario`;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`id`, `nombre`, `contrasenia`, `email`, `imagen`, `fecha_creacion`, `fecha_modificacion`, `fecha_eliminacion`, `id_rol`) VALUES
	(1, 'admin', '123456', 'admin@ipartek.es', 'http://emser.es/wp-content/uploads/2016/08/usuario-sin-foto.png', '2019-12-26 13:09:12', '2020-01-02 11:16:37', NULL, 2),
	(2, 'pepe', 'pepe', 'pepe@ipartek.es', 'http://emser.es/wp-content/uploads/2016/08/usuario-sin-foto.png', '2019-12-26 13:09:12', '2020-01-03 12:59:36', NULL, 1),
	(5, 'pepa', 'pepa', 'pepa@ipartek.es', 'http://emser.es/wp-content/uploads/2016/08/usuario-sin-foto.png', '2020-01-03 13:04:56', '2020-01-03 13:07:13', NULL, 1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;

-- Volcando estructura para procedimiento rupermercado.pa_categoria_getall
DELIMITER //
CREATE PROCEDURE `pa_categoria_getall`()
BEGIN

SELECT id, nombre FROM categoria ORDER BY nombre ASC LIMIT 500;

END//
DELIMITER ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
