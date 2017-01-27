package main.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import main.dto.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import java.security.MessageDigest;
import main.dto.connections.*;
import java.util.List;
import org.hibernate.Query;
import main.utilities.*;
import javax.ws.rs.core.*;

@Path("")
public class Resources {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("users/{username}")
	public DataObject_User user(@PathParam("username") String username){

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

		DataObject_User retrievedUser = (DataObject_User) results.get(0);

		return retrievedUser;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("users")
	public Response users(){

		// Open session and get user from database given username
		Session session = Hibernate.getSessionFactory().openSession();
		session.beginTransaction();
		
		String hql = "FROM DataObject_User";
		Query query = session.createQuery(hql);
		List<DataObject_User> results = (List<DataObject_User>) query.list();
		GenericEntity<List<DataObject_User>> list = new GenericEntity<List<DataObject_User>>(results){};

		session.getTransaction().commit();
		session.close();

		return Response.ok(list).build();
	}

}