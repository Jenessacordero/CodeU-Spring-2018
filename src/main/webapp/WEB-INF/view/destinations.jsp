<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Destination" %>
<%@ page import="codeu.model.data.User" %>

<!DOCTYPE html>
<html>
<head>
  <title>Destinations</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/about.jsp">About</a>
    <a href="/activityfeed">Activity Feed</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/user/<%=request.getSession().getAttribute("user") %>">Profile Page</a>
    <% } %>
    <a href="destinations">Destinations</a>
    <% if(request.getSession().getAttribute("user") != null && (request.getSession().getAttribute("user").equals("cavalos99") || 
    		request.getSession().getAttribute("user").equals("jenessacordero") || request.getSession().getAttribute("user").equals("agarwalv"))) {%>
    <a href="/adminpage">Admin</a>
    <% } %>
  </nav>
  
  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>


      <h1>New Destination</h1>
      <form action="/destinations" method="POST">
          <div class="form-group">
            <label class="form-control-label">Title:</label>
          <input type="text" name="destinationTitle">
        </div>

        <button type="submit">Create</button>
      </form>

      <hr/>



    <h1>Destinations</h1>
    <% if(request.getSession().getAttribute("user") == null){ %>
      <p>Login to create a destination!</p>
    <% } %>

    <%
    List<Destination> destinations =
      (List<Destination>) request.getAttribute("destinations");
    if(destinations == null || destinations.isEmpty()){
    %>
      <p>Create a destination to get started.</p>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      for(Destination destination : destinations){
    %>
      <li><a href="/destination/<%= destination.getTitle() %>">
        <%= destination.getTitle() %></a></li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>
  </div>
</body>
</html>