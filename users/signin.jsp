<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
		users.signInUser();
	%>

</c:otherwise>
</c:choose>