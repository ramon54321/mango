package mango.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import mango.dto.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import java.security.MessageDigest;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.Query;
import mango.utilities.*;
import javax.ws.rs.core.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Path("sessions/")
public class Sessions {

    /***
     * returns: boolean of true if a user is signed in.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Status sessionsGet(@Context HttpServletRequest request){
      int userid = DataServices.getSignedInUserId(request);
      if(userid == -1){
        return new Status(false, "No user signed in");
      } else {
        return new Status(true, "Userid attribute found: " + userid);
      }
    }

    /***
     * Invalidates the session, this in effect signs out the user.
     * returns: status of signout attempt.
     */
    @DELETE
  	@Produces(MediaType.APPLICATION_JSON)
  	public Status sessionsDelete(@Context HttpServletRequest request){

  		HttpSession session = request.getSession();
  		session.invalidate();

  		return new Status(true, "Session invalidated");
  	}

    /***
     * Creates a session if the given data matches a current user in the database.
     * returns: status of signin attempt.
     */
    @PUT
  	@Produces(MediaType.APPLICATION_JSON)
  	@Consumes(MediaType.APPLICATION_JSON)
  	public Status sessionsPut(SignIn signIn, @Context HttpServletRequest request){

  		UserSession user = DataServices.validateCredentials(signIn.getUsername(), signIn.getPassword());

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
}
