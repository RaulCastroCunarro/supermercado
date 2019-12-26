-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.1.38-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


– Volcando estructura de base de datos para rupermercado
DROP DATABASE IF EXISTS rupermercado;
CREATE DATABASE IF NOT EXISTS rupermercado /*!40100 DEFAULT CHARACTER SET utf8 */;
USE rupermercado;

– Volcando estructura para tabla rupermercado.producto
DROP TABLE IF EXISTS producto;
CREATE TABLE IF NOT EXISTS producto (
id int(11) NOT NULL AUTO_INCREMENT,
nombre varchar(50) NOT NULL,
precio float NOT NULL,
imagen tinytext NOT NULL,
descripcion tinytext NOT NULL,
descuento int(11) NOT NULL,
`fecha_creacion` timestamp,
`fecha_modificacion` timestamp,
`fecha_eliminacion` timestamp,
PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

– Volcando datos para la tabla rupermercado.producto: ~5 rows (aproximadamente)
DELETE FROM producto;
/*!40000 ALTER TABLE producto DISABLE KEYS */;
INSERT INTO producto (id, nombre, precio, imagen, descripcion, descuento) VALUES
(1, 'Cafe', 20, 'https://as01.epimg.net/deporteyvida/imagenes/2018/06/19/portada/1529402043_039778_1529402207_noticia_normal.jpg', 'Es un cafe con elche', 3),
(2, 'Leche', 80, 'https://upload.wikimedia.org/wikipedia/commons/0/0e/Milk_glass.jpg', 'Una montaña recien recolectada de nuestros ganaderos locales.', 9),
(3, 'Pan', 9, 'http://www.hacerpan.net/ImagenesHacerPan/ImagenesHacerPan/pan_trigo.jpg', 'Hola', 3),
(4, 'Patatas', 3.4, 'https://multisac.es/wp-content/uploads/2018/01/Saco-de-Rafia-trasparente-Marcado-Patata-de-Consumo.png', 'Patatas alavesas', 2),
(5, 'Posn', 0, 'https://cdn.shopify.com/s/files/1/0229/0839/files/Untitled_design__1.png?2393&format=jpg&quality=90&width=1024', 'Un posn muy rico ñam ñam', 9);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;

-- Volcando estructura para tabla supermercado.usuario
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL DEFAULT 'Default',
  `contrasenia` varchar(50) NOT NULL,
  `email` varchar(50),
  `imagen` varchar(250) NOT NULL DEFAULT 'http://emser.es/wp-content/uploads/2016/08/usuario-sin-foto.png',
  `fecha_creacion` timestamp,
  `fecha_modificacion` timestamp,
  `fecha_eliminacion` timestamp,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla supermercado.usuario: ~0 rows (aproximadamente)
DELETE FROM `usuario`;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`id`, `nombre`, `contrasenia`,`email`, `imagen`) VALUES
	(1, 'admin', '123456','admin@ipartek.es','http://emser.es/wp-content/uploads/2016/08/usuario-sin-foto.png'),
	(2, 'pepe', 'pepe', 'pepe@ipartek.es', 'http://emser.es/wp-content/uploads/2016/08/usuario-sin-foto.png');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;