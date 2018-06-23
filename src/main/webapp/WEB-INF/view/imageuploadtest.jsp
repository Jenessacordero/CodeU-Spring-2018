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
  -- currently outputs a blank webpage for testing purposes
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Images" %>


<!DOCTYPE html>
<html>
<head>
  <title>Image Upload (Test)</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
      <a id="navTitle" href="/">CodeU Chat App</a>
      <% if(request.getSession().getAttribute("user") != null && (request.getSession().getAttribute("user").equals("cavalos99") ||
          		request.getSession().getAttribute("user").equals("jenessacordero") || request.getSession().getAttribute("user").equals("agarwalv"))) {%>
          <a href="/adminpage">Admin</a>
      <% } %>
      <a href="/about.jsp">About</a>
      <a href="/activityfeed">Activity Feed</a>
      <a href="/conversations">Conversations</a>
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

    <% if(request.getSession().getAttribute("user") != null){ %>
      <h1>Image Upload Test</h1>
      <hr/>
    <% } %>

    <h3>File Upload:</h3>
          Select a file to upload: <br />
          <form action = "UploadServlet" method = "post"
             enctype = "multipart/form-data">
             <input type = "file" name = "file" size = "50" />
             <br />
             <input type = "submit" value = "Upload File" />
          </form>

    <hr/>

    <%
        List<Images> uploadedImages = (List<Images>) request.getAttribute("images");
        for(Images images : uploadedImages) { %>
            <img src = "<%= images.returnFileName() %> height ="50" width ="50">
        <% }
    %>
  </div>
</body>
</html>
