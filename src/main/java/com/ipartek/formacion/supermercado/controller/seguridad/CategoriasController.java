package com.ipartek.formacion.supermercado.controller.seguridad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ipartek.formacion.supermercado.controller.Alerta;
import com.ipartek.formacion.supermercado.modelo.dao.CategoriaDAO;
import com.ipartek.formacion.supermercado.modelo.dao.UsuarioDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Categoria;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/seguridad/categorias")
public class CategoriasController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = LogManager.getLogger(CategoriasController.class);

	private static final String VIEW_TABLA = "categorias/index.jsp";
	private static final String VIEW_FORM = "categorias/formulario.jsp";

	private static CategoriaDAO daoCategoria;
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
	String pImagen = "";
	
	Categoria pCategoria = null;
	
	Usuario usuarioSesion = null;

	@Override
	public void init() throws ServletException {
		super.init();
		daoCategoria = CategoriaDAO.getInstance();
		daoUsuario = UsuarioDAO.getInstance();
		// Crear Factoria y Validador
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Override
	public void destroy() {
		super.destroy();
		daoCategoria = null;
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
		LOG.debug("Entra en el Service");
		
		
		HttpSession session = req.getSession();
		usuarioSesion = (Usuario) session.getAttribute("usuarioLogeado");
		LOG.debug("Carga la sesión del Usuario");
		
		pCategoria = mapper(req, resp);
		
		pAccion = req.getParameter("accion");
		LOG.debug("accion: " + pAccion);
		
		super.service(req, resp);
	}
	
	private Categoria mapper(HttpServletRequest request, HttpServletResponse response) {
		
		LOG.debug("Entra en el mapper");
		
		if (request.getParameter("id") != null) {
			pId = Integer.parseInt(request.getParameter("id"));
		}
		
		pNombre = request.getParameter("nombre");
		pImagen = request.getParameter("imagen");
		
		Categoria resultado = new Categoria(pId, pNombre, pImagen);

		LOG.debug("Devuelve la Categoria mapeada: " + resultado.toString());
		
		return resultado;
	}
	
	private void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LOG.debug("Entra en el doAction; accion: " + pAccion);
		
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

			//request.setAttribute("productos", daoProducto.getAll());
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
			Categoria categoriaForm = null;
			try {
				if (pId != 0) {
					LOG.debug("Recupera la Categoria por su Id");
					categoriaForm = daoCategoria.getById(pId);
				}

				if (categoriaForm == null) {
					LOG.debug("Genera una nueva Categoria");
					categoriaForm = new Categoria();
				}
			} catch (NumberFormatException e) {
				if (categoriaForm == null) {
					LOG.debug("Genera una nueva Categoria");
					categoriaForm = new Categoria();
				}
			}

			LOG.debug("Pasa el Usuario y los Productos a la request");
			request.setAttribute("usuarios", daoUsuario.getAll());
			request.setAttribute("categoria", categoriaForm);

			vista = destino;
		}

		if (destino.equals(VIEW_TABLA)) {
			LOG.debug("Pasa la lista de Productos del Usuario a la request");
			request.setAttribute("categorias", daoCategoria.getAll());
			vista = destino;
		}
		return vista;
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOG.debug("Entra en eliminar");
		
		daoCategoria.delete(pCategoria.getId());
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {

		validator.validate(pCategoria);

		// Obtener las ConstrainViolation
		Set<ConstraintViolation<Categoria>> violations = validator.validate(pCategoria);
		if (violations.size() > 0) {
			LOG.debug("No pasa las validaciones");
			/* No ha pasado la valiadacion, iterar sobre los mensajes de validacion */
			for (ConstraintViolation<Categoria> cv : violations) {
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
			
			if (pCategoria.getId() == 0) {
				LOG.debug("Crea un Producto nuevo");
				daoCategoria.create(pCategoria);
			} else {
				LOG.debug("Itera para encontrar la Categoria correcta");
				List<Categoria> listado = daoCategoria.getAll();
				for (Categoria categoria : listado) {
					if (categoria.getId() == pCategoria.getId()) {
						categoria.setNombre(pCategoria.getNombre());
						categoria.setImagen(pCategoria.getImagen());
						LOG.debug("Modifica el producto");
						daoCategoria.update(categoria.getId(), categoria);
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

		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_FORM);
	}

	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOG.debug("Entra en listar");
		
		
		request.setAttribute("categorias", daoCategoria.getAll());
		LOG.debug("Pasa el listado de categorias a la request");
		
		
		mensajes.clear();
		vistaSeleccionada = operacionesVista(request, response, VIEW_TABLA);
	}

}
