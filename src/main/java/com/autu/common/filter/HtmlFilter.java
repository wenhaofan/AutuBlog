package com.autu.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class HtmlFilter implements Filter {

 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//HttpServletRequest req=(HttpServletRequest)request;
		
		//String path=req.getServletPath();
		
		//if(path.endsWith(".html")) {
		//	request.getRequestDispatcher("/_view/error/404.html").forward(request, response);
		//	return;
		//} 
		
		chain.doFilter(request, response);
	}

	 
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

 
	public void init(FilterConfig filterConfig) throws ServletException {
	 
	}

}
