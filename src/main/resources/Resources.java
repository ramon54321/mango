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
	public String userSignIn(@FormParam("username") String username, @FormParam("password") String password, @Context HttpServletRequest request, @Context HttpServletResponse response){

		UserSessionPOD user = DataServices.validateCredentials(username, password);

		HttpSession session = request.getSession();

		WebLog.log("Sign In Attempt - Username: " + username + " - Valid: " + user.successfulLogin + " - IP: " + request.getRemoteAddr());

		try {
			if(user.successfulLogin){
				System.out.println("Sign in valid. User will be given session.");
				session.setAttribute("userid", user.user.getUserId());
				session.setAttribute("username", user.user.getUsername());
				session.setAttribute("firstname", user.user.getFirstname());
				session.setAttribute("lastname", user.user.getLastname());
				session.setAttribute("email", user.user.getEmail());
				session.setAttribute("level", user.user.getLevel());
				return "success";
			} else {
				session.invalidate();
				return "failed";
			}
		} catch (Exception e){ e.printStackTrace(); return "failed"; }
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("users/signup")
	public String signup(@FormParam("username") String username, @FormParam("password") String password,
											@FormParam("email") String email, @FormParam("firstname") String firstname, @FormParam("lastname") String lastname,
											@FormParam("accesscode") String accesscode, @Context HttpServletRequest request, @Context HttpServletResponse response){

		// Checks
		if(password.length() < 5)
			return "password_to_short";

		if(username.length() < 5)
			return "username_to_short";

		// Access Code
		int level = 0;
		if(accesscode.equals("manager"))
			level = 1;
		if(accesscode.equals("admin"))
			level = 2;

		DataObject_User newUser = new DataObject_User(username, password, level, email, firstname, lastname);

		// Open session
		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(newUser);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) { e.printStackTrace(); return "failed"; }

		WebLog.log("New user created with Username: " + username + " - IP: " + request.getRemoteAddr());

		return "success";
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

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("notes/addnote")
	public String addNote(@FormParam("username") String username, @FormParam("title") String title, @FormParam("note") String note, @Context HttpServletRequest request, @Context HttpServletResponse response){

		// Checks
		if(title.length() < 5)
			return "title_to_short";

		if(note.length() < 5)
			return "note_to_short";

		DataObject_Note newNote = new DataObject_Note(username, title, note);

		// Open session
		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(newNote);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) { e.printStackTrace(); return "failed"; }

		WebLog.log("New note created by Username: " + username + " - Titled: "  + title + " - IP: " + request.getRemoteAddr());

		return "success";
	}

}
