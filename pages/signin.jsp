<p>Session ID: <%= session.getId() %></p>

<h2>Sign In</h2>
<p>The dev username and password is admin and admin.</p>
<form method="POST" action="/serv/signin">
	<p>Username:</p>
	<input type="text" name="username">
	<p>Password:</p>
	<input type="password" name="password">
	<p><button type="submit">Submit</button></p>
</form>
