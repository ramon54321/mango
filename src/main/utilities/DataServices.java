package main.utilities;

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

public class DataServices {

  public static boolean validateCredentials(String username, String password) {

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
		String givenPassword = getHash(password);

		System.out.println("Given: " + givenPassword);
		System.out.println("Expected: " + expectedPassword);

		// Compare passwords
		if(givenPassword.equals(expectedPassword)){
			return true;
		} else {
			return false;
		}
	}

  public static String getHash(String pure){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pure.getBytes());
			byte byteData[] = md.digest();

			StringBuffer pureHash = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				pureHash.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			String hash = pureHash.toString();
			return hash;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
