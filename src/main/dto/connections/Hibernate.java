package main.dto.connections;
import java.sql.*;
import main.dto.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

public class Hibernate {

	public static SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory(){
		if(sessionFactory == null){
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}

		return sessionFactory;
	}

	public static void addUser(DataObject_User user){
		Session session = Hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
	}
}
