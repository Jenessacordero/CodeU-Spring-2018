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
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.DestinationStore;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class DestinationsServletTest {

  private DestinationsServlet destinationsServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ConversationStore mockConversationStore;
  private DestinationStore mockDestinationStore;
  private UserStore mockUserStore;
  private UserActionStore mockUserActionStore;

  @Before
  public void setup() {
	  destinationsServlet = new DestinationsServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/destinations.jsp"))
        .thenReturn(mockRequestDispatcher);
    
    mockDestinationStore = Mockito.mock(DestinationStore.class);
    destinationsServlet.setDestinationStore(mockDestinationStore);

    mockUserStore = Mockito.mock(UserStore.class);
    destinationsServlet.setUserStore(mockUserStore);
    
    mockUserActionStore = Mockito.mock(UserActionStore.class);
    destinationsServlet.setUserActionStore(mockUserActionStore);
  }

//  @Test
//  public void testDoGet() throws IOException, ServletException {
//
//    List<Destination> fakeDestinationList = new ArrayList<>();
//    fakeDestinationList.add(
//        new Destination(UUID.randomUUID(), UUID.randomUUID(), "test_destination", Instant.now(), ""));
//
//    Mockito.when(mockDestinationStore.getAllDestinations()).thenReturn(fakeDestinationList);
//
//    destinationsServlet.doGet(mockRequest, mockResponse);
//
//    Mockito.verify(mockRequest).setAttribute("destinations", fakeDestinationList);
//    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
//  }

  @Test
  public void testDoPost_UserNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    destinationsServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockDestinationStore, Mockito.never())
        .addDestination(Mockito.any(Destination.class));
    Mockito.verify(mockResponse).sendRedirect("/index");
  }

  @Test
  public void testDoPost_InvalidUser() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(null);

    destinationsServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockDestinationStore, Mockito.never())
        .addDestination(Mockito.any(Destination.class));
    Mockito.verify(mockResponse).sendRedirect("/destinations");
  }


  @Test
  public void testDoPost_DestinationNameTaken() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("destinationTitle")).thenReturn("test_destination");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser = new User(UUID.randomUUID(), "test_username", "test_username", Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Mockito.when(mockDestinationStore.isTitleTaken("test_destination")).thenReturn(true);

    destinationsServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockDestinationStore, Mockito.never())
        .addDestination(Mockito.any(Destination.class));
    Mockito.verify(mockResponse).sendRedirect("/destination/test_destination");
  }

//  @Test
//  public void testDoPost_NewDestination() throws IOException, ServletException {
//    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
//    Mockito.when(mockRequest.getParameter("destinationTitle")).thenReturn("test_destination");
//
//    User fakeUser = new User(UUID.randomUUID(), "test_username", "test_username", Instant.now());
//    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);
//
//    Mockito.when(mockDestinationStore.isTitleTaken("test_destination")).thenReturn(false);
//
//    destinationsServlet.doPost(mockRequest, mockResponse);
//
//    ArgumentCaptor<Destination> destinationArgumentCaptor =
//        ArgumentCaptor.forClass(Destination.class);
//    Mockito.verify(mockDestinationStore).addDestination(destinationArgumentCaptor.capture());
//    Assert.assertEquals(destinationArgumentCaptor.getValue().getTitle(), "test_destination");
//
//    ArgumentCaptor<UserAction> userActionArgumentCaptor = ArgumentCaptor.forClass(UserAction.class);
//    Mockito.verify(mockUserActionStore).addUserAction(userActionArgumentCaptor.capture());
//
//    Mockito.verify(mockResponse).sendRedirect("/destination/test_destination");
//  }
}
