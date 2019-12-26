<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>



<div class="card shadow mb-4">
	<div class="card-header py-3">
		<h6 class="m-0 font-weight-bold text-center">
			<a href="seguridad/productos?accion=formulario&id=0"
				class="btn btn-primary">Nuevo Usuario</a>
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
						<th>Contraseña</th>
						<th>Email</th>
						<th>Fecha Creacion</th>
						<th>Fecha Modificacion</th>
						<th>Fecha Baja</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Id</th>
						<th>Foto</th>
						<th>Nombre</th>
						<th>Contraseña</th>
						<th>Email</th>
						<th>Fecha Creacion</th>
						<th>Fecha Modificacion</th>
						<th>Fecha Baja</th>
					</tr>
				</tfoot>
				<tbody>
					<c:forEach items="${usuarios}" var="u">
						<tr>
							<td>${u.id}</td>
							<td><img class="img-thumbnail rounded-circle img-tabla"
								src="${u.imagen}"></td>
							<td>${u.nombre}</td>
							<td>${u.contrasenia}</td>
							<td>${u.email}</td>
							<td>${u.fechaCreacion}</td>
							<td>${u.fechaModificacion}</td>
							<td>${u.fechaEliminacion}</td>
							<td><a
								href="seguridad/usuarios?accion=formulario&id=${u.id}">Editar</a>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<%@include file="/include/footer.jsp"%>