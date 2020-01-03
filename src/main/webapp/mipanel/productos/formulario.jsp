<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>

<div class="container">
	<!-- Page Heading -->
	<h1 class="text-center mb-4 mt-2 text-gray-800">Productos</h1>

	<!-- Begin Page Content -->
	<div class="row justify-content-center h-75">
		<div class="col-sm-7">
			<form action="seguridad/productos" class="user" method="post">
				<div id="formulario" class="card w-100">
					<div class="form-group card-body shadow">
						<label for="id">ID</label> 
						<input type="text" name="id"
							placeholder="ID" value="${producto.id}" required="required"
							readonly class="form-control mb-2 p-2" />
						<label for="nombre">Nombre</label>
						<input type="text" name="nombre" placeholder="Nombre"
							value="${producto.nombre}" required="required" pattern="{1,100}"
							class="form-control mb-2 p-2" />
						<label for="imagen">Imagen</label>
						<input type="text" id="imagen" name="imagen"
							placeholder="URL de la imagen" value="${producto.imagen}"
							required="required" pattern="http(|s):.*\.(jpg|png|jpeg|gif)"
							onblur="cargarImagen()" class="form-control mb-2 p-2" />
						<label for="precio">Precio</label>
						<input type="text" name="precio"
							placeholder="Precio" value="${producto.precio}"
							required="required" class="form-control mb-2 p-2" />
						<label for="descuento">Descuento</label>
						<input type="text"
							name="descuento" placeholder="Descuento"
							value="${producto.descuento}" required="required"
							pattern="(100)|(0*\d{1,2})" class="form-control mb-2 p-2" />
						<label for="descripcion">Descripción</label>
						<input type="text"
							name="descripcion" placeholder="Descripción"
							value="${producto.descripcion}" required="required"
							class="form-control mb-2 p-2" />
						<input type="hidden" name="usuarioId" value="${producto.usuario.nombre}" />
						<input type="text" name="usuario_id"
							placeholder="ID" value="${producto.id}" required="required"
							readonly class="form-control mb-2 p-2" />
						<c:if test="${producto.fechaCreacion!=null}">
							<label for="fecha_creacion">Fecha de Creación</label>
							<input type="text" name="fecha_creacion" placeholder="Fecha de creación del usuario"
								value="${producto.fechaCreacion}" required="required"
								readonly  class="form-control mb-2 p-2" />
						</c:if>
						<c:if test="${producto.fechaModificacion!=null}">
							<label for="fecha_modificacion">Fecha de Modificación</label>
							<input type="text" name="fecha_modificacion" placeholder="Fecha de última modificación del usuario"
								value="${producto.fechaModificacion}"
								readonly  class="form-control mb-2 p-2" />
						</c:if>
						<c:if test="${producto.fechaEliminacion!=null}">
							<label for="fecha_eliminacion">Fecha de Eliminación</label>
							<input type="text" name="fecha_eliminacion" placeholder="Fecha de Eliminación del usuario"
								value="${producto.fechaEliminacion}"
								readonly  class="form-control mb-2 p-2" />
						</c:if>
						<input type="hidden" name="idUsuario" value="${producto.usuario.id}" />
						<input type="hidden" name="accion" value="guardar" />
						<input class="btn btn-primary" type="submit" value="Inscribir">
						<c:if test="${producto.id!=0}">
							<button type="button" class="btn btn-warning" data-toggle="modal"
								data-target="#eliminarModal">Eliminar</button>
						</c:if>
					</div>
					<!-- card-body -->
				</div>
				<!-- card -->
			</form>

		</div>

		<!-- Modal -->
		<div class="modal fade" id="eliminarModal" tabindex="-1" role="dialog"
			aria-labelledby="exampleModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="exampleModalLabel">Eliminar</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">¿Seguro que quieres eliminarlo?</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">No</button>
						<a href="seguridad/productos?accion=eliminar&id=${producto.id}"
							class="btn btn-warning">Eliminar</a>
					</div>
				</div>
			</div>
		</div>
		<!-- FIn del Modal -->

		<!-- / .col-sm-6 -->
		<div class="col-sm-5 p-3 bg-register-image card-body shadow bg-white"
			id="container-img"></div>
		<script>
			function cargarImagen() {
				var imagen = document.getElementById("imagen");
				var urlImagen = imagen.value;
				console.debug('url %o', urlImagen);
				let container = document.getElementById('container-img');

				container.style.backgroundImage = "url(" + urlImagen + ")";
				container.style.backgroundPosition = "center";
				container.style.backgroundSize = "cover";
			}

			window.onload = cargarImagen();
		</script>
	</div>
	<!-- End of row -->
</div>
<!-- End of container -->




<%@include file="/include/footer.jsp"%>