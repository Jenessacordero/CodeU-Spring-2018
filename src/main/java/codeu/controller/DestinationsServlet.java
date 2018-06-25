// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Destination;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.DestinationStore;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the conversations page. */
public class DestinationsServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Destinations. */
  private DestinationStore destinationStore;
  
  /** Store class that gives access to UserActions. */
  private UserActionStore userActionStore;

  /**
   * Set up state for handling conversation-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setDestinationStore(DestinationStore.getInstance());
    setUserActionStore(UserActionStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the DestinationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setDestinationStore(DestinationStore destinationStore) {
    this.destinationStore = destinationStore;
  }
  
  /**
   * Sets the UserActionStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setUserActionStore(UserActionStore UserActionStore) {
    this.userActionStore = UserActionStore;
  }

  /**
   * This function fires when a user navigates to the conversations page. It gets all of the
   * conversations from the model and forwards to conversations.jsp for rendering the list.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    List<Destination> destinations = destinationStore.getAllDestinations();
    request.setAttribute("destinations", destinations);
    request.getRequestDispatcher("/WEB-INF/view/destinations.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the conversations page. It gets the
   * logged-in username from the session and the new conversation title from the submitted form
   * data. It uses this to create a new Conversation object that it adds to the model.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them create a destination
      response.sendRedirect("/destinations");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them create a destination
      System.out.println("User not found: " + username);
      response.sendRedirect("/destinations");
      return;
    }
  

    String destinationTitle = request.getParameter("destinationTitle");
    if (!destinationTitle.matches("[\\w*]*")) {
      request.setAttribute("error", "Please enter only letters and numbers.");
      request.getRequestDispatcher("/WEB-INF/view/destinations.jsp").forward(request, response);
      return;
    }

    if (destinationStore.isTitleTaken(destinationTitle)) {
      // destination title is already taken, just go into that conversation instead of creating a
      // new one
      response.sendRedirect("/destination/" + destinationTitle);
      return;
    }

    Destination destination =
        new Destination(UUID.randomUUID(), user.getId(), destinationTitle, Instant.now());

    destinationStore.addDestination(destination);
    
    // Creates a new user action.
    UserAction newDestination = new UserAction(UUID.randomUUID(), user.getId(), user.getName() + " has created a new destination: "
    		+ destinationTitle, Instant.now());
    userActionStore.addUserAction(newDestination);
    response.sendRedirect("/destination/" + destinationTitle);
  }
}
