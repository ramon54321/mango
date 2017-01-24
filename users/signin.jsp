<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p><%= session.getId() %></p>

<c:choose>
<c:when test="${param.username == null}">

	<h2>Sign In</h2>
	<form method="POST">
		<p>Username:</p>
		<input type="text" name="username">
		<p>Password:</p>
		<input type="password" name="password">
		<p><button type="submit">Submit</button></p>
	</form>

</c:when>
<c:otherwise>
	
	<p>Username: <c:out value="${param.username}"/></p>
	<p>Password: <c:out value="${param.password}"/></p>

	<jsp:useBean id="users" class="main.Users"/>
	<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		boolean validUser = users.validateCredentials(username, password);

		if(validUser){
			System.out.println("Sign in valid. User will be given session.");
			session.setAttribute("username",username);
			response.sendRedirect(request.getContextPath() + "/pages/wall.jsp");
		} else {
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/users/signin.jsp");
		}
	%>

	<%= validUser %>

</c:otherwise>
</c:choose>