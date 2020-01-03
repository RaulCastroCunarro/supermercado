<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>



<div class="card shadow mb-4">
	<div class="card-header py-3">
		<h6 class="m-0 font-weight-bold text-center">
			<a href="seguridad/productos?accion=formulario&id=0"
				class="btn btn-primary">Nuevo Producto</a>
		</h6>
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
						<th>Fecha Última Modificación</th>
						<th>Usuario</th>
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
						<th>Fecha Última Modificación</th>
						<th>Usuario</th>
					</tr>
				</tfoot>
				<tbody>
					<c:forEach items="${productos}" var="p">
						<tr>
							<td>${p.id}</td>
							<td><img class="img-thumbnail rounded-circle img-tabla"
								src="${p.imagen}"></td>
							<td>${p.nombre}</td>
							<td>${p.precio}</td>
							<td>${p.descripcion}</td>
							<td>${p.descuento}</td>
							<c:if test="${producto.fechaModificacion == null}">
								<td>${p.fechaCreacion}</td>
							</c:if>
							<c:if test="${producto.fechaModificacion != null}">
								<td>${p.fechaModificacion}</td>
							</c:if>
							<td>${p.usuario.nombre}</td>
							<td><a
								href="seguridad/productos?accion=formulario&id=${p.id}">Editar</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<%@include file="/include/footer.jsp"%>