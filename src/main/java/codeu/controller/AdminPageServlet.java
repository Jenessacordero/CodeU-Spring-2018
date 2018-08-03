// just copied main code from conversation servlet. Prototype includes hard-coded
// admin page message.
package codeu.controller;

import codeu.model.data.StatusUpdate;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
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

public class AdminPageServlet extends HttpServlet {

  private UserStore userStore;

  private ConversationStore conversationStore;

  private MessageStore messageStore;


  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
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
     * Sets the MessageStore used by this servlet. This function provides a common setup method
     * for use by the test framework or the servlet's init() function.
     */
  void setMessageStore(MessageStore messageStore) { this.messageStore = messageStore; }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
      // Methods are written in respective classes that return the list of all
      // conversations, messages, and users
      // Lists are needed to retrieve the length
      List allConversations = conversationStore.getAllConversations();
      List allUsers = userStore.returnAllUsers();
      List allMessages = messageStore.returnAllMessages();

      // sets attributes to the backend so it can be retrieved in the jsp file
      request.setAttribute("numConversations", allConversations.size());
      request.setAttribute("numUsers", allUsers.size());
      request.setAttribute("numMessages", allMessages.size());
      request.setAttribute("wordiest", userStore.wordiestUser().getName());
      request.setAttribute("mostActive", userStore.mostActive().getName());
      request.setAttribute("newest", userStore.getNewestUser().getName());
    request.getRequestDispatcher("/WEB-INF/view/adminpage.jsp").forward(request, response);
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
	// Logout Form
	     if (request.getParameter("extra_submit_param") != null) {
	    	 request.getSession().setAttribute("user", null);
	    	 response.sendRedirect("/index");
	     }  
  }

}
