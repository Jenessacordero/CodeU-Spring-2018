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
import codeu.model.data.StatusUpdate;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.StatusUpdateStore;
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

public class ActivityFeedServletTest {

  private ActivityFeedServlet activityFeedServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;
  private UserActionStore mockUserActionStore;
  private StatusUpdateStore mockStatusUpdateStore;
private User mockUser;

  @Before
  public void setup() {
    activityFeedServlet = new ActivityFeedServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
        .thenReturn(mockRequestDispatcher);


    mockUserStore = Mockito.mock(UserStore.class);
    activityFeedServlet.setUserStore(mockUserStore);
    
    mockUserActionStore = Mockito.mock(UserActionStore.class);
    activityFeedServlet.setUserActionStore(mockUserActionStore);
    
    mockStatusUpdateStore = Mockito.mock(StatusUpdateStore.class);
    activityFeedServlet.setStatusUpdateStore(mockStatusUpdateStore);
    
    mockUser = Mockito.mock(User.class);
  }

  /**
   * Tests the doGet of the servlet when a user loads the page.
   * @throws IOException
   * @throws ServletException
   */
  @Test
  public void testDoGet() throws IOException, ServletException {

    List<UserAction> fakeUserActions = new ArrayList<>();
    fakeUserActions.add(
        new UserAction(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test message",
            Instant.now()));
    Mockito.when(mockUserActionStore.returnAllUserActions())
        .thenReturn(fakeUserActions);

    activityFeedServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("userActions", fakeUserActions);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
  
  /**
   * Tests when a user presses the submit button.
   * @throws IOException
   * @throws ServletException
   */
  @Test
  public void testDoPost() throws IOException, ServletException {

	User fakeUser =
        new User(
        	UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");
    

    Mockito.when(mockRequest.getParameter("status-update")).thenReturn("Test status-update.");
    


    activityFeedServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<StatusUpdate> statusUpdateArgumentCaptor = ArgumentCaptor.forClass(StatusUpdate.class);
    Mockito.verify(mockStatusUpdateStore).addStatusUpdate(statusUpdateArgumentCaptor.capture());
    Assert.assertEquals("Test status-update.", statusUpdateArgumentCaptor.getValue().getContent());
    
    ArgumentCaptor<UserAction> userActionArgumentCaptor = ArgumentCaptor.forClass(UserAction.class);
    Mockito.verify(mockUserActionStore).addUserAction(userActionArgumentCaptor.capture());

    Mockito.verify(mockResponse).sendRedirect("/activityfeed");
  }


}
