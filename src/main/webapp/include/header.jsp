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


	<nav class="site-header sticky-top py-1">
		<div
			class="container d-flex flex-column flex-md-row justify-content-between">
			<a class="py-2" href="inicio"> <svg
					xmlns="http://www.w3.org/2000/svg" width="24" height="24"
					fill="none" stroke="currentColor" stroke-linecap="round"
					stroke-linejoin="round" stroke-width="2" class="d-block mx-auto"
					role="img" viewBox="0 0 24 24" focusable="false">
					<title>Product</title><circle cx="12" cy="12" r="10" />
					<path
						d="M14.31 8l5.74 9.94M9.69 8h11.48M7.38 12l5.74-9.94M9.69 16L3.95 6.06M14.31 16H2.83m13.79-4l-5.74 9.94" /></svg>
			</a>
			<c:if test="${empty usuarioLogeado}">
				<a class="py-2 d-none d-md-inline-block" href="login.jsp">Login</a>
			</c:if>
			<c:if test="${not empty usuarioLogeado}">
				<a class="py-2 d-none d-md-inline-block"
					href="seguridad/productos?accion=listar">Tabla</a>
				<a class="py-2 d-none d-md-inline-block"
					href="seguridad/productos?accion=formulario">Formulario</a>
				<a class="py-2 d-none d-md-inline-block home" href=""
					data-toggle="modal" data-target="#cerrarSesionModal"><i
					class="fas fa-sign-out-alt"></i></a>
			</c:if>

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