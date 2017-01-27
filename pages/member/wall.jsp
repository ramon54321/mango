<p>Session ID: <%= session.getId() %></p>
<p>Session Username: <%= session.getAttribute("username") %></p>

<h2>Wall</h2>
<button onclick="location.href='addnote.jsp';">Add Note</button>
<button onclick="location.href='editgroup.jsp';" <% if(Integer.valueOf(String.valueOf(session.getAttribute("level"))) < 1) { out.println("disabled"); } %>>Edit Group</button>