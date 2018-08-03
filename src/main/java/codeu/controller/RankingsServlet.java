package codeu.controller;

import codeu.model.data.Destination;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.store.basic.DestinationStore;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Servlet class responsible for the conversations page. */
public class RankingsServlet extends HttpServlet {
    //TODO change name to match jsp (tsk tsk to me)

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
     * This function fires when a user navigates to the rankings page. It gets all of the
     * rankings from the model and forwards to rankings.jsp for rendering the list.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<Destination> rankedDestinations = destinationStore.getRankedDestinations();
        request.setAttribute("rankedDestinations", rankedDestinations);
        request.getRequestDispatcher("/WEB-INF/view/rankingPage.jsp").forward(request, response);
    }

    /**
     * This function fires when a user tries to vote on the Rankings page. It gets the
     * logged-in username from the session, the action, and the destination voted on.
     * Ranks of destinations are adjusted accordingly.
     */
    // TODO: modify for ranking like and dislike
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    	// Logout Form
	     if (request.getParameter("extra_submit_param") != null) {
	    	 request.getSession().setAttribute("user", null);
	    	 response.sendRedirect("/index");
	     }
	     else {
	    	 String username = (String) request.getSession().getAttribute("user");
	         if (username == null) {
	             // user is not logged in, don't let them vote
	             response.sendRedirect("/index");
	             return;
	         }

	         User user = userStore.getUser(username);
	         if (user == null) {
	             // user was not found, don't let them vote
	             System.out.println("User not found: " + username);
	             response.sendRedirect("/rankingPage");
	             return;
	         }

	         //TODO: add checks for if they've already voted once
	         //Vote and create new user action
	         if (request.getParameter("upvote") != null) {
	             destinationStore.getDestinationWithTitle(request.getParameter("upvote")).upVote();
	             UserAction voteDestination = new UserAction(UUID.randomUUID(), user.getId(), user.getName() + " has upvoted  "
	                     + request.getParameter("upvote") + "!", Instant.now());
	             userActionStore.addUserAction(voteDestination);
	             response.sendRedirect("/rankingPage");

	         } else if (request.getParameter("downvote") != null) {
	             destinationStore.getDestinationWithTitle(request.getParameter("downvote")).downVote();
	             UserAction voteDestination = new UserAction(UUID.randomUUID(), user.getId(), user.getName() + " has downvoted  "
	                     + request.getParameter("downvote") + "!", Instant.now());
	             userActionStore.addUserAction(voteDestination);
	             response.sendRedirect("/rankingPage");
	         }
	     }
    }
}

