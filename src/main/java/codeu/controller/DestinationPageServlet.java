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
import codeu.model.data.Image;
import codeu.model.store.basic.*;
import codeu.model.data.Banner;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the conversations page. */
public class DestinationPageServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;
  
  /** Store class that gives access to Destinations. */
  private DestinationStore destinationStore;
  
  /** Store class that gives access to UserActions. */
  private UserActionStore userActionStore;

  /** Store class that gives access to Images. */
  private ImageStore imageStore;

  /** Store class that gives access to Images. */
  private BannerStore bannerStore;

  /**
   * Set up state for handling conversation-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setDestinationStore(DestinationStore.getInstance());
    setUserActionStore(UserActionStore.getInstance());
    setImageStore(ImageStore.getInstance());
    setBannerStore(BannerStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
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
   * Sets the ImageStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */

  void setImageStore(ImageStore imageStore) {
    this.imageStore = imageStore;
  }

  /**
   * Sets the BannerStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */

  void setBannerStore(BannerStore bannerStore) {
    this.bannerStore = bannerStore;
  }

  /**
   * This function fires when a user navigates to the conversations page. It gets all of the
   * conversations from the model and forwards to conversations.jsp for rendering the list.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
	  String requestUrl = request.getRequestURI();
	  String destinationTitle = requestUrl.substring("/destination/".length());
	  Destination destination = destinationStore.getDestinationWithTitle(destinationTitle);
	  List<Image> images = imageStore.returnImagesInDestination(destination);
	  String banner = destination.getBanner();
	  Banner banner2 = bannerStore.returnBanner(destination.getTitle());

    List<Conversation> conversations = conversationStore.getConvosInDestination(destination.getId());
    request.setAttribute("conversations", conversations);
    request.setAttribute("destinationTitle", destinationTitle);
    request.setAttribute("images", images);
    request.setAttribute("banner", banner2.returnBanner());
    request.getRequestDispatcher("/WEB-INF/view/destinationPage.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the conversations page. It gets the
   * logged-in username from the session and the new conversation title from the submitted form
   * data. It uses this to create a new Conversation object that it adds to the model.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
	  
	  String requestUrl = request.getRequestURI();
	  String destinationTitle = requestUrl.substring("/destination/".length());
	  Destination destination = destinationStore.getDestinationWithTitle(destinationTitle);

	  
    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them create a conversation
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them create a conversation
      System.out.println("User not found: " + username);
      response.sendRedirect("/login");
      return;
    }

    // conversation form

    String conversationTitle = request.getParameter("conversationTitle");
    if (conversationTitle != null && conversationTitle != "") {
      if (!conversationTitle.matches("[\\w*]*")) {
        request.setAttribute("error", "Please enter only letters and numbers.");
        request.getRequestDispatcher("/WEB-INF/view/destinationPage.jsp").forward(request, response);
        return;
      }
      if (conversationStore.isTitleTaken(conversationTitle)) {
        // conversation title is already taken, just go into that conversation instead of creating a
        // new one
        response.sendRedirect("/chat/" + conversationTitle);
        return;
      }

      Conversation conversation =
              new Conversation(UUID.randomUUID(), user.getId(), destination.getId(), conversationTitle, Instant.now());

      conversationStore.addConversation(conversation);

      // Creates a new user action.
      UserAction newConversation = new UserAction(UUID.randomUUID(), user.getId(), user.getName() + " has created a new conversation: "
              + conversationTitle, Instant.now());
      userActionStore.addUserAction(newConversation);
      response.sendRedirect("/chat/" + conversationTitle);
    }
    else if (request.getParameter("banner") != null && request.getParameter("banner") != "") {
        String banner = request.getParameter("banner");
        destination.changeBanner(banner);
        bannerStore.addBanner(destinationTitle, new Banner(banner, destinationTitle, UUID.randomUUID(), Instant.now()));
        response.sendRedirect("/destination/" + destinationTitle);
        return;
      }
      else {
        if (request.getParameter("filename") != null && request.getParameter("filename") != "") {
          String imageFilename = request.getParameter("filename");
          Image newImage = new Image(imageFilename, destinationTitle, UUID.randomUUID(), Instant.now());
          imageStore.addImage(newImage);
          response.sendRedirect("/destination/" + destinationTitle);
        }
      }
    }
  }
