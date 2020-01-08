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
import javax.servlet.http.HttpSession;
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
	
	Usuario usuarioSesion = null;

	@Override
	public void init() throws ServletException {
		super.init();
		
		LOG.debug("Entra en el init");
		
		dao = UsuarioDAO.getInstance();
		daoRol = RolDAO.getInstance();
		// Crear Factoria y Validador
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Override
	public void destroy() {
		super.destroy();
		
		LOG.debug("Entra en el init");
		
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
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.debug("Entra en el Service");
		
		
		HttpSession session = req.getSession();
		usuarioSesion = (Usuario) session.getAttribute("usuarioLogeado");
		LOG.debug("Carga la sesión del Usuario");
		
		pUsuario = mapper(req, resp);
		pAccion = req.getParameter("accion");
		LOG.debug("accion " + pAccion);

		super.service(req, resp);
	}
	
	private Usuario mapper(HttpServletRequest request, HttpServletResponse response) {
		
		LOG.debug("Entra en el mapper");
		
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
		
		LOG.debug("Devuelve el Usuario mapeado");
		return resultado;
	}

	private void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LOG.debug("Entra en el doAction; accion " + pAccion);
		
		try {
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
			LOG.error(e);
			e.printStackTrace();
		} finally {
			request.setAttribute("mensajesAlerta", mensajes);

			request.getRequestDispatcher(vistaSeleccionada).forward(request, response);
		}
	}

	private String operacionesVista(HttpServletRequest request, HttpServletResponse response, String destino) {
		
		LOG.debug("Entra en operacionVista");
		
		String vista = "";
		if (destino.equals(VIEW_FORM)) {
			int id;
			Usuario usuarioForm = null;
			try {
				id = Integer.parseInt(request.getParameter("id"));

				if (pUsuario.getId() != 0) {
					LOG.debug("Recupera el Usuario por su Id");
					usuarioForm = dao.getById(id);
				}

				if (usuarioForm == null) {
					LOG.debug("Genera un nuevo Usuario");
					usuarioForm = new Usuario();
				}
			} catch (NumberFormatException e) {
				if (usuarioForm == null) {
					LOG.debug("Genera un nuevo Usuario");
					usuarioForm = new Usuario();
				}
			}

			LOG.debug("Pasa el Usuario a la request");
			request.setAttribute("usuario", usuarioForm);

			vista = destino;
		}

		if (destino.equals(VIEW_TABLA)) {
			LOG.debug("Pasa la lista de Usuarios a la request");
			request.setAttribute("usuarios", dao.getAll());
			vista = destino;
		}
		return vista;
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		LOG.debug("Entra en eliminar");
		
		dao.delete(pUsuario.getId());
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {

		LOG.debug("entra en guardar");
		
		validator.validate(pUsuario);

		// Obtener las ConstrainViolation
		Set<ConstraintViolation<Usuario>> violations = validator.validate(pUsuario);
		if (violations.size() > 0) {
			LOG.debug("No pasa las validaciones");
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
			
			LOG.debug("Validaciones correctas");
			
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
				LOG.debug("Crea un Usuario nuevo");
				dao.create(pojo);
			} else {
				LOG.debug("Itera para encontrar el Usuario correcto");
				for (Usuario usuario : listado) {
					if (usuario.getId() == pUsuario.getId()) {
						LOG.debug("Encuentra el id correcto");
						usuario.setNombre(pUsuario.getNombre());
						usuario.setContrasenia(pUsuario.getContrasenia());
						usuario.setEmail(pUsuario.getEmail());
						usuario.setImagen(pUsuario.getImagen());
						usuario.setRol(pUsuario.getRol());
						LOG.debug("Modifica el usuario");
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
		LOG.debug("Entra en irFormulario");

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
		
		LOG.debug("Entra en listar");
		
		request.setAttribute("usuarios", dao.getAll());
		LOG.debug("Pasa el listado de Usuarios a la request");
		
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

}
