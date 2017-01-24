<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p>Session ID: <%= session.getId() %></p>

<c:if test="${session.getAttribute('username') == null}">

	<h2>Sign In</h2>
	<p>The dev username and password is admin and admin.</p>
	<form method="POST" action="/app1/serv/signin">
		<p>Username:</p>
		<input type="text" name="username">
		<p>Password:</p>
		<input type="password" name="password">
		<p><button type="submit">Submit</button></p>
	</form>

</c:if>