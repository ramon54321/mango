<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p><%= session.getId() %></p>
<p><%= session.getAttribute("username") %></p>

<%
	if(session.getAttribute("username") == null){
		response.sendRedirect(request.getContextPath() + "/users/signin.jsp");
	}
%>

<h2>Wall</h2>