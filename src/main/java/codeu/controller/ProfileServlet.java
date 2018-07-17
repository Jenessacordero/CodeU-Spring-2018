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
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.data.AboutMe;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.basic.AboutMeStore;
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
public class ProfileServlet extends HttpServlet {
	
	/** Store class that gives access to Users. */
	private UserStore userStore;

	/** Store class that gives access to Conversations. */
	private ConversationStore conversationStore;
	
	/** Store class that gives access to UserActions. */
	private UserActionStore userActionStore;
	
	/** Store class that gives access to AboutMes. */
	private AboutMeStore aboutMeStore;
	
	/**
	 * Set up state for handling conversation-related requests. This method is only called when
	 * running in a server, not when running in a test.
	 */
	 @Override
	 public void init() throws ServletException {
		 super.init();
		 setUserStore(UserStore.getInstance());
		 setConversationStore(ConversationStore.getInstance());
		 setUserActionStore(UserActionStore.getInstance());
		 setAboutMeStore(AboutMeStore.getInstance());
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
	   * Sets the UserActionStore used by this servlet. This function provides a common setup method for use
	   * by the test framework or the servlet's init() function.
	   */
	  void setUserActionStore(UserActionStore userActionStore) {
	    this.userActionStore = userActionStore;
	  }
	  
	  /**
	   * Sets the AboutMeStore used by this servlet. This function provides a common setup method for use
	   * by the test framework or the servlet's init() function.
	   */
	  void setAboutMeStore(AboutMeStore aboutMeStore) {
	    this.aboutMeStore = aboutMeStore;
	  }

	  
	  /**
	   * This function fires when a user navigates to the profile page.
	   */
	  @Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws IOException, ServletException {
		  String requestUrl = request.getRequestURI();
		  String username = requestUrl.substring("/user/".length());
		  
		  User user = userStore.getUser(username);
		  UUID userID = user.getId();
		  
		  List<UserAction> userActions = userActionStore.returnUserActionsByUser(userID);
		  AboutMe aboutMe = aboutMeStore.getAboutMeByUser(userID);
		  
		  request.setAttribute("userActions", userActions);
		  request.setAttribute("user", user);
		  request.setAttribute("username", username);
		  request.setAttribute("aboutMe", aboutMe);
		  
		  request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
	  }
	  
	  /**
	   * This function fires when a user submits a form on the page.
	   */
	  @Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
	      throws IOException, ServletException {
		  
		  // Here I get information about the user.
		  String username = (String) request.getSession().getAttribute("user");
		  User user = userStore.getUser(username);
		  
		  String aboutMeContent = request.getParameter("aboutme");

		  // this removes any HTML from the message content
		  String cleanedAboutMeContent = Jsoup.clean(aboutMeContent, Whitelist.none());
		  
		  // Create a new AboutMe object and add it to the store.
		  AboutMe aboutMe =
			        new AboutMe(
			            UUID.randomUUID(),
			            user.getId(),
			            cleanedAboutMeContent,
			            Instant.now());
		  
		  try {
			aboutMeStore.addAboutMe(aboutMe);
		} catch (PersistentDataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  // redirect to a GET request
		  response.sendRedirect("/user/" + username);
		  
	  }

}