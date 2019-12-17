<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>

<h1>Formulario</h1>

<section class="m-auto">
	<form action="seguridad/productos" method="post">
		<input type="text" name="id" placeholder="ID" value="${producto.id}"
			required="required" readonly/><br> 
		<input type="text" name="nombre" placeholder="Nombre" value="${producto.nombre}"
			required="required" /><br>
		<input type="text" name="imagen" placeholder="URL de la imagen" value="${producto.imagen}"
			required="required" /><br>
		<input type="text" name="precio" placeholder="Precio" value="${producto.precio}"
			required="required" /><br>
		<input type="text" name="descuento" placeholder="Descuento" value="${producto.descuento}"
			required="required" /><br>
		<input type="text" name="descripcion" placeholder="Descripción" value="${producto.descripcion}"
			required="required" /><br> 
		<input type="hidden" name="accion" value="guardar"/>
		<input class="btn btn-primary"
			type="submit" value="Inscribir"> 
		<c:if test="${producto.id!=0}">
			<a href="seguridad/productos?accion=eliminar&${producto.id}" class="btn btn-warning">Eliminar</a>
		</c:if>
	</form>
</section>
<%@include file="/include/footer.jsp"%>