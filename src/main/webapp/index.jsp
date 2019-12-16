<%@ page contentType="text/html; charset=UTF-8" %>

<%@include file="/include/header.jsp"%>

		<div class="row contenedor-productos">
			<c:forEach items="${productos}" var="p">
				<div class="col">
					<!-- producto -->
					<div class="producto">
						<c:if test="${p.descuento!=0}"><span class="descuento">${p.descuento}%</span> </c:if><img class="" src="${p.imagen}"
							alt="imagen de ${p.nombre}" >

						<div class="body">
							<p>
								<c:if test="${p.descuento!=0}"><span class="precio-descuento"><fmt:formatNumber type="currency" currencySymbol="€" value="${p.precio}" /></c:if></span> <span
									class="precio-original"><fmt:formatNumber type="currency" currencySymbol="€" value="${p.getPrecioDescuento()}" /></span>
							</p>
							<p class="text-muted precio-unidad">${p.nombre}</p>
							<p class="descripcion text-truncate">${p.descripcion}</p>
						</div>

						<div class="botones">
							<button class="minus">-</button>
							<input type="text" value="1">
							<button class="plus">+</button>
						</div>

						<button class="carro">añadir al carro</button>

					</div>
					<!-- /.producto -->

				</div>
			</c:forEach>
		</div>

	<%@include file="/include/footer.jsp"%>
	
	