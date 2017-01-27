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

public class ServletAddNote extends HttpServlet{
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

		String title = request.getParameter("title");
		String note = request.getParameter("note");

		HttpSession httpSession = request.getSession();
		String username = (String) httpSession.getAttribute("username");

		// Checks
		if(title.length() < 5)
			return;

		if(note.length() < 5)
			return;

		if(username.length < 5)
			return;

		DataObject_Note newNote = new DataObject_Note(username, title, note);

		// Open session
		Session session = Hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(newNote);
		session.getTransaction().commit();
		session.close();

		WebLog.log("New note created by Username: " + username + " - Titled: "  + title + " - IP: " + request.getRemoteAddr());

		response.sendRedirect(request.getContextPath() + "/pages/member/wall.jsp");
	}

}
