package mango.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import mango.dto.*;
import org.hibernate.Session;
import mango.utilities.*;
import javax.ws.rs.core.*;
import javax.servlet.http.HttpServletRequest;

@Path("users/")
public class Users {

	/**
	 * *
	 * Adds the given user to the database. User password and username has to be at least 5 chars long. The user password will be hashed upon entery to database. returns: boolean of true if a user is signed in.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Status sessionsPost(User user, @Context HttpServletRequest request) {
		// Checks
		if (user.getPassword().length() < 5) {
			return new Status(false, "Password to short");
		}

		if (user.getUsername().length() < 5) {
			return new Status(false, "Username to short");
		}

		user.hashPassword();

		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new Status(false, "Failed to insert user into database");
		}

		WebLog.log("New user created with Username: " + user.getUsername() + " - IP: " + request.getRemoteAddr());

		return new Status(true, "User created");
	}
}
