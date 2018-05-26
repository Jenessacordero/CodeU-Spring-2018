<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

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
        <li>Users: __</li>
        <li>Conversations: __</li>
        <li>Messages: __</li>
        <li>Most active user: __</li>
        <li>Wordiest user: __</li>
    </ul>
    <hr/>
    <h2>Import Data</h2>
    <hr/>

  </div>
</body>
</html>
