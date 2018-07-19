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
<%@ page import="codeu.model.data.Banner" %>
<%@ page import="codeu.model.data.Destination" %>
<%@ page import="codeu.model.store.basic.BannerStore" %>
<%@ page import="com.google.appengine.api.datastore.Text" %>
<%@ page import="java.util.List" %>


<!DOCTYPE html>
<html>
<head>
    <title>Rankings</title>
    <link rel="stylesheet" href="/css/main.css">
</head>
<body>

<%@include file="nav.jsp" %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <h1>Ranks</h1>
    <p>Here are the trending places to visit - where will you go next?</p>
    <hr/>

  <div id="chat">
      <ol>
    <%
        BannerStore bannerStore = BannerStore.getInstance();

        List<Destination> rankedDestinations = (List<Destination>) request.getAttribute("rankedDestinations");
      for (Destination destination : rankedDestinations) {
    %>

        <li>
         <form action="${pageContext.request.contextPath}/rankingPage" method="post">
             <button type="submit" name="upvote" value =<%=destination.getTitle()%> >Upvote</button>
             <button type="submit" name="downvote" value =<%=destination.getTitle()%> >Downvote</button>
         </form>

          <strong><%= destination.getVotes() %> </strong>       <a href="/destination/<%= destination.getTitle() %>"><%= destination.getTitle() %></a></li>
          <% Text banner = destination.getBanner();
              if (banner != null) {
                  Banner banner2 = bannerStore.returnBanner(destination.getTitle());
                  if (banner2 != null) { %>
          <img src="<%=banner2.returnBanner().getValue()%>" height="200" width="400"/>
          <% }
          }%>
          </li>
    <%
      }
    %>
      </ol>
    </div>

    <hr/>

	<div>
    <% if (request.getSession().getAttribute("user") == null) { %>
      <p><a href="/login">Login</a> to vote on destinations!</p>
    <% } %>
    </div>

    <hr/>
  </div>
</body>
</html>
