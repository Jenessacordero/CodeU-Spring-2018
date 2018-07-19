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

<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

<%@include file="WEB-INF/view/nav.jsp" %>

<div id="box">
  <div id="login" style ="margin-left: 30%;
  margin-right: auto;
  margin-top: 10%;
  width: 40%;
  height: 60%;
  background-color: rgba(0, 0, 0, .6);
  position: absolute;">
    <h1 style="float: none;
  display: block;
  margin-left: auto;
  margin-right: auto;
  text-align: center;
  color: white;">
      Login</h1>

    <% if(request.getAttribute("error") != null){ %>
    <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>
    <form action="/index" method="POST" style="float: none;
  display: block;
  margin-left: auto;
  margin-right: auto;
  text-align: center;
  color: white;">

      <label for="username">Username: </label>
      <br/>
      <input type="text" name="username" id="username">
      <br/>
      <label for="password">Password: </label>
      <br/>
      <input type="password" name="password" id="password">
      <br/><br/>
      <button type="submit">Login</button>
    </form>
    <p style="float: none;
  display: block;
  margin-left: auto;
  margin-right: auto;
  text-align: center;
  color: white;">New users can register <a href="/register">here</a>.</p>
  </div>
</div>

</body>
</html>

