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

public class ServletSignUp extends HttpServlet{
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String email = request.getParameter("email");
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String accesscode = request.getParameter("accesscode");

		if(!password.equals(password2))
			return;

		if(password.length() < 5)
			return;

		// Access Code
		int level = 0;
		if(accesscode.equals("manager"))
			level = 1;
		if(accesscode.equals("admin"))
			level = 2;

		DataObject_User newUser = new DataObject_User(username, password, level, email, firstname, lastname);

		// Open session
		Session session = Hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(newUser);
		session.getTransaction().commit();
		session.close();

		WebLog.log("New user created with Username: " + username + " - IP: " + request.getRemoteAddr());

		response.sendRedirect(request.getContextPath() + "/pages/signin.jsp");
	}

}
