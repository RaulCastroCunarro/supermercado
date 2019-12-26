package com.ipartek.formacion.supermercado.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.ipartek.formacion.supermercado.modelo.ConnectionManager;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;
//import com.ipartek.formacion.supermercado.modelo.pojo.Rol;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;

public class UsuarioDAO implements IUsuarioDAO {

	private final static Logger LOG = Logger.getLogger(UsuarioDAO.class);
	
	private static UsuarioDAO INSTANCE = null;

	private static final String SQL_GET_ALL = "SELECT id, nombre, contrasenia, email, imagen, fecha_creacion, fecha_eliminacion FROM usuario WHERE fecha_eliminacion IS NULL ORDER BY id DESC LIMIT 500;";
	private static final String SQL_GET_BY_ID = "SELECT id, nombre, contrasenia, email, imagen, fecha_creacion, fecha_eliminacion FROM usuario WHERE id = ?;";
	private static final String SQL_GET_ALL_BY_NOMBRE = "SELECT id, nombre, contrasenia, email, imagen, fecha_creacion, fecha_eliminacion FROM usuario WHERE nombre LIKE ? ORDER BY nombre ASC LIMIT 500;";
	private static final String SQL_EXISTE = " SELECT id, nombre, contrasenia, email, imagen, fecha_creacion, fecha_eliminacion FROM usuario as u WHERE nombre = ? AND contrasenia = ? AND fecha_eliminacion IS NULL;";
	private static final String SQL_INSERT = "INSERT INTO usuario (nombre, contrasenia, email, imagen, fecha_creacion) VALUES ( ? , ?, ?, ?, CURRENT_TIMESTAMP());";
	private static final String SQL_UPDATE = "UPDATE usuario SET nombre= ?, contrasenia= ?, imagen=?, email=? WHERE id = ?;";
	private static final String SQL_DELETE = "DELETE FROM usuario WHERE id = ?;";
	private static final String SQL_DELETE_LOGICO = "UPDATE usuario SET fecha_eliminacion = CURRENT_TIMESTAMP() WHERE id = ?;";

	private UsuarioDAO() {
		super();
	}

	public static synchronized UsuarioDAO getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new UsuarioDAO();
		}

		return INSTANCE;

	}
	
	private Usuario mapper(ResultSet rs) throws SQLException {

		Usuario resultado = new Usuario();
		resultado.setId(rs.getInt("id"));
		resultado.setNombre(rs.getString("nombre"));
		resultado.setContrasenia(rs.getString("contrasenia"));
		resultado.setEmail(rs.getString("email"));
		resultado.setImagen(rs.getString("imagen"));
		resultado.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
		resultado.setFechaEliminacion(rs.getTimestamp("fecha_eliminacion"));

		/*
		 * Rol rol = new Rol(); rol.setId( rs.getInt("id_rol")); rol.setNombre(
		 * rs.getString("nombre_rol")); u.setRol(rol);
		 */

		return resultado;
	}

	/**
	 * Compruab si existe el usuario en la base datos, lo busca por su nombre y
	 * conetrseña
	 * 
	 * @param nombre
	 * @param contrasenia
	 * @return Usuario con datos si existe, null en caso de no existir
	 */
	
	@Override
	public Usuario exist(String nombre, String contrasenia) {
		Usuario usuario = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_EXISTE);) {

			// sustituir ? por parametros
			pst.setString(1, nombre);
			pst.setString(2, contrasenia);
			LOG.debug(pst);

			// ejecutar sentencia SQL y obtener Resultado
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					usuario = mapper(rs);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usuario;
	}
	
	public ArrayList<Usuario> getAll() {

		ArrayList<Usuario> lista = new ArrayList<Usuario>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {
				lista.add(mapper(rs));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lista;
	}

	public ArrayList<Usuario> getAllByNombre(String nombre) {
		ArrayList<Usuario> lista = new ArrayList<Usuario>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_BY_NOMBRE);) {

			pst.setString(1, "%" + nombre + "%");

			try (ResultSet rs = pst.executeQuery()) {

				while (rs.next()) {
					lista.add(mapper(rs));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lista;
	}

	public Usuario getById(int id) {
		Usuario resul = new Usuario();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID)) {

			pst.setInt(1, id);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					resul = mapper(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resul;
	}

	@Override
	public Usuario delete(int id) {
		Usuario resultado = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE_LOGICO);) {

			pst.setInt(1, id);

			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				resultado = getById(id);
			}

		} catch (Exception e) {
			LOG.error(e);
		}

		return resultado;
	}
	
	public Usuario deleteFinal(int id) {
		Usuario resultado = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE);) {

			pst.setInt(1, id);

			Usuario usuarioBorrar = getById(id);

			int affetedRows = pst.executeUpdate();
			if (affetedRows == 1) {
				resultado = usuarioBorrar;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}

	@Override
	public Usuario update(int id, Usuario pojo) throws Exception {
		Usuario resultado = null;
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {
			
			pojo = sanitizar(pojo);

			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getContrasenia());
			pst.setString(3, pojo.getEmail());
			pst.setString(4, pojo.getImagen());
			pst.setInt(5, pojo.getId());

			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				resultado = getById(id);
			}
		}
		return resultado;
	}

	@Override
	public Usuario create(Usuario pojo) throws Exception {
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

			pojo = sanitizar(pojo);
			
			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getContrasenia());
			pst.setString(3, pojo.getEmail());
			pst.setString(4, pojo.getImagen());

			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				// conseguimos el ID que acabamos de crear
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					pojo.setId(rs.getInt(1));
				}

			}

		}

		return pojo;
	}

	private Usuario sanitizar(Usuario pojo) {
		Usuario resultado = pojo;

		resultado.setNombre(Jsoup.clean(pojo.getNombre(), Whitelist.none()));
		resultado.setEmail(Jsoup.clean(pojo.getEmail(), Whitelist.none()));
		resultado.setImagen(Jsoup.clean(pojo.getImagen(), Whitelist.none()));

		return resultado;
	}
	
}