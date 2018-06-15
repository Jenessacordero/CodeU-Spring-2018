// just copied main code from conversation servlet. Prototype includes hard-coded
// activity feed page message.
package codeu.controller;

import java.io.PrintWriter;
import codeu.model.data.Conversation;
import codeu.model.data.StatusUpdate;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.StatusUpdateStore;
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

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class ActivityFeedServlet extends HttpServlet {

  private UserStore userStore;
  
  private UserActionStore userActionStore;
  
  private StatusUpdateStore statusUpdateStore;


  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setUserActionStore(UserActionStore.getInstance());
    setStatusUpdateStore(StatusUpdateStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  
  /**
   * Sets the UserActionStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setUserActionStore(UserActionStore userActionStore) {
    this.userActionStore = userActionStore;
  }
  
  /**
   * Sets the StatusUpdateStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setStatusUpdateStore(StatusUpdateStore statusUpdateStore) {
    this.statusUpdateStore = statusUpdateStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
	  List<UserAction> userActions = userActionStore.returnAllUserActions();
	  request.setAttribute("userActions", userActions);
      request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);

    }
  
  /**
   * This function fires when a user submits the form on the activityFeed page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = (String) request.getSession().getAttribute("user");  
    User user = userStore.getUser(username);

    String statusUpdateContent = request.getParameter("status-update");
    
    String cleanedStatusUpdateContent = Jsoup.clean(statusUpdateContent, Whitelist.none());

    // Creates a status update.
    StatusUpdate statusUpdate =
        new StatusUpdate(UUID.randomUUID(), user.getId(), cleanedStatusUpdateContent, Instant.now());

    statusUpdateStore.addStatusUpdate(statusUpdate);
    
    // Creates a user action.
    UserAction newStatusUpdate = new UserAction(UUID.randomUUID(), user.getId(), user.getName() + ": "
    		+ cleanedStatusUpdateContent, Instant.now());
    userActionStore.addUserAction(newStatusUpdate);
    response.sendRedirect("/activityfeed");
  }

}
