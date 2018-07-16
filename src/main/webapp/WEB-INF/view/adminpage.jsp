<!DOCTYPE html>
<html>
<head>
  <title>Admin Page</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@include file="nav.jsp" %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <h1>Administration</h1>
    <hr/>
    <h2>Site Statistics</h2>
    <p></p>
    <p>Here are some site stats:</p>
    <p></p>
    <ul>
        <li>Users: <%= request.getAttribute("numUsers") %></li>
        <li>Conversations: <%= request.getAttribute("numConversations") %></li>
        <li>Messages: <%= request.getAttribute("numMessages") %></li>
        <li>Newest user: <%= request.getAttribute("newest") %></li>
        <li>Most active user: <%= request.getAttribute("mostActive") %></li>
        <li>Wordiest user: <%= request.getAttribute("wordiest") %></li>
    </ul>
    <hr/>
    <h2>Import Data</h2>
    <hr/>

  </div>
</body>
</html>
