package codeu.controller;

import codeu.model.data.Destination;
import codeu.model.store.basic.DestinationStore;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;
import com.google.appengine.api.datastore.Text;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
    public void testDoGet() {
        List<Destination> fakeRankedDestinationList = new ArrayList<>();
        fakeRankedDestinationList.add(
                new Destination(UUID.randomUUID(), UUID.randomUUID(), "location1", Instant.now(), new Text(""), 5));
        fakeRankedDestinationList.add(
                new Destination(UUID.randomUUID(), UUID.randomUUID(), "location2", Instant.now(), new Text(""), 0));
        fakeRankedDestinationList.add(
                new Destination(UUID.randomUUID(), UUID.randomUUID(), "location3", Instant.now(), new Text(""), -2));
        Mockito.when(mockDestinationStore.getRankedDestinations()).thenReturn(fakeRankedDestinationList);
    }

    @Test
    public void testDoPost_UserNotLoggedIn() throws IOException, ServletException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

        rankingsServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockDestinationStore, Mockito.never())
                .addDestination(Mockito.any(Destination.class));
        Mockito.verify(mockResponse).sendRedirect("/index");
    }

    @Test
    public void testDoPost_InvalidUser() throws IOException, ServletException {
        Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
        Mockito.when(mockUserStore.getUser("test_username")).thenReturn(null);

        rankingsServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockDestinationStore, Mockito.never())
                .addDestination(Mockito.any(Destination.class));
        Mockito.verify(mockResponse).sendRedirect("/rankingPage");
    }

    //TODO: make sure votes work
    //TODO: make sure user only able to vote once
    
}