<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>

<div class="container">
	<!-- Page Heading -->
	<h1 class="text-center mb-4 mt-2 text-gray-800">Categoría</h1>

	<!-- Begin Page Content -->
	<div class="row justify-content-center h-75">
		<div class="col-sm-7">
			<form action="seguridad/categorias" class="user" method="post">
				<div id="formulario" class="card w-100">
					<div class="form-group card-body shadow">
						<label for="id">ID</label> 
						<input type="text" name="id"
							placeholder="ID" value="${categoria.id}" required="required"
							readonly class="form-control mb-2 p-2" />
						<label for="nombre">Nombre</label>
						<input type="text" name="nombre" placeholder="Nombre"
							value="${categoria.nombre}" required="required" pattern="{1,100}"
							class="form-control mb-2 p-2" />
						<label for="imagen">Imagen</label>
						<input type="text" id="imagen" name="imagen"
							placeholder="URL de la imagen" value="${categoria.imagen}"
							required="required" pattern="http(|s):.*\.(jpg|png|jpeg|gif)"
							onblur="cargarImagen()" class="form-control mb-2 p-2" />
						<input type="hidden" name="accion" value="guardar" />
						<input class="btn btn-primary" type="submit" value="Inscribir">
						<c:if test="${categoria.id!=0}">
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
						<a href="seguridad/categorias?accion=eliminar&id=${categoria.id}"
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