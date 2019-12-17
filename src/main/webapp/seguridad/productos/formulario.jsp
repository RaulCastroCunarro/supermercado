<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>

<div class="container">
	<!-- Page Heading -->
	<h1 class="text-center mb-4 mt-2 text-gray-800">Formulario</h1>

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
						<input type="text" name="imagen" placeholder="URL de la imagen"
							value="${producto.imagen}" required="required" pattern="http(|s):.*\.(jpg|png|jpeg|gif)"
							onblur="cargarImagen(this.value)" class="form-control mb-2 p-2" />
						<label for="precio">Precio</label>
						<input type="text"
							name="precio" placeholder="Precio" value="${producto.precio}"
							required="required" class="form-control mb-2 p-2" />
						<label for="descuento">Descuento</label>
						<input type="text"
							name="descuento" placeholder="Descuento"
							value="${producto.descuento}" required="required" pattern="(100)|(0*\d{1,2})"
							class="form-control mb-2 p-2" />
						<label for="descripcion">Descripción</label>
						<input type="text" name="descripcion" placeholder="Descripción"
							value="${producto.descripcion}" required="required"
							class="form-control mb-2 p-2" />
						<input type="hidden"
							name="accion" value="guardar" />
						<input class="btn btn-primary"
							type="submit" value="Inscribir">
						<c:if test="${producto.id!=0}">
							<a href="seguridad/productos?accion=eliminar&id=${producto.id}"
								class="btn btn-warning">Eliminar</a>
						</c:if>
					</div>
					<!-- card-body -->
				</div>
				<!-- card -->
			</form>

		</div>
		<!-- / .col-sm-6 -->
		<div class="col-sm-5 p-3 bg-register-image card-body shadow bg-white"
			id="container-img"></div>
		<script>
			function cargarImagen(urlImagen) {
				console.debug('url %o', urlImagen);
				let container = document.getElementById('container-img');

				/*let imagen = '<img src="'+ url + '" alt="imagen del disco"';
				container.innerHTML = imagen;*/

				container.style.backgroundImage = "url(" + urlImagen + ")";
				container.style.backgroundPosition = "center";
				container.style.backgroundSize = "cover";
			}
		</script>
	</div>
	<!-- End of row -->
</div>
<!-- End of container -->
<%@include file="/include/footer.jsp"%>