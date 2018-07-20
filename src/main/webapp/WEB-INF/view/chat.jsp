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
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="org.kefirsf.bb.BBProcessorFactory" %>
<%@ page import="org.kefirsf.bb.ConfigurationFactory" %>
<%@ page import="org.kefirsf.bb.TextProcessor" %>

<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>

  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollChat()">

  <%@include file="nav.jsp" %>

  <div id="container">

    <h1><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">
      <ul>
    <%-- the processor stuff below is for styled text --%>
    <%
        TextProcessor processor = BBProcessorFactory.getInstance()
            .createFromResource(ConfigurationFactory.MARKDOWN_CONFIGURATION_FILE);
        assert "<strong>text</strong>".equals(processor.process("**text**"));
    %>

    <%
        if (messages != null && !messages.isEmpty()) {
      for (Message message : messages) {
        String author = UserStore.getInstance()
          .getUser(message.getAuthorId()).getName();
         User user = UserStore.getInstance()
                .getUser(message.getAuthorId());
         if (message.getType().equals('m')) {
        %>
            <a href="/user/<%=author %>"><li><strong><%= author %></a>:</strong> <%= processor.process(message.getContent()) %></li>
        <%
         } else {
             %>
            <a href="/user/<%=author %>"><li><strong><%= author %></a>:</strong></li>
        <a href="<%=message.getContent()%>"><img src="<%=message.getContent()%>" width = "75" height = "75"></a>
        <%
         }
      }
            }
    %>
      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text" name="message">
        <br/>
        <button type="submit">Send Message</button>
    </form>
      <form action="/chat/<%= conversation.getTitle() %>" method="POST">
          <input type="text" name="image">
          <br/>
          <button type="submit">Send Image (Image Address Only)</button>
      </form>
    <% } else { %>
      <p><a href="/index">Login</a> to send a message.</p>
    <% } %>

    <hr/>

  </div>

</body>
</html>
