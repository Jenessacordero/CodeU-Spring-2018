package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeu.model.data.UserAction;
import org.mindrot.jbcrypt.BCrypt;

import codeu.model.data.User;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;

public class RegisterServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;
  
  /** Store class that gives access to UserActions. */
  private UserActionStore userActionStore;


  /**
   * Set up state for handling registration-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
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
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserActionStore(UserActionStore userActionStore) {
    this.userActionStore = userActionStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
	// Logout Form
	     if (request.getParameter("extra_submit_param") != null) {
	    	 request.getSession().setAttribute("user", null);
	    	 response.sendRedirect("/index");
	     }
	     else {
	    	 String username = request.getParameter("username");

	    	    if (!username.matches("[\\w*\\s*]*")) {
	    	      request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
	    	      request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
	    	      return;
	    	    }

	    	    if (userStore.isUserRegistered(username)) {
	    	      request.setAttribute("error", "That username is already taken.");
	    	      request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
	    	      return;
	    	    }
	    	    
	    	    String password = request.getParameter("password");    
	    	    String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

	    	    User user = new User(UUID.randomUUID(), username, hashed, Instant.now());
	    	    userStore.addUser(user);
	    	    
	    	    // Creates a new user action.
	    	    UserAction newUser = new UserAction(UUID.randomUUID(), user.getId(), user.getName() + " has registered as a new user.", Instant.now());
	    	    userActionStore.addUserAction(newUser);
	    	    response.sendRedirect("/index");
	     }
  }
}
