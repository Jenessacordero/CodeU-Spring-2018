package codeu.controller;

import codeu.controller.RankingsServlet;
import codeu.model.data.Destination;
import codeu.model.data.User;
import codeu.model.store.basic.DestinationStore;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import com.google.appengine.api.datastore.Text;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import sun.security.krb5.internal.crypto.Des;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class RankingsServletTest {

    private RankingsServlet rankingsServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private RequestDispatcher mockRequestDispatcher;
    private UserStore mockUserStore;
    private DestinationStore mockDestinationStore;
    private UserActionStore mockUserActionStore;

    @Before
    public void setup() {
        rankingsServlet = new RankingsServlet();
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);

        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/rankingPage.jsp"))
                .thenReturn(mockRequestDispatcher);

        mockDestinationStore = Mockito.mock(DestinationStore.class);
        rankingsServlet.setDestinationStore(mockDestinationStore);

        mockUserStore = Mockito.mock(UserStore.class);
        rankingsServlet.setUserStore(mockUserStore);

        mockUserActionStore = Mockito.mock(UserActionStore.class);
        rankingsServlet.setUserActionStore(mockUserActionStore);

    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        Destination dest1 = new Destination(UUID.randomUUID(), UUID.randomUUID(), "location1", Instant.now(), new Text(""), 5);
        Destination dest2 = new Destination(UUID.randomUUID(), UUID.randomUUID(), "location2", Instant.now(), new Text(""), 0);
        Destination dest3 = new Destination(UUID.randomUUID(), UUID.randomUUID(), "location3", Instant.now(), new Text(""), -3);
        List<Destination> fakeRankedList = new ArrayList<>();
        fakeRankedList.add(dest1);
        fakeRankedList.add(dest2);
        fakeRankedList.add(dest3);
        Mockito.when(mockDestinationStore.getRankedDestinations()).thenReturn(fakeRankedList);

        rankingsServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }


    @Test
    public void testDoPost_UserNotLoggedIn() throws IOException, ServletException, PersistentDataStoreException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

        rankingsServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockDestinationStore, Mockito.never())
                .addDestination(Mockito.any(Destination.class));
        Mockito.verify(mockResponse).sendRedirect("/index");
    }

    @Test
    public void testDoPost_InvalidUser() throws IOException, ServletException, PersistentDataStoreException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
        Mockito.when(mockUserStore.getUser("test_username")).thenReturn(null);

        rankingsServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockDestinationStore, Mockito.never())
                .addDestination(Mockito.any(Destination.class));
        Mockito.verify(mockResponse).sendRedirect("/rankingPage");
    }

//    @Test
//    public void testUpVote() throws IOException, ServletException {
//        Destination fakeDestination = Mockito.mock(Destination.class);
//        User fakeUser = Mockito.mock(User.class);
////        Destination fakeDestination = new Destination(UUID.randomUUID(), UUID.randomUUID(), "test_destination", Instant.now(), new Text(""), 0);
//        fakeUser.getVotesDictionary().put(fakeDestination.title, "upvote");
//
//        Mockito.when(mockRequest.getParameter("upvote")).thenReturn("test_destination");
//        Mockito.when(mockDestinationStore.getDestinationWithTitle("test_destination")).thenReturn(fakeDestination);
//        Mockito.when(mockSession.getAttribute("user")).thenReturn("test_user");
//        Mockito.when(mockUserStore.getUser("test_user")).thenReturn(fakeUser);
//        Mockito.when(fakeUser.getVotesDictionary().get("test_destination")).thenReturn(null);
//
//        rankingsServlet.doPost(mockRequest, mockResponse);
//
//        Mockito.verify(fakeDestination, Mockito.times(1)).upVote();
//        Assert.assertEquals(1, fakeDestination.getVotes());
//    }
//
//    @Test
//    public void testDownVote() {
//        Destination fakeDestination = Mockito.mock(Destination.class);
//
//        fakeDestination.downVote();
//
//        Mockito.verify(fakeDestination, Mockito.times(1)).downVote();
//        Assert.assertEquals(-1, fakeDestination.getVotes());
//    }
//
//
//    @Test
//    public void testUpVote_multiple() {
//        Destination fakeDestination = Mockito.mock(Destination.class);
//
//        fakeDestination.upVote();
//        fakeDestination.upVote();
//
//        Mockito.verify(fakeDestination, Mockito.times(1)).upVote();
//        Assert.assertEquals(1, fakeDestination.getVotes());
//    }
//
//    @Test
//    public void testDownVote_multiple() {
//        Destination fakeDestination = Mockito.mock(Destination.class);
//
//        fakeDestination.downVote();
//        fakeDestination.downVote();
//
//        Mockito.verify(fakeDestination, Mockito.times(1)).downVote();
//        Assert.assertEquals(-1, fakeDestination.getVotes());
//    }
//
//    @Test
//    public void testUpThenDown() {
//        Destination fakeDestination = Mockito.mock(Destination.class);
//
//        fakeDestination.upVote();
//        fakeDestination.downVote();
//
//        Mockito.verify(fakeDestination, Mockito.times(1)).upVote();
//        Mockito.verify(fakeDestination, Mockito.times(2)).downVote();
//        Assert.assertEquals(-1, fakeDestination.getVotes());
//    }
//
//    @Test
//    public void testDownThenUp() {
//        Destination fakeDestination = Mockito.mock(Destination.class);
//
//        fakeDestination.downVote();
//        fakeDestination.upVote();
//
//        Mockito.verify(fakeDestination, Mockito.times(1)).downVote();
//        Mockito.verify(fakeDestination, Mockito.times(2)).upVote();
//        Assert.assertEquals(1, fakeDestination.getVotes());
//    }
    
}