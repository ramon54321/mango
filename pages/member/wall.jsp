<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p>Session ID: <%= session.getId() %></p>
<p>Session Username: <%= session.getAttribute("username") %></p>

<h2>Wall</h2>