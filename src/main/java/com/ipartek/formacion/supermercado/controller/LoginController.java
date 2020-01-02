package com.ipartek.formacion.supermercado.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.ipartek.formacion.supermercado.modelo.dao.ProductoDAO;
import com.ipartek.formacion.supermercado.modelo.dao.UsuarioDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Rol;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(LoginController.class);
	
	private static UsuarioDAO dao = null;

	// Crear Factoria y Validador
	ValidatorFactory factory;
	Validator validator;

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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String view = "login.jsp";
		String nombre = request.getParameter("nombre");
		String pass = request.getParameter("password");
		ArrayList<Alerta> mensajes = new ArrayList<Alerta>();

		try {
			
			Usuario usuario = dao.exist(nombre, pass);
			if (usuario != null) {
				LOG.info("login correcto " + usuario);
				HttpSession sesion = request.getSession();
				sesion.setAttribute("usuarioLogeado", usuario);
				sesion.setMaxInactiveInterval(-1/* 60*3 */);
				
				if (usuario.getRol().getId() == Rol.ROL_ADMINISTRADOR) {
					view = "seguridad/index.jsp"; //accedemos al BACK-OFFICE
				}else {
					view = "mipanel/index.jsp"; //accedemos al BACK-OFFICE
				}
				
			} else {
				mensajes.add(new Alerta("Datos incorrectos", Alerta.TIPO_DANGER));
				request.setAttribute("mensajesAlerta", mensajes);
			}
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			request.getRequestDispatcher(view).forward(request, response);
		}
	}

}
