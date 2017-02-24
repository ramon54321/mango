package main;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class FilterMember implements Filter  {

	public void  init(FilterConfig config) throws ServletException {

	}

	public void  doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {

		HttpSession session = ((HttpServletRequest)request).getSession();

		if(session.getAttribute("userid") == null){
			((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath() + "/pages/signin.jsp");
			System.out.println("User directed to sign in page due to authentication failure.");
		}

		// Pass request back down the filter chain
		chain.doFilter(request,response);
	}

	public void destroy( ){

	}
}
