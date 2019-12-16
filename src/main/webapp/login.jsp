<%@ page contentType="text/html; charset=UTF-8"%>

<%@include file="/include/header.jsp"%>

<div class="row justify-content-center">
	<div class="col-4 mt-5">
		<form action="login" method="post">
			<div class="form-group">
				<label for="nombre">Nombre</label> <input type="text"
					class="form-control" name="nombre" id="nombre"
					aria-describedby="Nombre" required autofocus>
			</div>
			<div class="form-group">
				<label for="password">Contraseña</label> <input type="text"
					class="form-control" name="password" id="password"
					aria-describedby="Contraseña" required>
			</div>
			<button type="submit" class="btn btn-primary btn-block">Entrar</button>
		</form>
	</div>
</div>

<%@include file="/include/footer.jsp"%>