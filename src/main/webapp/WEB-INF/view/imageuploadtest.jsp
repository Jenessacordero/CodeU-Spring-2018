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
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/user/<%=request.getSession().getAttribute("user") %>">Profile Page</a>
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

    <h1>Test</h1>
    <form action="/imageuploadtest" method="POST">
              <div class="form-group">
                <label class="form-control-label">Filename:</label>
              <input type="text" name="filename">
            </div>

            <button type="submit">Submit</button>
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
