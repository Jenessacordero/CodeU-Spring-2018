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
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Image" %>


<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

<nav>
  <a id="navTitle" href="/">CodeU Chat App</a>
  <a href="/about.jsp">About</a>
  <a href="/activityfeed">Activity Feed</a>
  <a href="/destinations">Destinations</a>
  <% if(request.getSession().getAttribute("user") != null && (request.getSession().getAttribute("user").equals("cavalos99") ||
          request.getSession().getAttribute("user").equals("jenessacordero") || request.getSession().getAttribute("user").equals("agarwalv"))) {%>
  <a href="/adminpage">Admin</a>
  <% } %>
  <% if(request.getSession().getAttribute("user") != null){ %>
  <a href="/user/<%=request.getSession().getAttribute("user") %>">Profile Page</a>
  <% } %>
  <% if(request.getSession().getAttribute("user") != null){ %>
  <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
  <% } else{ %>
  <a href="/login">Login</a>
  <% } %>
</nav>
  
  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>


      <h1><%= request.getAttribute("destinationTitle") %></h1>

    <img src="<%=request.getAttribute("banner")%>" height="400" width="800"/>
    <form action="/destination/<%= request.getAttribute("destinationTitle") %>" method="POST">
      <div class="form-group">
        <label class="form-control-label">Upload/Change the Banner Image (Image Address Only): </label>
        <input type="text" name="banner">
      </div>


    <h2>Photos:</h2>

    <% List<Image> images = (List<Image>) request.getAttribute("images");
    if (images == null || images.isEmpty()) { %>
        <p>Be the first to add an image of this destination!</p>
    <% }
    else { %>
      <%
        for(Image image : images ){
      %>
    <a href="<%=image.returnFilename()%>"><img src="<%=image.returnFilename()%>" width = "100" height = "100"/></a>
      <%
        }
      %>
    <% } %>

    <%-- New form to submit new photos, then below are the photos displayed --%>
    <form action="/destination/<%= request.getAttribute("destinationTitle") %>" method="POST">
      <div class="form-group">
        <label class="form-control-label">Title:</label>
        <input type="text" name="filename">
      </div>

      <button type="submit">Upload Image</button> <p>Copy and Paste Image Address Only!</p>
    </form>

    <hr/>

      <form action="/destination/<%= request.getAttribute("destinationTitle") %>" method="POST">
          <div class="form-group">
            <label class="form-control-label">Title:</label>
          <input type="text" name="conversationTitle">
        </div>

        <button type="submit">Start Conversation</button>
      </form>

      <hr/>



    <h1>Conversations</h1>
    <% if(request.getSession().getAttribute("user") == null){ %>
      <p>Login to start a conversation!</p>
    <% } %>

    <%
    List<Conversation> conversations =
      (List<Conversation>) request.getAttribute("conversations");
    if(conversations == null || conversations.isEmpty()){
    %>
      <p>Create a conversation to get started.</p>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      for(Conversation conversation : conversations){
    %>
      <li><a href="/chat/<%= conversation.getTitle() %>">
        <%= conversation.getTitle() %></a></li>
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
