package com.ipartek.formacion.supermercado.modelo;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ipartek.formacion.supermercado.controller.mipanel.ProductosController;

public class ConnectionManager {
	
	private final static Logger LOG = LogManager.getLogger(ProductosController.class);

	private static Connection conn;

	public static Connection getConnection() {

		conn = null;

		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mydb");

			if (ds == null) {
				throw new Exception("Data source no encontrado!");
			}

			conn = ds.getConnection();

		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
		}

		return conn;

	}
	
}