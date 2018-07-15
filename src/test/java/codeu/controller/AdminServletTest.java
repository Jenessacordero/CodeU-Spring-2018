package codeu.controller;

import codeu.model.data.User;

import java.io.IOException;
import java.util.UUID;
import java.time.Instant;

import codeu.model.store.basic.UserStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;



public class AdminServletTest {

    private AdminPageServlet adminPageServlet;
    private HttpServletRequest mockRequest;
    private HttpSession mockSession;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private UserStore mockUserStore;


    @Before
    public void setup() {
        adminPageServlet = new AdminPageServlet();

        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/adminpage.jsp"))
                .thenReturn(mockRequestDispatcher);

        mockUserStore = Mockito.mock(UserStore.class);
        adminPageServlet.setUserStore(mockUserStore);
    }

    @Test
    public void testNewestUser() {
        UUID id = UUID.randomUUID();
        String name = "test_username";
        String passwordHash = "testPass";
        Instant creation = Instant.now();

        User user = new User(id, name, passwordHash, creation);

    }

}
