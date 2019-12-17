package com.ipartek.formacion.supermercado.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet Filter implementation class SeguridadFilter
 */
@WebFilter(dispatcherTypes = {
				DispatcherType.REQUEST, 
				DispatcherType.FORWARD, 
				DispatcherType.INCLUDE, 
				DispatcherType.ERROR
		}
					, urlPatterns = { "/seguridad/*" })
public class SeguridadFilter implements Filter {
	
	private final static Logger LOG = Logger.getLogger(SeguridadFilter.class);

  
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		LOG.trace("destroy");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		HttpSession session = req.getSession();
		
		if ( session.getAttribute("usuarioLogeado") == null ) {
		
			LOG.warn("Intentan entrar sin logearse");
			LOG.debug("RequestURL " + req.getRequestURL() );
			LOG.debug("RequestURI " + req.getRequestURI() );
			LOG.debug("HTTP Protocol " + req.getProtocol() );
			LOG.debug("HTTP RemoteAddr " + req.getRemoteAddr() );
			LOG.debug("HTTP RemoteHost " + req.getRemoteHost() );
			LOG.debug("navegador " + req.getHeader("User-Agent") );
			String base =  req.getContextPath();
			res.sendRedirect(base + "/inicio");
			
		}else {
			// dejamos continuar
			// pass the request along the filter chain
			LOG.trace("logeado con exito");
			chain.doFilter(request, response);
		}
		
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		LOG.trace("init");
	}

}