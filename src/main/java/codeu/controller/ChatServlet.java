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

import codeu.model.data.*;
import codeu.model.store.basic.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet class responsible for the chat page. */
public class ChatServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;
  
  /** Store class that gives access to UserActions. */
  private UserActionStore userActionStore;

  /** Store class that gives access to Images. */
  private ImageStore imageStore;

  /** Store class that gives access to Destinations. */
  private DestinationStore destinationStore;

  
  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setUserActionStore(UserActionStore.getInstance());
    setImageStore(ImageStore.getInstance());
    setDestinationStore(DestinationStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserActionStore(UserActionStore userActionStore) {
    this.userActionStore = userActionStore;
  }
  
  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the ImageStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setImageStore(ImageStore imageStore) {
    this.imageStore = imageStore;
  }

  /**
   * Sets the DestinationStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setDestinationStore(DestinationStore destinationStore) {
    this.destinationStore = destinationStore;
  }

  /**
   * This function fires when a user navigates to the chat page. It gets the conversation title from
   * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
   * It then forwards to chat.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring(("/chat/".length()));

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationTitle);
      response.sendRedirect("/destinations");
      return;
    }

    UUID conversationId = conversation.getId();

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);

    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the chat page. It gets the logged-in
   * username from the session, the conversation title from the URL, and the chat message from the
   * submitted form data. It creates a new Message from that data, adds it to the model, and then
   * redirects back to the chat page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
	// Logout Form
	     if (request.getParameter("extra_submit_param") != null) {
	    	 request.getSession().setAttribute("user", null);
	    	 response.sendRedirect("/index");
	     }  
	     else {
	    	 String cleanedMessageContent = "";
	    	    Character type = 'm';
	    	    String username = (String) request.getSession().getAttribute("user");
	    	    if (username == null) {
	    	      // user is not logged in, don't let them add a message
	    	      response.sendRedirect("/index");
	    	      return;
	    	    }

	    	    User user = userStore.getUser(username);
	    	    if (user == null) {
	    	      // user was not found, don't let them add a message
	    	      response.sendRedirect("/index");
	    	      return;
	    	    }

	    	    String requestUrl = request.getRequestURI();
	    	    String conversationTitle = requestUrl.substring("/chat/".length());

	    	    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
	    	    if (conversation == null) {
	    	      // couldn't find conversation, redirect to conversation list
	    	      response.sendRedirect("/destinations");
	    	      return;
	    	    }
	    	    if (request.getParameter("message") != null) {
	    	      String messageContent = request.getParameter("message");

	    	      // this removes any HTML from the message content
	    	      cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());


	    	      // Creates a new user action.
	    	      UserAction newMessage = new UserAction(UUID.randomUUID(), user.getId(), user.getName() + " has sent a message in " + conversationTitle
	    	              + ": " + "\"" + cleanedMessageContent + "\"", Instant.now());
	    	      userActionStore.addUserAction(newMessage);
	    	    }
	    	    else {
	    	      if (request.getParameter("image") != null) {
	    	        cleanedMessageContent = request.getParameter("image");
	    	        type = 'i';
	    	        UserAction newMessage = new UserAction(UUID.randomUUID(), user.getId(), user.getName() + " has sent an image in " + conversationTitle
	    	                + ": " + "\"" + cleanedMessageContent + "\"", Instant.now());
	    	        userActionStore.addUserAction(newMessage);
	    	      }
	    	    }

	    	    // Creates a message.
	    	    Message message =
	    	            new Message(
	    	                    UUID.randomUUID(),
	    	                    conversation.getId(),
	    	                    user.getId(),
	    	                    cleanedMessageContent,
	    	                    Instant.now(), type);

	    	    messageStore.addMessage(message);
	    	    user.changeNumPersonalMessageCount();
	    	    user.changeNumWords(message.getContent());
	    	    response.sendRedirect("/chat/" + conversationTitle);
	     }
  }
}
