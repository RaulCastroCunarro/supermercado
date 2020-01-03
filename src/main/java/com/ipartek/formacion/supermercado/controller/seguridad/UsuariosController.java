package com.ipartek.formacion.supermercado.controller.seguridad;

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
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.ipartek.formacion.supermercado.controller.Alerta;
import com.ipartek.formacion.supermercado.modelo.dao.RolDAO;
import com.ipartek.formacion.supermercado.modelo.dao.UsuarioDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Rol;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/seguridad/usuarios")
public class UsuariosController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(UsuariosController.class);

	private static final String VIEW_TABLA = "usuarios/index.jsp";
	private static final String VIEW_FORM = "usuarios/formulario.jsp";

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

	int pId = 0;
	String pNombre = "";
	String pContrasenia = "";
	String pEmail = "";
	String pImagen = "";
	Timestamp pFechaCreacion = null;
	Timestamp pFechaModificacion = null;
	Timestamp pFechaEliminacion = null;
	Rol pRol = null;

	Usuario pUsuario = null;

	@Override
	public void init() throws ServletException {
		super.init();
		dao = UsuarioDAO.getInstance();
		daoRol = RolDAO.getInstance();
		// Crear Factoria y Validador
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Override
	public void destroy() {
		super.destroy();
		dao = null;
		daoRol = null;
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

		pUsuario = mapper(request, response);

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

			request.setAttribute("productos", dao.getAll());
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
		if (destino.equals(VIEW_FORM)) {
			int id;
			Usuario usuarioForm = null;
			try {
				id = Integer.parseInt(request.getParameter("id"));

				if (pUsuario.getId() != 0) {
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
			request.setAttribute("usuarios", dao.getAll());
			vista = destino;
		}
		return vista;
	}

	private Usuario mapper(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("id") != null) {
			pId = Integer.parseInt(request.getParameter("id"));
		}

		pNombre = request.getParameter("nombre");

		pContrasenia = request.getParameter("contrasenia");
		pEmail = request.getParameter("email");
		pImagen = request.getParameter("imagen");

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

		if (request.getParameter("rol") != null) {
			pRol = daoRol.getById(Integer.parseInt(request.getParameter("rol")));
		} else {
			pRol = new Rol();
		}
		
		Usuario resultado = new Usuario(pId, pNombre, pContrasenia, pEmail, pImagen, pFechaCreacion,
				pFechaModificacion, pFechaEliminacion, pRol);
		
		return resultado;
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		dao.delete(pUsuario.getId());
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {

		validator.validate(pUsuario);

		// Obtener las ConstrainViolation
		Set<ConstraintViolation<Usuario>> violations = validator.validate(pUsuario);
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
			
			Usuario pojo = null;
			List<Usuario> listado = dao.getAll();
			if (pUsuario.getId() == 0) {
				
				String sRolId = request.getParameter("rol_id");
				int rolId = 0;
				
				if (sRolId != null) {
					rolId = Integer.parseInt(sRolId);
				}
				
				pojo = pUsuario;
				pojo.setRol(daoRol.getById(rolId));
				dao.create(pojo);
			} else {
				for (Usuario usuario : listado) {
					if (usuario.getId() == pUsuario.getId()) {
						usuario.setNombre(pUsuario.getNombre());
						usuario.setContrasenia(pUsuario.getContrasenia());
						usuario.setEmail(pUsuario.getEmail());
						usuario.setImagen(pUsuario.getImagen());
						usuario.setRol(pUsuario.getRol());
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

		if (pUsuario.getId() != 0) {
			usuarioForm = dao.getById(pUsuario.getId());
		}

		if (usuarioForm == null) {
			usuarioForm = new Usuario();
		}
		request.setAttribute("usuario", usuarioForm);
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_FORM);
	}

	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("usuarios", dao.getAll());
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

}
