package main;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import java.sql.*;
import main.dto.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import java.security.MessageDigest;
import main.dto.connections.*;
import java.util.List;
import org.hibernate.Query;
import main.utilities.*;

public class ServletSignIn extends HttpServlet{

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		HttpSession session = request.getSession();

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		UserSessionPOD user = DataServices.validateCredentials(username, password);

		WebLog.log("Sign In Attempt - Username: " + username + " - Valid: " + user.successfulLogin + " - IP: " + request.getRemoteAddr());

		if(user.successfulLogin){
			System.out.println("Sign in valid. User will be given session.");
			session.setAttribute("userid", user.user.getUserId());
			session.setAttribute("username", user.user.getUsername());
			session.setAttribute("firstname", user.user.getFirstname());
			session.setAttribute("lastname", user.user.getLastname());
			session.setAttribute("email", user.user.getEmail());
			session.setAttribute("level", user.user.getLevel());
			response.sendRedirect(request.getContextPath() + "/pages/member/wall.jsp");
		} else {
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/pages/signin.jsp");
		}
	}
}
