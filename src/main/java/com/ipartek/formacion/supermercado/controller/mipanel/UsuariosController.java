package com.ipartek.formacion.supermercado.controller.mipanel;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.io.IOException;
import java.sql.Timestamp;
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
import com.ipartek.formacion.supermercado.modelo.dao.RolDAO;
import com.ipartek.formacion.supermercado.modelo.dao.UsuarioDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;
import com.ipartek.formacion.supermercado.modelo.pojo.Rol;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/mipanel/usuarios")
public class UsuariosController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(UsuariosController.class);

	private static final String VIEW_TABLA = "usuarios/index.jsp";
	private static final String VIEW_FORM = "usuarios/formulario.jsp";
	private static final int MIN_CAR = 2;
	private static final int MAX_CAR = 150;
	private static String FORWARD = VIEW_TABLA;

	private static UsuarioDAO dao;
	private static RolDAO daoRol;

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
	String pContrasenia = "";
	String pEmail = "";
	String pImagen = "";
	String pFechaCreacion = "";
	String pFechaModificacion = "";
	String pFechaEliminacion = "";
	String pRol = "";

	@Override
	public void init() throws ServletException {
		super.init();
		dao = UsuarioDAO.getInstance();
		// Crear Factoria y Validador
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Override
	public void destroy() {
		super.destroy();
		dao = null;
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

		doRecogerDatos(request, response);

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

			request.setAttribute("productos", dao.getAllByUser(idSesion));
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
		String vista = "";
		HttpSession session = request.getSession();
		Usuario usuarioSesion = (Usuario)session.getAttribute("usuarioLogeado");
		int idSesion = usuarioSesion.getId();
		
		if (destino.equals(VIEW_FORM)) {
			int id;
			Usuario usuarioForm = null;
			try {
				id = Integer.parseInt(request.getParameter("id"));

				if (pId != null) {
					usuarioForm = dao.getById(id);
				}

				if (usuarioForm == null) {
					usuarioForm = new Usuario();
				}
			} catch (NumberFormatException e) {
				if (usuarioForm == null) {
					usuarioForm = new Usuario();
				}
			}

			request.setAttribute("usuario", usuarioForm);

			vista = destino;
		}

		if (destino.equals(VIEW_TABLA)) {
			request.setAttribute("usuarios", dao.getAllByUser(idSesion));
			vista = destino;
		}
		return vista;
	}

	private void doRecogerDatos(HttpServletRequest request, HttpServletResponse response) {
		pId = request.getParameter("id");
		pNombre = request.getParameter("nombre");
		pContrasenia = request.getParameter("contrasenia");
		pEmail = request.getParameter("email");
		pImagen = request.getParameter("imagen");
		pFechaCreacion = request.getParameter("fecha-creacion");
		pFechaModificacion = request.getParameter("fecha-modificacion");
		pFechaEliminacion = request.getParameter("fecha-eliminacion");
		pRol = request.getParameter("rol");
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("id"));
		dao.delete(id);
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Timestamp fechaCrea = null;
		Timestamp fechaMod = null;
		Timestamp fechaElim = null;
		Rol rol = null;
		
		if (pFechaCreacion != null) {
			fechaCrea = Timestamp.valueOf(pFechaCreacion);
		}else {
			fechaCrea = new Timestamp(System.currentTimeMillis());
		}
		
		if (pFechaModificacion != null) {
			fechaMod = Timestamp.valueOf(pFechaModificacion);
		}
		
		if (pFechaEliminacion != null) {
			fechaElim = Timestamp.valueOf(pFechaEliminacion);
		}
		
		if (pRol != null) {
			rol = daoRol.getById(Integer.parseInt(pRol));
		}else {
			rol = new Rol();
		}
		
		Usuario pGuardar = new Usuario(Integer.parseInt(pId), pNombre, pContrasenia, pEmail, pImagen,
				Timestamp.valueOf(pFechaCreacion), fechaMod,
				fechaElim, rol);

		validator.validate(pGuardar);

		// Obtener las ConstrainViolation
		Set<ConstraintViolation<Usuario>> violations = validator.validate(pGuardar);
		if (violations.size() > 0) {
			/* No ha pasado la valiadacion, iterar sobre los mensajes de validacion */
			for (ConstraintViolation<Usuario> cv : violations) {
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
			String contrasenia = pContrasenia;
			String email = pEmail;
			String imagen = pImagen;
			Timestamp fechaCreacion = Timestamp.valueOf(pFechaCreacion);
			Timestamp fechaModificacion = Timestamp.valueOf(pFechaModificacion);
			Timestamp fechaEliminacion = Timestamp.valueOf(pFechaEliminacion);
			
			HttpSession session = request.getSession();
			Usuario usuarioSesion = (Usuario)session.getAttribute("usuarioLogeado");
			int idSesion = usuarioSesion.getId();

			Usuario pojo = null;
			List<Usuario> listado = dao.getAllByUser(idSesion);
			if (id == 0) {
				pojo = new Usuario(id, nombre, contrasenia, email, imagen, fechaCreacion, fechaModificacion, fechaEliminacion, rol);
				dao.create(pojo);
			} else {
				for (Usuario usuario : listado) {
					if (usuario.getId() == id) {
						usuario.setNombre(nombre);
						usuario.setContrasenia(contrasenia);
						usuario.setEmail(email);
						usuario.setImagen(imagen);
						usuario.setFechaCreacion(fechaCreacion);
						usuario.setFechaModificacion(fechaModificacion);
						usuario.setFechaEliminacion(fechaEliminacion);
						dao.update(usuario.getId(), usuario);
					}
				}
			}
			mensajes.clear();
			vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
		}
		request.setAttribute("mensajesAlerta", mensajes);
	}

	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		Usuario usuarioForm = null;

		if (pId != null) {
			usuarioForm = dao.getById(Integer.parseInt(pId));
		}

		if (usuarioForm == null) {
			usuarioForm = new Usuario();
		}
		request.setAttribute("usuario", usuarioForm);
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_FORM);
	}

	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Usuario usuarioSesion = (Usuario)session.getAttribute("usuarioLogeado");
		int idSesion = usuarioSesion.getId();
		
		request.setAttribute("usuarios", dao.getAllByUser(idSesion));
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

}
