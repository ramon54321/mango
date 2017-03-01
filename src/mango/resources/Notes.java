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
import mango.websockets.*;

@Path("notes/")
public class Notes {

  /***
	 * If the passed json search variable contains a string, it will be used to filter notes that contain said string. Else all notes will be returned in High to Low priority, with date sub ordering.
   * returns: a list of all notes which matches the search criteria.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response notesGet(@QueryParam("search") String search, @Context HttpServletRequest request){

    System.out.println("String search : " + search);

		if(DataServices.getSignedInUserId(request) == -1){
			return null;
		}

		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "";
			Query query;
			List<Note> results = new ArrayList();
			GenericEntity<List<Note>> list = null;

			if(search != null){
				hql = "FROM Note WHERE note like '%" + search + "%'";
        query = session.createQuery(hql);
				results.addAll((List<Note>) query.list());

				list = new GenericEntity<List<Note>>(results){};
			} else {
				hql = "FROM Note WHERE note like '%#highpriority%' order by dateCreated desc";
				query = session.createQuery(hql);
				results.addAll((List<Note>) query.list());

				hql = "FROM Note WHERE note not like '%#highpriority%' and note not like '%#lowpriority%' order by dateCreated desc";
				query = session.createQuery(hql);
				results.addAll((List<Note>) query.list());

				hql = "FROM Note WHERE note like '%#lowpriority%' and note not like '%#highpriority%' order by dateCreated desc";
				query = session.createQuery(hql);
				results.addAll((List<Note>) query.list());

        int orderNumber = 0;
        for(Note n : results){
          n.sortOrder = orderNumber;
          orderNumber++;
        }

				list = new GenericEntity<List<Note>>(results){};
			}

			session.getTransaction().commit();
			session.close();

			return Response.ok(list).build();

		} catch (Exception e){
			return null;
		}
	}

  /***
	 * Adds the passed note to the database, with the currently signed in user and the current date.
   * returns: a status of the add.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Status notesPost(Note note, @Context HttpServletRequest request){

		if(DataServices.getSignedInUserId(request) == -1){
			return new Status(false, "No signed in user");
		}

		note.setUser(DataServices.getSignedInUser(request));
		note.setDateCurrent();

		try {
			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(note);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) { e.printStackTrace(); return new Status(false, "Failed to save to database"); }

		WebLog.log("New note created by Username: " + note.getUser().getUsername() + " - Titled: "  + note.getTitle() + " - IP: " + request.getRemoteAddr());

    WebsocketEndpoint.broadcast();

		return new Status(true, "Note created");
	}

  /***
	 * Sets the specified note to completed.
   * returns: the updated note.
	 */
  @PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("complete/{id}")
	public Note notesComplete(@PathParam("id") String idin){

		try {
			int id = Integer.valueOf(String.valueOf(idin));

			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM Note WHERE noteId = :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", id);
			List results = query.list();

			System.out.println("List length: " + results.size());

			Note retrievedNote = (Note) results.get(0);

			retrievedNote.setCompleted(true);
			session.save(retrievedNote);

			session.getTransaction().commit();
			session.close();

      WebsocketEndpoint.broadcast();

			return retrievedNote;

		} catch (Exception e){
			return null;
		}
	}

  /***
	 * Sets the specified note to uncompleted.
   * returns: the updated note.
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("uncomplete/{id}")
	public Note notesUncomplete(@PathParam("id") String idin){

		try {
			int id = Integer.valueOf(String.valueOf(idin));

			Session session = Hibernate.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM Note WHERE noteId = :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", id);
			List results = query.list();

			System.out.println("List length: " + results.size());

			Note retrievedNote = (Note) results.get(0);

			retrievedNote.setCompleted(false);
			session.save(retrievedNote);

			session.getTransaction().commit();
			session.close();

      WebsocketEndpoint.broadcast();

			return retrievedNote;

		} catch (Exception e){
			return null;
		}
	}
}
