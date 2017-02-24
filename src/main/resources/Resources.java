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
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Path("")
public class Resources {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("users/username/{username}")
	public DataObject_User user_username(@PathParam("username") String username){

		try {
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

		} catch (Exception e){
			return null;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("users/userid/{id}")
	public DataObject_User user_userid(@PathParam("id") String idin){

		try {
			int id = Integer.valueOf(String.valueOf(idin));

			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM DataObject_User WHERE userId = :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", id);
			List results = query.list();

			System.out.println("List length: " + results.size());
			session.getTransaction().commit();
			session.close();

			DataObject_User retrievedUser = (DataObject_User) results.get(0);

			return retrievedUser;

		} catch (Exception e){
			return null;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("users")
	public Response users(){

		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM DataObject_User";
			Query query = session.createQuery(hql);
			List<DataObject_User> results = (List<DataObject_User>) query.list();
			GenericEntity<List<DataObject_User>> list = new GenericEntity<List<DataObject_User>>(results){};

			session.getTransaction().commit();
			session.close();

			return Response.ok(list).build();

		} catch (Exception e){
			return null;
		}
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("users/signin")
	public String userSignIn(@HeaderParam("username") String username, @HeaderParam("password") String password, @Context HttpServletRequest request, @Context HttpServletResponse response){

		UserSessionPOD user = DataServices.validateCredentials(username, password);

		HttpSession session = request.getSession();

		WebLog.log("Sign In Attempt - Username: " + username + " - Valid: " + user.successfulLogin + " - IP: " + request.getRemoteAddr());

		try{
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
		} catch (Exception e){
			
		}

		return String.valueOf(user.successfulLogin);
	}

	// Notes -----------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("notes")
	public Response notes(){

		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM DataObject_Note";
			Query query = session.createQuery(hql);
			List<DataObject_Note> results = (List<DataObject_Note>) query.list();
			GenericEntity<List<DataObject_Note>> list = new GenericEntity<List<DataObject_Note>>(results){};

			session.getTransaction().commit();
			session.close();

			return Response.ok(list).build();

		} catch (Exception e){
			return null;
		}
	}

}
