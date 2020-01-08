<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>



<div class="card shadow mb-4">
	<div class="card-header py-3">
		<h6 class="m-0 font-weight-bold text-center">
			<a href="seguridad/categorias?accion=formulario&id=0"
				class="btn btn-primary">Nueva Categoría</a>
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
					</tr>
				</thead>
				<tfoot>
					<tr>
						<th>Id</th>
						<th>Foto</th>
						<th>Nombre</th>
					</tr>
				</tfoot>
				<tbody>
					<c:forEach items="${categorias}" var="c">
						<tr>
							<td>${c.id}</td>
							<td><img class="img-thumbnail rounded-circle img-tabla"
								src="${c.imagen}"></td>
							<td>${c.nombre}</td>
							<td><a
								href="seguridad/categorias?accion=formulario&id=${c.id}">Editar</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<%@include file="/include/footer.jsp"%>