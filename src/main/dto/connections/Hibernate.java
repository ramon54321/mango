package main.dto.connections;
import java.sql.*;
import main.dto.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import java.security.MessageDigest;

public class Hibernate {

	public static SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory(){
		if(sessionFactory == null){
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}

		return sessionFactory;
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

	public static void addUser(DataObject_User user){
		Session session = Hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
	}
}
