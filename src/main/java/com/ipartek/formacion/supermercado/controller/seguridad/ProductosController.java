package com.ipartek.formacion.supermercado.controller.seguridad;

import java.util.List;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ipartek.formacion.supermercado.modelo.dao.ProductoDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/seguridad/productos")
public class ProductosController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW_TABLA = "productos/index.jsp";
	private static final String VIEW_FORM = "productos/formulario.jsp";
	private static String FORWARD = VIEW_TABLA;

	private static ProductoDAO dao;

	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_FORM = "formulario";
	public static final String ACCION_GUARDAR = "guardar"; // crear y modificar
	public static final String ACCION_ELIMINAR = "eliminar";

	String vistaSeleccionada = VIEW_TABLA;

	String pAccion = "";

	String pId = "";
	String pNombre = "";
	String pPrecio = "";
	String pImagen = "";
	String pDescripcion = "";
	String pDescuento = "";

	@Override
	public void init() throws ServletException {
		super.init();
		dao = ProductoDAO.getInstance();
	}

	@Override
	public void destroy() {
		super.destroy();
		dao = null;
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

		pId = request.getParameter("id");
		pNombre = request.getParameter("nombre");
		pPrecio = request.getParameter("precio");
		pImagen = request.getParameter("imagen");
		pDescripcion = request.getParameter("descripcion");
		pDescuento = request.getParameter("descuento");

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			request.getRequestDispatcher(vistaSeleccionada).forward(request, response);
		}
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("id"));
		dao.delete(id);
		vistaSeleccionada = VIEW_TABLA;
	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mensaje = "";

		if (pId == null || pId.matches("^\\d+$") == false) {
			mensaje = "El ID solo puede ser un número entero";
		} else if (pNombre == "") {
			mensaje = "El nombre no puede ser vacío";
		} else if (Float.parseFloat(pPrecio) < 0) {
			mensaje = "El precio no puede ser mnegativo";
		} else if (Pattern.matches("http(|s):.*/.(jpg|png|jpeg|gif)", pImagen) == false) {
			mensaje = "Debe introducirse una URL valida";
		} else if (pDescripcion == "") {
			mensaje = "Debe incluirse una descripción del producto";
		} else if (Integer.parseInt(pDescuento) < 0 || Integer.parseInt(pDescuento) > 100) {
			mensaje = "El descuento debe estar entre 0 y 100";
		} else {

			int id = Integer.parseInt(request.getParameter("id"));
			String nombre = request.getParameter("nombre");
			float precio = Float.parseFloat(request.getParameter("precio"));
			String imagen = request.getParameter("imagen");
			String descripcion = request.getParameter("descripcion");
			int descuento = Integer.parseInt(request.getParameter("descuento"));

			Producto pojo = null;
			List<Producto> listado = dao.getAll();
			if (id == 0) {
				pojo = new Producto(nombre, precio, imagen, descripcion, descuento);
				dao.create(pojo);
			} else {
				for (Producto producto : listado) {
					if (producto.getId() == id) {
						producto.setNombre(nombre);
						producto.setImagen(imagen);
						producto.setDescripcion(descripcion);
						producto.setDescuento(descuento);
						producto.setPrecio(precio);
					}
				}
			}

			request.setAttribute("productos", dao.getAll());
			vistaSeleccionada = VIEW_TABLA;
		}
		if (mensaje != "") {
			request.setAttribute("mensaje", mensaje);
			vistaSeleccionada = VIEW_FORM;
		}
	}

	private void irFormulario(HttpServletRequest request, HttpServletResponse response) {
		Producto productoForm = null;
		if (pId != null) {
			productoForm = dao.getById(Integer.parseInt(pId));
		}

		if (productoForm == null) {
			productoForm = new Producto();
		}
		request.setAttribute("producto", productoForm);
		vistaSeleccionada = VIEW_FORM;
	}

	private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("productos", dao.getAll());
		vistaSeleccionada = VIEW_TABLA;
	}

}
