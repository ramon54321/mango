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

public class ServletSignIn extends HttpServlet{
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		
		HttpSession session = request.getSession();

		response.setContentType("text/html");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		boolean validUser = validateCredentials(username, password);

		if(validUser){
			System.out.println("Sign in valid. User will be given session.");
			session.setAttribute("username",username);
			response.sendRedirect(request.getContextPath() + "/pages/member/wall.jsp");
		} else {
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/pages/signin.jsp");
		}
	}

	private boolean validateCredentials(String username, String password) {
/*
		String driver = "com.mysql.jdbc.Driver";
		String db_url = "jdbc:mysql://92.222.90.41:3306/mango";

		String user = "root";
		String pass = "admin";

		try{
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(db_url, user, pass);

			Statement stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM notes";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()){
				String note = rs.getString("note");
				writer.println("<li>" + note + "</li>");
			}

			rs.close();
			stmt.close();
			conn.close();
			
		} catch (Exception e) {e.printStackTrace();}
		*/


		if(username.equals("hibernate")){

			System.out.println("Testing Hibernate");

			DataObject_User myUser = new DataObject_User();
			myUser.setUsername("Jeremy1");
			myUser.setUserId(1);

			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(myUser);
			session.getTransaction().commit();

		}




		if(username.equals("admin")){
			if(password.equals("admin")){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
