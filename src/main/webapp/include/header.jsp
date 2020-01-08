<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setLocale value="es_ES" />

<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="Ander Uraga">
<title>Supermercado</title>
<base href="${pageContext.request.contextPath}/">
<!-- Bootstrap core CSS -->
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	rel="stylesheet">

<!-- nuestro css -->
<link rel="stylesheet" href="css/custom.css">

<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.8.1/css/all.css"
	integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf"
	crossorigin="anonymous">

</head>
<body id="top">


	<%@include file="/include/snow.jsp"%>


	<nav class="navbar navbar-expand-lg bg-navbar sticky-top text-primary">

		<button class="navbar-toggler text-primary" type="button"
			data-toggle="collapse" data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"><i class="fas fa-bars"></i></span>
		</button>
		
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav container d-flex flex-column flex-md-row justify-content-between">
				<li class="nav-item"><a class="navbar-brand text-primary"
					href="inicio"><i class="fas fa-home"></i></a></li>

				<c:if test="${empty usuarioLogeado}">
					<li class="nav-item"><a class="py-2 d-none d-md-inline-block"
						href="login.jsp">Login</a></li>
				</c:if>
				<c:if test="${usuarioLogeado.rol.id eq 2}">
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle text-primary" href="#"
						id="navbarDropdown" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false">Categorías</a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="seguridad/categorias?accion=listar">Todas</a>
							<a class="dropdown-item"
								href="seguridad/categorias?accion=formulario">Nueva</a>
						</div></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle text-primary" href="#"
						id="navbarDropdown" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false"> Productos </a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="seguridad/productos?accion=listar">Todos</a>
							<a class="dropdown-item"
								href="seguridad/productos?accion=formulario">Nuevo</a>
						</div></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle text-primary" href="#"
						id="navbarDropdown" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false"> Usuarios </a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="seguridad/usuarios?accion=listar">Todos</a> <a
								class="dropdown-item" href="seguridad/usuarios?accion=formulario">Nuevo</a>
						</div></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle text-primary" href="#"
						id="navbarDropdown" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false"><!-- <img class="" src="${usuarioLogeado.getImagen()}" alt="Imagen de Perfil de ${usuarioLogeado.getNombre()}"></img> --></a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="seguridad/usuarios?accion=formulario&id=${usuarioLogeado.id}">${usuarioLogeado.getNombre()}</a> <a
								class="dropdown-item" href="" data-toggle="modal"
								data-target="#cerrarSesionModal">Logout <i
								class="fas fa-sign-out-alt"></i></a>
						</div></li>
				</c:if>
				<c:if test="${usuarioLogeado.rol.id eq 1}">
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle text-primary" href="#"
						id="navbarDropdown" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false"> Productos </a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="mipanel/productos?accion=listar">Todos</a>
							<a class="dropdown-item"
								href="mipanel/productos?accion=formulario">Nuevo</a>
						</div></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle text-primary" href="#"
						id="navbarDropdown" role="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false"><!-- <img class="" src="${usuarioLogeado.getImagen()}" alt="Imagen de Perfil de ${usuarioLogeado.getNombre()}"></img> --></a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="mipanel/usuarios?accion=formulario&id=${usuarioLogeado.id}">${usuarioLogeado.getNombre()}</a> <a
								class="dropdown-item" href="" data-toggle="modal"
								data-target="#cerrarSesionModal">Logout <i
								class="fas fa-sign-out-alt"></i></a>
						</div></li>
				</c:if>
			</ul>
		</div>
	</nav>

	<!-- Modal-->
	<div class="modal fade" id="cerrarSesionModal" tabindex="-1"
		role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Cerrar Sesión</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">¿Seguro que quieres cerrar tu sesión?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">No</button>
					<a href="logout" class="btn btn-warning">Cerrar</a>
				</div>
			</div>
		</div>
	</div>
	<!-- Fin del Modal -->

	<main class="container">

		<c:if test="${not empty mensajesAlerta}">
			<c:forEach items="${mensajesAlerta}" var="m">
				<div class="alert alert-${m.tipo} alert-dismissible fade show mt-3"
					role="alert">
					${m.mensaje}
					<button type="button" class="close" data-dismiss="alert"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
			</c:forEach>
		</c:if>