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
<%@ page import="codeu.model.data.UserAction" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.UserAction" %>
<%@ page import="codeu.model.store.basic.UserStore" %><%
List<UserAction> userActions = (List<UserAction>) request.getAttribute("userActions");
%><!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
  <%@include file="nav.jsp" %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>
    <h1>Activity</h1>
    <p>Here's everything that's happened on the site so far!</p>
    <hr/>

  <div id="chat">
      <ul>
    <%
      for (UserAction userAction : userActions) {
    %>
      <li><strong><%= userAction.getFormattedTime() %>: </strong><%= userAction.getMessage() %></li>
    <%
      }
    %>
      </ul>
    </div>
    
    <hr/>

	<div>
    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/activityfeed" method="POST">
        <textarea name="status-update"> </textarea>
        <br/>
        <button type="submit">Send</button>
    </form>
    <% } else { %>
      <p><a href="/login">Login</a> to post a status update!</p>
    <% } %>
    </div>

    <hr/>
  </div>
</body>
</html>
