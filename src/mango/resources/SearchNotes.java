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

@Path("searchnotes/")
public class SearchNotes {

  /***
	 * If the passed json search variable contains a string, it will be used to filter notes that contain said string. Else all notes will be returned in High to Low priority, with date sub ordering.
   * returns: a list of all notes which matches the search criteria.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
  @Path("{search}")
	public Response notesGet(@PathParam("search") String searchHex, @Context HttpServletRequest request){

    String search = DataServices.decode(searchHex);

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

        String[] searchParts = search.split(" ");
        String searchQuery = "";
        for(int i = 0; i < searchParts.length; i++){
          if(i > 0){
            searchQuery += " and";
          }
          searchQuery += " note like '%" + searchParts[i] + "%'";
        }

				hql = "FROM Note WHERE" + searchQuery;
        System.out.println("Search HQL: " + hql);
        query = session.createQuery(hql);
				results.addAll((List<Note>) query.list());

        int orderNumber = 0;
        for(Note n : results){
          n.sortOrder = orderNumber;
          orderNumber++;
        }

				list = new GenericEntity<List<Note>>(results){};
			} else {
				hql = "FROM Note WHERE note like '%#highpriority%' and completed = false order by dateCreated desc";
				query = session.createQuery(hql);
				results.addAll((List<Note>) query.list());

				hql = "FROM Note WHERE note not like '%#highpriority%' and note not like '%#lowpriority%' and completed = false order by dateCreated desc";
				query = session.createQuery(hql);
				results.addAll((List<Note>) query.list());

				hql = "FROM Note WHERE note like '%#lowpriority%' and note not like '%#highpriority%' and completed = false order by dateCreated desc";
				query = session.createQuery(hql);
				results.addAll((List<Note>) query.list());

				hql = "FROM Note WHERE completed = true order by dateCreated desc";
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
}
