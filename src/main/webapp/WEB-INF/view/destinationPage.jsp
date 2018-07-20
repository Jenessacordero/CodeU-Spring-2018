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
<%@ page import="codeu.model.data.Destination" %>
<%@ page import="codeu.model.data.Tip" %>


<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@include file="nav.jsp" %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

      <% if (request.getAttribute("destinationTitle") != null) { %>
      <h1><%= request.getAttribute("destinationTitle") %></h1>
    <% } %>
    <% if (request.getAttribute("ranks") != null) { %>
    <h3>Ranked #<%=request.getAttribute("ranks")%></h3>
<% } %>
    <% if (request.getAttribute("banner") != null) { %>
        <img src="<%=request.getAttribute("banner").toString()%>" height="400" width="800"/>
    <% } %>

    <form action="/destination/<%= request.getAttribute("destinationTitle") %>" method="POST">
      <div class="form-group">
        <label class="form-control-label">Upload/Change the Banner Image (Image Address Only): </label>
        <input type="text" name="banner">
      </div>
    </form>


    <h2>Photos:</h2>
      <%-- New form to submit new photos, then below are the photos displayed --%>
      <form action="/destination/<%= request.getAttribute("destinationTitle") %>" method="POST">
          <div class="form-group">
              <label class="form-control-label">Image Address:</label>
              <input type="text" name="filename">
          </div>

          <button type="submit">Upload Image</button>
      </form>

    <% List<Image> images = (List<Image>) request.getAttribute("images");
    if (images == null || images.isEmpty()) { %>
        <p>Be the first to add an image of this destination!</p>
    <% }
    else { %>
      <%
        for(Image image : images ){
      %>
    <a href="<%=image.returnFilename().getValue()%>"><img src="<%=image.returnFilename().getValue()%>" width = "100" height = "100"/></a>
      <%
        }
      %>
    <% } %>

    <hr/>

    <h1>Conversations</h1>
    <form action="/destination/<%= request.getAttribute("destinationTitle") %>" method="POST">
      <div class="form-group">
        <label class="form-control-label">Conversation Title:</label>
        <input type="text" name="conversationTitle">
      </div>

      <button type="submit">Start Conversation</button>
    </form>
    <% if(request.getSession().getAttribute("user") == null){ %>
      <p>Login to start a conversation!</p>
    <% }
    else if((List<Conversation>) request.getAttribute("conversations") == null || ((List<Conversation>) request.getAttribute("conversations")).isEmpty()){
    %>
      <p>Create a conversation to get started.</p>
    <%
    }
    else {
    %>
      <ul class="mdl-list">
    <%
      List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
      String destinationTag = (String) request.getAttribute("destinationTag");
      for(Conversation conversation : conversations){
    %>
      <li><a href="/chat/<%=conversation.getTitle() %>">
        <%= conversation.getTitle() %></a></li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>
	<div>
	<h1>Tips</h1>
      <div>
        <form action="/destination/<%= request.getAttribute("destinationTitle") %>" method="POST">
          <label class="form-control-label">Tip Content:</label>
          <input type="text" name="tip">
          <br/>
          <button type="submit">Add a tip!</button>
        </form>
      </div>
    <% if(request.getSession().getAttribute("user") == null){ %>
      <p>Login to add a tip!</p>
    <% }
    else if((List<Tip>) request.getAttribute("tips") == null || ((List<Tip>) request.getAttribute("tips")).isEmpty()){
    %>
      <p>Add some tips!</p>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      List<Tip> tips = (List<Tip>) request.getAttribute("tips");
      for(Tip tip : tips){
    %>
        <li><%= tip.getContent() %></li>
    <%
      }
    %>
      </ul>
    <%
    }
    %>
	</div>
    <hr/>
  </div>
</body>
</html>
