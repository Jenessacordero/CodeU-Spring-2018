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
<%@ page import="codeu.model.data.Countries" %>
<%@ page import="codeu.model.data.Banner" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.w3c.dom.Text" %>
<%@ page import="codeu.model.store.basic.BannerStore" %>
<%@ page import="codeu.model.store.basic.DestinationStore" %>
<%@ page import="java.text.Collator" %>
<%@ page import="java.util.ArrayList" %>


<%
List<String> countryList = (List<String>) request.getAttribute("countryList");
HashMap<String, Banner> bannerList = (HashMap<String, Banner>) request.getAttribute("banners");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Destinations</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@include file="nav.jsp" %>
  
  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>


      <h1>New Destination</h1>
      <form action="/destinations" method="POST">
          <div class="form-group">
            <label class="form-control-label">Destination:</label>
          <select name="destinationTitle">
          <%
            if (countryList != null || !countryList.isEmpty()) {
      for(String country : countryList){%>
          <option value="<%= country %>"><%= country %></option>
          <%
          }
            }
          %>
          </select>
            <br/>
            <label class="form-control-label">Banner Image Address:</label>
            <input type="text" name="banner">
        </div>

        <button type="submit">Create Destination</button>
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
      DestinationStore dStore = DestinationStore.getInstance();
      List<String> destinationStrings = new ArrayList<>();
      for (Destination destination: destinations) {
          destinationStrings.add(destination.getTitle());
      }
      java.util.Collections.sort(destinationStrings, Collator.getInstance());
      for(String destination : destinationStrings){
          Destination current = dStore.getDestinationWithTitle(destination);
    	  String cleanDestinationTitle = destination.replace("_", " ");
    %>  <div id="destImage">
        <% com.google.appengine.api.datastore.Text banner = current.getBanner();
           BannerStore bannerStore = BannerStore.getInstance();
          if (banner != null) {
              Banner banner2 = bannerStore.returnBanner(cleanDestinationTitle);
              if (banner2 != null) { %>
          <a href="/destination/<%= destination %>"><img src="<%=banner2.returnBanner().getValue()%>" height="375" width="500"/></a>
       <% }
          } %>
          <div id="destText">
          <li><a href="/destination/<%= destination %>">
              <%= cleanDestinationTitle %></a></li>
          </div>
      </div>
          <p></p>
      <%}
    %>
      </ul>
    <%
    }
    %>
    <hr/>
  </div>
</body>
</html>
