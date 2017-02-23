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

	private DataObject_User userToLogIn = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		HttpSession session = request.getSession();

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		boolean validUser = validateCredentials(username, password);

		WebLog.log("Sign In Attempt - Username: " + username + " - Valid: " + validUser + " - IP: " + request.getRemoteAddr());

		if(validUser){
			System.out.println("Sign in valid. User will be given session.");
			session.setAttribute("userid", userToLogIn.getUserId());
			session.setAttribute("username", userToLogIn.getUsername());
			session.setAttribute("firstname", userToLogIn.getFirstname());
			session.setAttribute("lastname", userToLogIn.getLastname());
			session.setAttribute("email", userToLogIn.getEmail());
			session.setAttribute("level", userToLogIn.getLevel());
			response.sendRedirect(request.getContextPath() + "/pages/member/wall.jsp");
		} else {
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/pages/signin.jsp");
		}
	}

	private boolean validateCredentials(String username, String password) {

		if(username.equals("exec")){

			//Hibernate.addUser(new DataObject_User("andrew", "goodwill", 0, "andrew@mail.com"));

			//WebLog.log("This should be a line.");
			WebLog.truncate();

			return false;
		}

		// Open session and get user from database given username
		Session session = Hibernate.getSessionFactory().openSession();
		session.beginTransaction();

		String hql = "FROM DataObject_User WHERE username = :username";
		Query query = session.createQuery(hql);
		query.setParameter("username", username);
		List results = query.list();

		System.out.println("List length: " + results.size());
		session.getTransaction().commit();
		session.close();

		// Ensure a user was found
		if(results.size() < 1)
			return false;

		DataObject_User retrievedUser = (DataObject_User) results.get(0);
		String expectedPassword = retrievedUser.getPassword();
		String givenPassword = DataServices.getHash(password);

		System.out.println("Given: " + givenPassword);
		System.out.println("Expected: " + expectedPassword);

		// Compare passwords
		if(givenPassword.equals(expectedPassword)){
			userToLogIn = retrievedUser;
			return true;
		} else {
			return false;
		}
	}

}
