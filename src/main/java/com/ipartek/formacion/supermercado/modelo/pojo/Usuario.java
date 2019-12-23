package com.ipartek.formacion.supermercado.modelo.pojo;

import java.sql.Timestamp;

public class Usuario {
	private int id;
	private String nombre;
	private String password;
	private String email;
	private String imagen;
	private Timestamp fechaCreacion;
	private Timestamp fechaEliminacion;
	
	public Usuario() {
		super();
	}

	public Usuario(int id, String nombre, String password, String email, String imagen) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.email = email;
		this.imagen = imagen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Timestamp getFechaEliminacion() {
		return fechaEliminacion;
	}

	public void setFechaEliminacion(Timestamp fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", password=" + password + ", email=" + email + ", imagen="
				+ imagen + ", fechaCreacion=" + fechaCreacion + ", fechaEliminacion=" + fechaEliminacion + "]";
	}

	
	
	
}
