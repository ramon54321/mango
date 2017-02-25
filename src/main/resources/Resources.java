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
	public User user_username(@PathParam("username") String username){

		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM User WHERE username = :username";
			Query query = session.createQuery(hql);
			query.setParameter("username", username);
			List results = query.list();

			System.out.println("List length: " + results.size());
			session.getTransaction().commit();
			session.close();

			User retrievedUser = (User) results.get(0);

			return retrievedUser;

		} catch (Exception e){
			return null;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("users/userid/{id}")
	public User user_userid(@PathParam("id") String idin){

		try {
			int id = Integer.valueOf(String.valueOf(idin));

			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM User WHERE userId = :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", id);
			List results = query.list();

			System.out.println("List length: " + results.size());
			session.getTransaction().commit();
			session.close();

			User retrievedUser = (User) results.get(0);

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

			String hql = "FROM User";
			Query query = session.createQuery(hql);
			List<User> results = (List<User>) query.list();
			GenericEntity<List<User>> list = new GenericEntity<List<User>>(results){};

			session.getTransaction().commit();
			session.close();

			return Response.ok(list).build();

		} catch (Exception e){
			return null;
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("users/signin")
	public Status userSignIn(SignIn signIn, @Context HttpServletRequest request){

		UserSessionPOD user = DataServices.validateCredentials(signIn.getUsername(), signIn.getPassword());

		HttpSession session = request.getSession();

		WebLog.log("Sign In Attempt - Username: " + signIn.getUsername() + " - Valid: " + user.successfulLogin + " - IP: " + request.getRemoteAddr());

		try {
			if(user.successfulLogin){
				System.out.println("Sign in valid. User will be given session.");
				session.setAttribute("userid", user.user.getUserId());
				session.setAttribute("username", user.user.getUsername());
				session.setAttribute("firstname", user.user.getFirstname());
				session.setAttribute("lastname", user.user.getLastname());
				session.setAttribute("email", user.user.getEmail());
				session.setAttribute("level", user.user.getLevel());
				return new Status(true, "User logged in");
			} else {
				session.invalidate();
				return new Status(false, "Incorrect details, session invalidated");
			}
		} catch (Exception e){ e.printStackTrace(); return new Status(false, "Database query failed"); }
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("users/signup")
	public Status signup(User user, @Context HttpServletRequest request){

		// Checks
		if(user.getPassword().length() < 5)
			return new Status(false, "Password to short");

		if(user.getUsername().length() < 5)
			return new Status(false, "Username to short");

		// Need to create local object in order to run constructor which hashes the password
		User newUser = new User(user.getUsername(), user.getPassword(), user.getLevel(), user.getEmail(), user.getFirstname(), user.getLastname());

		// Open session
		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(newUser);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) { e.printStackTrace(); return new Status(false, "Failed to insert user into databse"); }

		WebLog.log("New user created with Username: " + user.getUsername() + " - IP: " + request.getRemoteAddr());

		return new Status(true, "User created");
	}

	// Session ---------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("session/issignedin")
	public Status signup(@Context HttpServletRequest request){

		HttpSession httpSession = request.getSession();
		Object userId = httpSession.getAttribute("userid");

		if(userId == null){
			return new Status(false, "Userid attribute null");
		} else {
			return new Status(true, "Userid attribute found: " + ((int) userId));
		}
	}

	// Notes -----------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("notes")
	public Response notes(){
		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM Note";
			Query query = session.createQuery(hql);
			List<Note> results = (List<Note>) query.list();
			GenericEntity<List<Note>> list = new GenericEntity<List<Note>>(results){};

			session.getTransaction().commit();
			session.close();

			return Response.ok(list).build();

		} catch (Exception e){
			return null;
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("notes/addnote")
	public Status addNote(Note note, @Context HttpServletRequest request){
		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(note);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) { e.printStackTrace(); return new Status(false, "Failed to save to database"); }

		WebLog.log("New note created by Username: " + note.getCreatorUsername() + " - Titled: "  + note.getTitle() + " - IP: " + request.getRemoteAddr());

		return new Status(true, "Note created");
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("notes/activate/{id}")
	public Note setNoteActive(@PathParam("id") String idin){

		try {
			int id = Integer.valueOf(String.valueOf(idin));

			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM Note WHERE noteId = :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", id);
			List results = query.list();

			System.out.println("List length: " + results.size());
			session.getTransaction().commit();
			session.close();

			Note retrievedNote = (Note) results.get(0);

			retrievedNote.setActive(true);

			return retrievedNote;

		} catch (Exception e){
			return null;
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("notes/deactivate/{id}")
	public Note setNoteDeactive(@PathParam("id") String idin){

		try {
			int id = Integer.valueOf(String.valueOf(idin));

			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM Note WHERE noteId = :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", id);
			List results = query.list();

			System.out.println("List length: " + results.size());
			session.getTransaction().commit();
			session.close();

			Note retrievedNote = (Note) results.get(0);

			retrievedNote.setActive(false);

			return retrievedNote;

		} catch (Exception e){
			return null;
		}
	}

}
