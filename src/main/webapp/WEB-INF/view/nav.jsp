<nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/about.jsp">About</a>
    <a href="/activityfeed">Activity Feed</a>
    <a href="/destinations">Destinations</a>
    <a href="/rankingPage">Rankings</a>
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