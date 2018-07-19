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
<%@ page import="codeu.model.data.UserAction" %>
<%@ page import="codeu.model.data.AboutMe" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
List<UserAction> userActions = (List<UserAction>) request.getAttribute("userActions");
AboutMe aboutMe = (AboutMe) request.getAttribute("aboutMe");
%>
<!DOCTYPE html>
<html>

<head>
  <title>CodeU Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
    <%@include file="nav.jsp" %>
  
   <div style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;" id="container">
    <div>
      
       <% if(request.getSession().getAttribute("user") != null){ %>
       <% if(request.getSession().getAttribute("user").equals(request.getAttribute("username"))){ %>
       <h1>Welcome <%= request.getAttribute("username") %>!</h1>
        <% } else{ %>
      <h1>This is <%= request.getAttribute("username") %>'s profile page!</h1>
    <% } %>
    <% } %>
    </div>
    
    
     <div contenteditable="true"> 
     <% if(request.getSession().getAttribute("user") != null){ %>
       <% if(request.getSession().getAttribute("user").equals(request.getAttribute("username"))){ %>
      <form action="/user/<%= request.getSession().getAttribute("user") %>" method="POST">
        <textarea name="aboutme"><%= aboutMe.getContent() %></textarea>
        <br/>
        <button type="submit">Update Bio</button>
    </form>
        <% } else if(!request.getSession().getAttribute("user").equals(request.getAttribute("username"))){ %>
        <h1><%= request.getAttribute("username") %>'s bio:</h1>
		<p>
       <%= aboutMe.getContent() %>
       </p>
        <% } %>
        <% } else if(request.getSession().getAttribute("user") == null){ %>
         <h1><%= request.getAttribute("username") %>'s bio:</h1>
		<p>
       <%= aboutMe.getContent() %>
       </p>
        <% } %>
        
        </div>
        
     
	
    <h1>User Activity</h1>
    <hr/>

    <div id="chat">
    
      <ul>
    <%
      for (UserAction userAction : userActions) {
    %>
      <li><strong><%= userAction.getFormattedTime() %>: </strong><%= userAction.getMessage() %></li>
          <% } %>
      </ul>
    </div>

    <hr/>
    </div>
  
 </body>

</html>
