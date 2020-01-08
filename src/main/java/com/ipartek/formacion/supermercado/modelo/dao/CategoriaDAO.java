package com.ipartek.formacion.supermercado.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.supermercado.modelo.ConnectionManager;
import com.ipartek.formacion.supermercado.modelo.pojo.Categoria;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;

public class CategoriaDAO implements ICategoriaDAO {

	private final static Logger LOG = Logger.getLogger(CategoriaDAO.class);

	private static CategoriaDAO INSTANCE;

	private CategoriaDAO() {
		super();
	}

	public static synchronized CategoriaDAO getInstance() {
		LOG.debug("Entra en getInstance");

		if (INSTANCE == null) {
			INSTANCE = new CategoriaDAO();
			LOG.debug("Crea una nueva Instancia");
		}

		return INSTANCE;
	}

	@Override
	public List<Categoria> getAll() {
		LOG.trace("recuperar todas las categorias");
		List<Categoria> resultado = new ArrayList<Categoria>();
		
		try (Connection con = ConnectionManager.getConnection();
				CallableStatement cs = con.prepareCall("{CALL pa_categoria_getall();}")) {

			LOG.debug("Ejecuta la query: " + cs.toString());
			
			try(ResultSet rs = cs.executeQuery()){
				
				//TODO mapper para vosotros
				while (rs.next()) {
					Categoria c = new Categoria();
					c.setId(rs.getInt("id"));
					c.setNombre(rs.getString("nombre"));
					resultado.add(c);
				}
				
				
			}

		} catch (SQLException e) {
			LOG.error(e);
			e.printStackTrace();
		}
		return resultado;
	}

	@Override
	public Categoria getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Categoria delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Categoria update(int id, Categoria pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Categoria create(Categoria pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
