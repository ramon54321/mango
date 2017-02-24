<p>Session ID: <%= session.getId() %></p>

<h2>Add Note</h2>
<form method="POST" action="/mango/serv/addnote">
	<p>Title:</p>
	<input type="text" name="title">
	<p>Note:</p>
	<input type="text" name="note">
	<p><button type="submit">Submit</button></p>
</form>
