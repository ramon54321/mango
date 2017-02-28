package mango.utilities;
import java.sql.*;
import mango.dto.*;
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
}
