<p>Session ID: <%= session.getId() %></p>

<h2>Sign Up</h2>
<form method="POST" action="/app1/serv/signup">
	<p>Username:</p>
	<input type="text" name="username">
	<p>Password:</p>
	<input type="password" name="password">
	<p>Repeat Password:</p>
	<input type="password" name="password2">
	<p>Email:</p>
	<input type="text" name="email">
	<p>First Name:</p>
	<input type="text" name="firstname">
	<p>Last Name:</p>
	<input type="text" name="lastname">
	<p>Access Code (Not Required):</p>
	<input type="text" name="accesscode">
	<p><button type="submit">Submit</button></p>
</form>