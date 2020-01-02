package com.ipartek.formacion.supermercado.controller.mipanel;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
	private static final int MIN_CAR = 2;
	private static final int MAX_CAR = 150;
	private static String FORWARD = VIEW_TABLA;

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

	String pId = "";
	String pNombre = "";
	String pPrecio = "";
	String pImagen = "";
	String pDescripcion = "";
	String pDescuento = "";
	Usuario pUsuario = null;

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

	private void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// recoger parametros
		String pAccion = request.getParameter("accion");
		
		HttpSession session = request.getSession();
		Usuario usuarioSesion = (Usuario)session.getAttribute("usuarioLogeado");
		int idSesion = usuarioSesion.getId();

		mapper(request, response);

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

			request.setAttribute("productos", daoProducto.getAllByUser(idSesion));
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
		Usuario usuarioSesion = (Usuario)session.getAttribute("usuarioLogeado");
		int idSesion = usuarioSesion.getId();
		
		String vista = "";
		if (destino.equals(VIEW_FORM)) {
			int id;
			Producto productoForm = null;
			try {
				id = Integer.parseInt(request.getParameter("id"));

				if (pId != null) {
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

			request.setAttribute("usuarios", daoUsuario.getAllByUser(idSesion));
			request.setAttribute("producto", productoForm);

			vista = destino;
		}

		if (destino.equals(VIEW_TABLA)) {
			request.setAttribute("productos", daoProducto.getAllByUser(idSesion));
			vista = destino;
		}
		return vista;
	}

	private void mapper(HttpServletRequest request, HttpServletResponse response) {
		pId = request.getParameter("id");
		pNombre = request.getParameter("nombre");
		pPrecio = request.getParameter("precio");
		pImagen = request.getParameter("imagen");
		pDescripcion = request.getParameter("descripcion");
		pDescuento = request.getParameter("descuento");
		if (request.getParameter("usuario") == null) {
			if (request.getParameter("idUsuario") != null) {
				pUsuario = daoUsuario.getById(Integer.parseInt(request.getParameter("idUsuario")));
			}
		}
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("id"));
		daoProducto.delete(id);
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Producto pGuardar = new Producto(Integer.parseInt(pId), pNombre, Float.parseFloat(pPrecio), pImagen,
				pDescripcion, Integer.parseInt(pDescuento), pUsuario);

		validator.validate(pGuardar);

		// Obtener las ConstrainViolation
		Set<ConstraintViolation<Producto>> violations = validator.validate(pGuardar);
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
			int id = Integer.parseInt(pId);
			String nombre = pNombre;
			float precio = Float.parseFloat(pPrecio);
			String imagen = pImagen;
			String descripcion = pDescripcion;
			int descuento = Integer.parseInt(pDescuento);
			
			HttpSession session = request.getSession();
			Usuario usuarioSesion = (Usuario)session.getAttribute("usuarioLogeado");
			int idSesion = usuarioSesion.getId();

			Producto pojo = null;
			List<Producto> listado = daoProducto.getAllByUser(idSesion);
			if (id == 0) {
				pojo = new Producto(nombre, precio, imagen, descripcion, descuento);
				daoProducto.create(pojo);
			} else {
				for (Producto producto : listado) {
					if (producto.getId() == id) {
						producto.setNombre(nombre);
						producto.setImagen(imagen);
						producto.setDescripcion(descripcion);
						producto.setDescuento(descuento);
						producto.setPrecio(precio);
						producto.setFechaCreacion(producto.getFechaCreacion());
						producto.setFechaModificacion(producto.getFechaModificacion());
						producto.setFechaEliminacion(producto.getFechaEliminacion());
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

		if (pId != null) {
			productoForm = daoProducto.getById(Integer.parseInt(pId));
		}

		if (productoForm == null) {
			productoForm = new Producto();
		}

		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_FORM);
	}

	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Usuario usuarioSesion = (Usuario)session.getAttribute("usuarioLogeado");
		int idSesion = usuarioSesion.getId();
		
		request.setAttribute("productos", daoProducto.getAllByUser(idSesion));
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

}
