package com.ipartek.formacion.supermercado.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String USUARIO = "admin";
	private static final String PASSWORD = "admin";
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String view = "login.jsp";
		String nombre = request.getParameter("nombre");
		String pass = request.getParameter("password");
		
		try {
			if(USUARIO.equals(nombre) && PASSWORD.equals(pass)){
				HttpSession sesion = request.getSession();
				sesion.setMaxInactiveInterval(60*3);
				
				view = "seguridad/index.jsp";
			}else {
				request.setAttribute("mensajeAlerta", new Alerta("Datos incorrectos",Alerta.TIPO_DANGER));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			request.getRequestDispatcher(view).forward(request,response);
		}
	}

}
