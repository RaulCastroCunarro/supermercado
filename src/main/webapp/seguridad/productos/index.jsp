<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>

<h1>Tabla</h1>

<a href="seguridad/productos?accion=formulario&id=0">Nuevo Producto</a>

<div class="card shadow mb-4">
	<div class="card-header py-3">
		<h6 class="m-0 font-weight-bold text-center">Perros</h6>
	</div>
	<div class="card-body">
		<div class="table-responsive">
			<table class="table table-bordered text-center" id="dataTable"
				width="100%" cellspacing="0">
				<thead>
					<tr>
						<th>Id</th>
						<th>Foto</th>
						<th>Nombre</th>
						<th>Precio</th>
						<th>Descripción</th>
						<th>Descuento</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Id</th>
						<th>Foto</th>
						<th>Nombre</th>
						<th>Precio</th>
						<th>Descripción</th>
						<th>Descuento</th>
					</tr>
				</tfoot>
				<tbody>
					<c:forEach items="${productos}" var="p">
						<tr>
							<td>${p.id}</td>
							<td><img class="img-thumbnail rounded-circle"
								src="${p.imagen}"></td>
							<td>${p.nombre}</td>
							<td>${p.precio}</td>
							<td>${p.descripcion}</td>
							<td>${p.descuento}</td>
							<td><a
								href="seguridad/productos?accion=formulario&id=${p.id}">Editar</a>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<%@include file="/include/footer.jsp"%>