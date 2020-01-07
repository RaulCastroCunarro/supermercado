package com.ipartek.formacion.supermercado.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.ipartek.formacion.supermercado.modelo.ConnectionManager;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;

public class ProductoDAO implements IProductoDAO {
	private final static Logger LOG = Logger.getLogger(ProductoDAO.class);

	private static ProductoDAO INSTANCE;

	private static final String SQL_GET_ALL = "SELECT " + 
			"		p.id AS 'id_producto', " + 
			"		p.nombre AS 'nombre_producto', " + 
			"		p.imagen AS 'imagen_producto', " + 
			"		p.precio, " + 
			"		p.descripcion, " + 
			"		p.descuento, " + 
			"		p.fecha_creacion AS 'fecha_creacion_producto', " + 
			"		p.fecha_modificacion AS 'fecha_modificacion_producto', " + 
			"		p.fecha_eliminacion AS 'fecha_eliminacion_producto', " + 
			"		u.id AS 'id_usuario', " + 
			"		u.nombre 'nombre_usuario', " + 
			"		u.contrasenia, " + 
			"		u.email AS 'email', " + 
			"		u.imagen AS 'imagen_usuario', " + 
			"		u.fecha_creacion AS 'fecha_creacion_usuario', " + 
			"		u.fecha_modificacion AS 'fecha_modificacion_usuario', " + 
			"		u.fecha_eliminacion AS 'fecha_eliminacion_usuario' " + 
			"FROM producto p INNER JOIN usuario u " + 
			"WHERE p.fecha_eliminacion IS NULL AND p.id_usuario = u.id " + 
			" ORDER BY p.id DESC LIMIT 500;";
	private static final String SQL_GET_ALL_BY_USER = "SELECT " + 
			"		p.id AS 'id_producto', " + 
			"		p.nombre AS 'nombre_producto', " + 
			"		p.imagen AS 'imagen_producto', " + 
			"		p.precio, " + 
			"		p.descripcion, " + 
			"		p.descuento, " + 
			"		p.fecha_creacion AS 'fecha_creacion_producto', " + 
			"		p.fecha_modificacion AS 'fecha_modificacion_producto', " + 
			"		p.fecha_eliminacion AS 'fecha_eliminacion_producto', " + 
			"		u.id AS 'id_usuario', " + 
			"		u.nombre 'nombre_usuario', " + 
			"		u.contrasenia, " + 
			"		u.email AS 'email', " + 
			"		u.imagen AS 'imagen_usuario', " + 
			"		u.fecha_creacion AS 'fecha_creacion_usuario', " + 
			"		u.fecha_modificacion AS 'fecha_modificacion_usuario', " + 
			"		u.fecha_eliminacion AS 'fecha_eliminacion_usuario' " + 
			"FROM producto p INNER JOIN usuario u " + 
			"WHERE p.fecha_eliminacion IS NULL AND p.id_usuario = u.id AND p.id_usuario = ?" + 
			" ORDER BY p.id DESC LIMIT 500;";
	private static final String SQL_GET_BY_ID = "SELECT " + 
			"		p.id AS 'id_producto', " + 
			"		p.nombre AS 'nombre_producto', " + 
			"		p.imagen AS 'imagen_producto', " + 
			"		p.precio, " + 
			"		p.descripcion, " + 
			"		p.descuento, " + 
			"		p.fecha_creacion AS 'fecha_creacion_producto', " + 
			"		p.fecha_modificacion AS 'fecha_modificacion_producto', " + 
			"		p.fecha_eliminacion AS 'fecha_eliminacion_producto', " + 
			"		u.id AS 'id_usuario'," + 
			"		u.nombre AS 'nombre_usuario', " + 
			"		u.contrasenia, " + 
			"		u.email, " + 
			"		u.imagen AS 'imagen_usuario', " + 
			"		u.fecha_creacion AS 'fecha_creacion_usuario'," + 
			"		u.fecha_modificacion AS 'fecha_modificacion_usuario', " + 
			"		u.fecha_eliminacion AS 'fecha_eliminacion_usuario' " + 
			"FROM producto p, usuario u " + 
			"WHERE p.fecha_eliminacion IS NULL AND p.id_usuario = u.id AND p.id = ? " + 
			"ORDER BY p.id DESC LIMIT 500;";
	private static final String SQL_GET_BY_ID_BY_USER = "SELECT " + 
			"		p.id AS 'id_producto', " + 
			"		p.nombre AS 'nombre_producto', " + 
			"		p.imagen AS 'imagen_producto', " + 
			"		p.precio, " + 
			"		p.descripcion, " + 
			"		p.descuento, " + 
			"		p.fecha_creacion AS 'fecha_creacion_producto', " + 
			"		p.fecha_modificacion AS 'fecha_modificacion_producto', " + 
			"		p.fecha_eliminacion AS 'fecha_eliminacion_producto', " + 
			"		u.id AS 'id_usuario'," + 
			"		u.nombre AS 'nombre_usuario', " + 
			"		u.contrasenia, " + 
			"		u.email, " + 
			"		u.imagen AS 'imagen_usuario', " + 
			"		u.fecha_creacion AS 'fecha_creacion_usuario'," + 
			"		u.fecha_modificacion AS 'fecha_modificacion_usuario', " + 
			"		u.fecha_eliminacion AS 'fecha_eliminacion_usuario' " + 
			"FROM producto p, usuario u " + 
			"WHERE p.fecha_eliminacion IS NULL AND p.id_usuario = u.id AND p.id = ? AND u.id = ?" + 
			"ORDER BY p.id DESC LIMIT 500;";
	private static final String SQL_INSERT = "INSERT INTO producto (id, nombre, imagen, precio, descuento, descripcion, fecha_creacion, id_usuario) VALUES ( ? , ?, ?, ?, ?, ?, CURRENT_TIMESTAMP(), ?);";
	private static final String SQL_UPDATE = "UPDATE producto SET nombre= ?, imagen=?, precio=?, descuento=?, descripcion=?, fecha_modificacion=CURRENT_TIMESTAMP(), id_usuario=? WHERE id = ?;";
	private static final String SQL_UPDATE_BY_USER = "UPDATE producto SET nombre= ?, imagen=?, precio=?, descuento=?, descripcion=?, fecha_modificacion=CURRENT_TIMESTAMP(), id_usuario=? WHERE id = ? AND id_usuario = ?;";
	private static final String SQL_DELETE = "DELETE FROM producto WHERE id = ?;";
	private static final String SQL_DELETE_BY_USER = "DELETE FROM producto WHERE id = ? AND id_producto = ?;";
	private static final String SQL_DELETE_LOGICO = "UPDATE producto SET fecha_eliminacion = CURRENT_TIMESTAMP() WHERE id = ?;";
	private static final String SQL_DELETE_LOGICO_BY_USER = "UPDATE producto SET fecha_eliminacion = CURRENT_TIMESTAMP() WHERE id = ? AND id_usuario=?;";
	
	private ProductoDAO() {
		super();
	}

	public static synchronized ProductoDAO getInstance() {
		LOG.debug("Entra en getInstance");

		if (INSTANCE == null) {
			INSTANCE = new ProductoDAO();
			LOG.debug("Crea una nueva Instancia");
		}

		return INSTANCE;
	}
	
	private Producto mapper(ResultSet rs) throws SQLException {
		
		LOG.debug("Entra en el mapper");

		Producto p = new Producto();
		Usuario u = p.getUsuario();
		
		p.setId(rs.getInt("id_producto"));
		p.setNombre(rs.getString("nombre_producto"));
		p.setImagen(rs.getString("imagen_producto"));
		p.setPrecio(rs.getFloat("precio"));
		p.setDescuento(rs.getInt("descuento"));
		p.setDescripcion(rs.getString("descripcion"));
		p.setFechaCreacion(rs.getTimestamp("fecha_creacion_producto"));
		p.setFechaModificacion(rs.getTimestamp("fecha_modificacion_producto"));
		p.setFechaEliminacion(rs.getTimestamp("fecha_eliminacion_producto"));
		u.setId(rs.getInt("id_usuario"));
		u.setNombre(rs.getString("nombre_usuario"));
		u.setContrasenia(rs.getString("contrasenia"));
		u.setEmail(rs.getString("email"));
		u.setImagen(rs.getString("imagen_usuario"));
		u.setFechaCreacion(rs.getTimestamp("fecha_creacion_usuario"));
		u.setFechaModificacion(rs.getTimestamp("fecha_modificacion_usuario"));
		u.setFechaEliminacion(rs.getTimestamp("fecha_eliminacion_usuario"));
		p.setUsuario(u);

		p = sanitizar(p);
		
		return p;
	}

	private Producto sanitizar(Producto pojo) {
		
		LOG.debug("Entra en sanitizar");
		
		Producto resultado = pojo;
		Usuario usuario = pojo.getUsuario();

		resultado.setNombre(Jsoup.clean(pojo.getNombre(), Whitelist.none()));
		resultado.setImagen(Jsoup.clean(pojo.getImagen(), Whitelist.none()));
		resultado.setDescripcion(Jsoup.clean(pojo.getDescripcion(), Whitelist.none()));
		usuario.setNombre(Jsoup.clean(pojo.getUsuario().getNombre(), Whitelist.none()));
		usuario.setImagen(Jsoup.clean(pojo.getUsuario().getImagen(), Whitelist.none()));
		usuario.setEmail(Jsoup.clean(pojo.getUsuario().getEmail(), Whitelist.none()));
		resultado.setUsuario(usuario);

		return resultado;
	}

	public List<Producto> getAllByUser(int id) {
		
		LOG.debug("Entra en getAllByUser");

		ArrayList<Producto> lista = new ArrayList<Producto>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL_BY_USER)){
				
				pst.setInt(1, id);
				
				LOG.debug("Ejecuta la query: " + pst.toString());
				ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				Producto p = mapper(rs);
				lista.add(p);

			}

		} catch (SQLException e) {
			LOG.error(e);
			e.printStackTrace();
		}

		return lista;
	}
	
	@Override
	public List<Producto> getAll() {
		
		LOG.debug("Entra en getAll");

		ArrayList<Producto> lista = new ArrayList<Producto>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()) {
			
			LOG.debug("Ejecuta la query: " + pst.toString());

			while (rs.next()) {

				Producto p = mapper(rs);
				lista.add(p);

			}

		} catch (SQLException e) {
			LOG.error(e);
			e.printStackTrace();
		}

		return lista;
	}

	@Override
	public Producto getById(int id) {
		
		LOG.debug("Entra en getbyId");
		
		Producto resul = new Producto();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID)) {

			pst.setInt(1, id);

			LOG.debug("Ejecuta la query: " + pst.toString());
			
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					resul = mapper(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		}
		return resul;
	}
	
	@Override
	public Producto getByIdByUser(int idProducto, int idUsuario) throws ProductoException {
		
		LOG.debug("Entra en getbyIdByUser");
		
		Producto resul = new Producto();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID_BY_USER)) {

			pst.setInt(1, idProducto);
			pst.setInt(2, idUsuario);

			LOG.debug("Ejecuta la query: " + pst.toString());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					resul = mapper(rs);
				}else {
					throw new ProductoException(ProductoException.EXCEPTION_UNAUTORIZED);
				}
			}
		} catch (ProductoException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		}
		return resul;
	}

	@Override
	public Producto delete(int id) throws Exception {
		
		LOG.debug("Entra en delete");
		
		Producto resultado = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE_LOGICO);) {

			pst.setInt(1, id);

			LOG.debug("Ejecuta la query: " + pst.toString());
			
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				resultado = getById(id);
			}else {
				LOG.fatal("El update esta mal, ha afectado a mas de un producto");
			}

		} catch (Exception e) {
			LOG.error(e);
		}

		return resultado;
	}

	@Override
	public Producto deleteByUser(int idProducto, int idUsuario) throws ProductoException {
		
		LOG.debug("Entra en deleteByUser");
		
		Producto resultado = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE_LOGICO_BY_USER);) {

			pst.setInt(1, idProducto);
			pst.setInt(2, idUsuario);
			
			LOG.debug("Ejecuta la query: " + pst.toString());

			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				resultado = getById(idProducto);
			}else if(affectedRows == 0){
				throw new ProductoException(ProductoException.EXCEPTION_UNAUTORIZED);
			}else {
				LOG.fatal("El update esta mal, ha afectado a mas de un producto");
			}

		} catch (ProductoException e) {
			throw e;
		} catch (Exception e) {
			LOG.error(e);
		}

		return resultado;
		//throw new ProductoException(ProductoException.EXCEPTION_UNAUTORIZED);
	}
	
	public Producto deleteFinal(int id) throws Exception {
		
		LOG.debug("Entra en deleteFinal");
		
		Producto resultado = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE);) {

			pst.setInt(1, id);

			Producto productoBorrar = getById(id);
			
			LOG.debug("Ejecuta la query: " + pst.toString());

			int affetedRows = pst.executeUpdate();
			if (affetedRows == 1) {
				resultado = productoBorrar;
			}else {
				LOG.fatal("El delete esta mal, ha eliminado mas de un producto");
			}

		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}

		return resultado;
	}
	
	public Producto deleteFinalByUser(int idProducto, int idUsuario) throws Exception {
		
		LOG.debug("Entra en deleteFinalByUser");
		
		Producto resultado = null;

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_DELETE_BY_USER);) {

			pst.setInt(1, idProducto);
			pst.setInt(2, idUsuario);

			Producto productoBorrar = getById(idProducto);
			
			LOG.debug("Ejecuta la query: " + pst.toString());

			int affetedRows = pst.executeUpdate();
			if (affetedRows == 1) {
				resultado = productoBorrar;
			}else {
				LOG.fatal("El delete esta mal, ha eliminado mas de un producto");
			}

		} catch (Exception e) {
			LOG.error(e);
			throw e;
		}

		return resultado;
	}

	@Override
	public Producto update(int id, Producto pojo) throws Exception {
		
		LOG.debug("Entra en update");
		
		Producto resultado = null;
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE)) {
			
			pojo = sanitizar(pojo);

			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getImagen());
			pst.setFloat(3, pojo.getPrecio());
			pst.setInt(4, pojo.getDescuento());
			pst.setString(5, pojo.getDescripcion());
			pst.setInt(6, pojo.getUsuario().getId());
			pst.setInt(7, id);

			LOG.debug("Ejecuta la query: " + pst.toString());
			
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				resultado = getById(id);
			}else {
				LOG.fatal("El update esta mal, ha afectado a mas de un producto");
			}
		}
		return resultado;
	}
	
	@Override
	public Producto updateByUser(int idProducto, int idUsuario, Producto pojo) throws ProductoException, SQLException {
		
		LOG.debug("Entra en updateByUser");

		Producto resultado = null;
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_UPDATE_BY_USER)) {
			
			pojo = sanitizar(pojo);

			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getImagen());
			pst.setFloat(3, pojo.getPrecio());
			pst.setInt(4, pojo.getDescuento());
			pst.setString(5, pojo.getDescripcion());
			pst.setInt(6, pojo.getUsuario().getId());
			pst.setInt(7, idProducto);
			pst.setInt(8, idUsuario);

			LOG.debug("Ejecuta la query: " + pst.toString());
			
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				resultado = getById(idProducto);
			}else {
				LOG.fatal("El update esta mal, ha afectado a mas de un producto");
			}
		}
		
		return resultado;
	}

	@Override
	public Producto create(Producto pojo) throws SQLException {
		
		LOG.debug("Entra en create");
		
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
		
			pojo = sanitizar(pojo);

			pst.setInt(1, pojo.getId());
			pst.setString(2, pojo.getNombre());
			pst.setString(3, pojo.getImagen());
			pst.setFloat(4, pojo.getPrecio());
			pst.setInt(5, pojo.getDescuento());
			pst.setString(6, pojo.getDescripcion());
			pst.setInt(7, pojo.getUsuario().getId());

			LOG.debug("Ejecuta la query: " + pst.toString());
			
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

}