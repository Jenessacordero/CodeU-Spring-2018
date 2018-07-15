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
import codeu.model.data.Images;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.DestinationStore;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.UploadedImagesStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
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

public class DestinationPageServletTest {

  private DestinationPageServlet destinationPageServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ConversationStore mockConversationStore;
  private DestinationStore mockDestinationStore;
  private UserStore mockUserStore;
  private UserActionStore mockUserActionStore;
  private UploadedImagesStore mockImageStore;

  @Before
  public void setup() {
      destinationPageServlet = new DestinationPageServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/destinationPage.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    destinationPageServlet.setConversationStore(mockConversationStore);
    
    mockDestinationStore = Mockito.mock(DestinationStore.class);
    destinationPageServlet.setDestinationStore(mockDestinationStore);

    mockUserStore = Mockito.mock(UserStore.class);
    destinationPageServlet.setUserStore(mockUserStore);
    
    mockUserActionStore = Mockito.mock(UserActionStore.class);
    destinationPageServlet.setUserActionStore(mockUserActionStore);

    mockImageStore = Mockito.mock(UploadedImagesStore.class);
    destinationPageServlet.setImageStore(mockImageStore);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
	UUID fakeDestinationId = UUID.randomUUID();
	Mockito.when(mockRequest.getRequestURI()).thenReturn("/destination/test_destination");
    Mockito.when(mockRequest.getParameter("destinationTitle")).thenReturn("test_destination");
    
    Destination fakeDestination =
            new Destination(fakeDestinationId, UUID.randomUUID(), "test_destination", Instant.now());
        Mockito.when(mockDestinationStore.getDestinationWithTitle("test_destination"))
            .thenReturn(fakeDestination);
        
    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(UUID.randomUUID(), UUID.randomUUID(), fakeDestinationId, "test_conversation", Instant.now()));
    Mockito.when(mockConversationStore.getConvosInDestination(fakeDestinationId)).thenReturn(fakeConversationList);

    List<Images> fakeImageList = new ArrayList<>();
    fakeImageList.add(
            new Images("random", "random_destination", UUID.randomUUID()));
    Mockito.when(mockImageStore.returnAllImages()).thenReturn(fakeImageList);

    destinationPageServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("conversations", fakeConversationList);
    Mockito.verify(mockRequest).setAttribute("destinationTitle", "test_destination");
    Mockito.verify(mockRequest).setAttribute("images", fakeImageList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_UserNotLoggedIn() throws IOException, ServletException {
	  UUID fakeDestinationId = UUID.randomUUID();
		Mockito.when(mockRequest.getRequestURI()).thenReturn("/destination/test_destination");
	    Mockito.when(mockRequest.getParameter("destinationTitle")).thenReturn("test_destination");
	    
	    Destination fakeDestination =
	            new Destination(fakeDestinationId, UUID.randomUUID(), "test_destination", Instant.now());
	        Mockito.when(mockDestinationStore.getDestinationWithTitle("test_destination"))
	            .thenReturn(fakeDestination);
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    destinationPageServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoPost_InvalidUser() throws IOException, ServletException {
	  UUID fakeDestinationId = UUID.randomUUID();
		Mockito.when(mockRequest.getRequestURI()).thenReturn("/destination/test_destination");
	    Mockito.when(mockRequest.getParameter("destinationTitle")).thenReturn("test_destination");
	    
	    Destination fakeDestination =
	            new Destination(fakeDestinationId, UUID.randomUUID(), "test_destination", Instant.now());
	        Mockito.when(mockDestinationStore.getDestinationWithTitle("test_destination"))
	            .thenReturn(fakeDestination);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(null);

    destinationPageServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoPost_BadConversationName() throws IOException, ServletException {
	  UUID fakeDestinationId = UUID.randomUUID();
		Mockito.when(mockRequest.getRequestURI()).thenReturn("/destination/test_destination");
	    Mockito.when(mockRequest.getParameter("destinationTitle")).thenReturn("test_destination");
	    
	    Destination fakeDestination =
	            new Destination(fakeDestinationId, UUID.randomUUID(), "test_destination", Instant.now());
	        Mockito.when(mockDestinationStore.getDestinationWithTitle("test_destination"))
	            .thenReturn(fakeDestination);
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn("bad !@#$% name");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser = new User(UUID.randomUUID(), "test_username", "test_username", Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    destinationPageServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockRequest).setAttribute("error", "Please enter only letters and numbers.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_ConversationNameTaken() throws IOException, ServletException {
	  UUID fakeDestinationId = UUID.randomUUID();
		Mockito.when(mockRequest.getRequestURI()).thenReturn("/destination/test_destination");
	    Mockito.when(mockRequest.getParameter("destinationTitle")).thenReturn("test_destination");
	    
	    Destination fakeDestination =
	            new Destination(fakeDestinationId, UUID.randomUUID(), "test_destination", Instant.now());
	        Mockito.when(mockDestinationStore.getDestinationWithTitle("test_destination"))
	            .thenReturn(fakeDestination);
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn("test_conversation");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser = new User(UUID.randomUUID(), "test_username", "test_username", Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Mockito.when(mockConversationStore.isTitleTaken("test_conversation")).thenReturn(true);

    destinationPageServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockConversationStore, Mockito.never())
        .addConversation(Mockito.any(Conversation.class));
    Mockito.verify(mockResponse).sendRedirect("/chat/test_destination/test_conversation");
  }

  @Test
  public void testDoPost_NewConversation() throws IOException, ServletException {
	  Mockito.when(mockRequest.getRequestURI()).thenReturn("/destination/test_destination");
    Mockito.when(mockRequest.getParameter("destinationTitle")).thenReturn("test_destination");
    
    Destination fakeDestination =
            new Destination(UUID.randomUUID(), UUID.randomUUID(), "test_destination", Instant.now());
        Mockito.when(mockDestinationStore.getDestinationWithTitle("test_destination"))
            .thenReturn(fakeDestination);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    Mockito.when(mockRequest.getParameter("conversationTitle")).thenReturn("test_conversation");

    User fakeUser = new User(UUID.randomUUID(), "test_username", "test_username", Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);

    Mockito.when(mockConversationStore.isTitleTaken("test_conversation")).thenReturn(false);

    destinationPageServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<Conversation> conversationArgumentCaptor =
        ArgumentCaptor.forClass(Conversation.class);
    Mockito.verify(mockConversationStore).addConversation(conversationArgumentCaptor.capture());
    Assert.assertEquals(conversationArgumentCaptor.getValue().getTitle(), "test_conversation");
    
    ArgumentCaptor<UserAction> userActionArgumentCaptor = ArgumentCaptor.forClass(UserAction.class);
    Mockito.verify(mockUserActionStore).addUserAction(userActionArgumentCaptor.capture());

    Mockito.verify(mockResponse).sendRedirect("/chat/test_destination/test_conversation");
  }
}
