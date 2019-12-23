package com.ipartek.formacion.supermercado.controller;

public class Alerta {
	
	public static final String TIPO_PRIMARY = "primary";
	public static final String TIPO_DANGER = "danger";
	public static final String TIPO_WARNING = "warning";
	
	private String mensaje;
	private String tipo;
	
	public Alerta() {
		super();
		this.mensaje = "ERROR inesperado de la aplicación";
		this.tipo = TIPO_DANGER;
	}

	public Alerta(String mensaje, String tipo) {
		super();
		this.mensaje = mensaje;
		this.tipo = tipo;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
