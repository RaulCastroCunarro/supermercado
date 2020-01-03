package com.ipartek.formacion.supermercado.controller.mipanel;

import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.ipartek.formacion.supermercado.controller.Alerta;
import com.ipartek.formacion.supermercado.modelo.dao.ProductoDAO;
import com.ipartek.formacion.supermercado.modelo.dao.UsuarioDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/mipanel/productos")
public class ProductosController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(ProductosController.class);

	private static final String VIEW_TABLA = "productos/index.jsp";
	private static final String VIEW_FORM = "productos/formulario.jsp";

	private static ProductoDAO daoProducto;
	private static UsuarioDAO daoUsuario;

	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_FORM = "formulario";
	public static final String ACCION_GUARDAR = "guardar"; // crear y modificar
	public static final String ACCION_ELIMINAR = "eliminar";

	// Crear Factoria y Validador
	ValidatorFactory factory;
	Validator validator;

	String vistaSeleccionada = VIEW_TABLA;
	ArrayList<Alerta> mensajes = new ArrayList<Alerta>();

	String pAccion = "";

	int pId = 0;
	String pNombre = "";
	float pPrecio = 0;
	String pImagen = "";
	String pDescripcion = "";
	int pDescuento = 0;
	Timestamp pFechaCreacion = null;
	Timestamp pFechaModificacion = null;
	Timestamp pFechaEliminacion = null;
	Usuario pUsuario = null;

	Producto pProducto = null;
	
	Usuario usuarioSesion = null;

	@Override
	public void init() throws ServletException {
		super.init();
		daoProducto = ProductoDAO.getInstance();
		daoUsuario = UsuarioDAO.getInstance();
		// Crear Factoria y Validador
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Override
	public void destroy() {
		super.destroy();
		daoProducto = null;
		daoUsuario = null;
		factory = null;
		validator = null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		usuarioSesion = (Usuario) session.getAttribute("usuarioLogeado");
		
		pProducto = mapper(req, resp);
		
		String pAccion = req.getParameter("accion");
		if (pAccion == ACCION_FORM || pAccion == ACCION_ELIMINAR || pAccion == ACCION_GUARDAR) {
			if (enPropiedad()) {
				super.service(req, resp);
			}else {
				resp.sendRedirect("/logout");
			}
		} else {
			super.service(req, resp);
		}
	}

	private boolean enPropiedad() {
		
		boolean resultado = false;
		
		Producto producto = daoProducto.getById(pProducto.getId());
		
		if (producto != null) {
			if(usuarioSesion.getId() == producto.getUsuario().getId()) {
				resultado = true;
			}
		}else if(pProducto.getId() == 0){
			resultado = true;
		}
		
		return resultado;
	}

	private void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// recoger parametros
		String pAccion = request.getParameter("accion");

		try {
			// TODO log
			switch (pAccion) {
			case ACCION_LISTAR:
				listar(request, response);
				break;

			case ACCION_FORM:
				irFormulario(request, response);
				break;

			case ACCION_GUARDAR:
				guardar(request, response);
				break;

			case ACCION_ELIMINAR:
				eliminar(request, response);
				break;

			default:
				listar(request, response);
				break;
			}

		} catch (MySQLIntegrityConstraintViolationException e) {
			mensajes.add(new Alerta("El nombre de ese producto ya existe.", Alerta.TIPO_DANGER));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			request.setAttribute("mensajesAlerta", mensajes);

			request.getRequestDispatcher(vistaSeleccionada).forward(request, response);
		}
	}

	private String operacionesVista(HttpServletRequest request, HttpServletResponse response, String destino) {

		HttpSession session = request.getSession();
		Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogeado");
		int idUsuSesion = usuarioSesion.getId();

		String vista = "";
		if (destino.equals(VIEW_FORM)) {
			int id;
			Producto productoForm = null;
			try {
				id = Integer.parseInt(request.getParameter("id"));

				if (pId != 0) {
					productoForm = daoProducto.getById(id);
				}

				if (productoForm == null) {
					productoForm = new Producto();
				}
			} catch (NumberFormatException e) {
				if (productoForm == null) {
					productoForm = new Producto();
				}
			}

			request.setAttribute("usuarios", daoUsuario.getAllByUser(idUsuSesion));
			request.setAttribute("producto", productoForm);

			vista = destino;
		}

		if (destino.equals(VIEW_TABLA)) {
			request.setAttribute("productos", daoProducto.getAllByUser(idUsuSesion));
			vista = destino;
		}
		return vista;
	}

	private Producto mapper(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("id") != null) {
			pId = Integer.parseInt(request.getParameter("id"));
		}

		pNombre = request.getParameter("nombre");

		if (request.getParameter("precio") != null) {
			pPrecio = Float.parseFloat(request.getParameter("precio"));
		}

		pImagen = request.getParameter("imagen");
		pDescripcion = request.getParameter("descripcion");

		if (request.getParameter("descuento") != null) {
			pDescuento = Integer.parseInt(request.getParameter("descuento"));
		}

		String fechaCreacion = request.getParameter("fecha_creacion");
		String fechaModificacion = request.getParameter("fecha_modificacion");
		String fechaEliminacion = request.getParameter("fecha_eliminacion");

		if (fechaCreacion != null) {
			pFechaCreacion = Timestamp.valueOf(fechaCreacion);
		} else {
			pFechaCreacion = new Timestamp(System.currentTimeMillis());
		}

		if (fechaModificacion != null) {
			pFechaModificacion = Timestamp.valueOf(fechaModificacion);
		}

		if (fechaEliminacion != null) {
			pFechaEliminacion = Timestamp.valueOf(fechaEliminacion);
		}

		if (request.getParameter("usuario") == null) {
			if (request.getParameter("idUsuario") != null) {
				pUsuario = daoUsuario.getById(Integer.parseInt(request.getParameter("idUsuario")));
			}
		}

		Producto resultado = new Producto(pId, pNombre, pPrecio, pImagen, pDescripcion, pDescuento, pFechaCreacion,
				pFechaModificacion, pFechaEliminacion, pUsuario);

		return resultado;
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {

		daoProducto.delete(pProducto.getId());
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {

		validator.validate(pProducto);

		// Obtener las ConstrainViolation
		Set<ConstraintViolation<Producto>> violations = validator.validate(pProducto);
		if (violations.size() > 0) {
			/* No ha pasado la valiadacion, iterar sobre los mensajes de validacion */
			for (ConstraintViolation<Producto> cv : violations) {
				char[] caracteres = cv.getPropertyPath().toString().toCharArray();
				caracteres[0] = Character.toUpperCase(caracteres[0]);
				String campo = "";
				for (char chars : caracteres) {
					campo = campo + chars;
				}

				mensajes.add(new Alerta(campo + " " + cv.getMessage(), Alerta.TIPO_WARNING));
			}

			vistaSeleccionada = operacionesVista(request, response, VIEW_FORM);
		} else {

			Producto pojo = null;
			List<Producto> listado = daoProducto.getAllByUser(usuarioSesion.getId());
			if (pProducto.getId() == 0) {
				pojo = pProducto;
				daoProducto.create(pojo);
			} else {
				for (Producto producto : listado) {
					if (producto.getId() == pProducto.getId()) {
						producto.setNombre(pProducto.getNombre());
						producto.setImagen(pProducto.getImagen());
						producto.setDescripcion(pProducto.getDescripcion());
						producto.setDescuento(pProducto.getDescuento());
						producto.setPrecio(pProducto.getPrecio());
						producto.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
						daoProducto.update(producto.getId(), producto);
					}
				}
			}
			mensajes.clear();
			vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
		}
		request.setAttribute("mensajesAlerta", mensajes);
	}

	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		Producto productoForm = null;

		if (pId != 0) {
			productoForm = daoProducto.getById(pId);
		}

		if (productoForm == null) {
			productoForm = new Producto();
		}

		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_FORM);
	}

	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setAttribute("productos", daoProducto.getAllByUser(usuarioSesion.getId()));
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

}
