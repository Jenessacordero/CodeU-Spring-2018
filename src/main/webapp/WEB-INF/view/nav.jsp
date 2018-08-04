
    <div id="navLeft">
    <a id="navTitle" href="/">CodeU Crossing</a>
    </div>
    <div id="navRight">

        <div class="dropdown">
            <span>Menu</span>
            <div class="dropdown-content">
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

            </div>
        </div>

            <% if(request.getSession().getAttribute("user") != null){ %>
            <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
            <% } else{ %>
            <a href="/index">Login</a>
            <% } %>
                <% if (request.getSession().getAttribute("user") != null) {%>
                    <form method="POST" action="/index" class="inline">
                        <input type="hidden" name="extra_submit_param" value="extra_submit_value">
                        <button type="submit" name="submit_param" value="submit_value" class="link-button">
                            Logout
                        </button>
                    </form>
                <% }
                %>
    </div>