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
import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.data.AboutMe;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.AboutMeStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
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

public class ProfileServletTest {

  private ProfileServlet profileServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;
  private AboutMeStore mockAboutMeStore;

  @Before
  public void setup() {
    profileServlet = new ProfileServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockMessageStore = Mockito.mock(MessageStore.class);
    profileServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    profileServlet.setUserStore(mockUserStore);
    
    mockAboutMeStore = Mockito.mock(AboutMeStore.class);
    profileServlet.setAboutMeStore(mockAboutMeStore);
  	}
  
  /**
   * Simple test for the doGet method.
   * @throws IOException
   * @throws ServletException
   */
  @Test
  public void testDoGet() throws IOException, ServletException {
	  UUID userId;
	User user =
		        new User(
		            userId = UUID.randomUUID(),
		            "test username",
		            "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
		            Instant.now());
	
	 LinkedList<Message> fakeMessageList = new LinkedList<>();

	 fakeMessageList.add(
		        new Message(
		            UUID.randomUUID(),
		            UUID.randomUUID(),
		            userId,
		            "test content",
		            Instant.now()));
	 	 

	 AboutMe fakeAboutMe = new AboutMe(
		            UUID.randomUUID(),
		            userId,
		            "test content",
		            Instant.now());
	

	 Mockito.when(mockRequest.getRequestURI()).thenReturn("/user/test username");
	 Mockito.when(mockUserStore.getUser("test username")).thenReturn(user);
	 Mockito.when(mockMessageStore.getMessagesByUser(userId))
	 .thenReturn(fakeMessageList);
	 Mockito.when(mockAboutMeStore.getAboutMeByUser(userId))
	 .thenReturn(fakeAboutMe);

	 HttpSession mockSession = Mockito.mock(HttpSession.class);
	 Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
		    
	 profileServlet.doGet(mockRequest, mockResponse);
    
    Mockito.verify(mockRequest).setAttribute("user", user);
    Mockito.verify(mockRequest).setAttribute("messages", fakeMessageList);
    Mockito.verify(mockRequest).setAttribute("aboutMe", fakeAboutMe);



    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
  
  /**
   * Simple test to see if doPost stores the AboutMe.
   * @throws IOException
   * @throws ServletException
   * @throws PersistentDataStoreException
   */
  @Test
  public void testDoPost_StoresAboutMe() throws IOException, ServletException, PersistentDataStoreException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/user/test_username");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

	User fakeUser =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy",
            Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);


    Mockito.when(mockRequest.getParameter("aboutme")).thenReturn("Test bio.");

    profileServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<AboutMe> aboutMeArgumentCaptor = ArgumentCaptor.forClass(AboutMe.class);
    Mockito.verify(mockAboutMeStore).addAboutMe(aboutMeArgumentCaptor.capture());
    Assert.assertEquals("Test bio.", aboutMeArgumentCaptor.getValue().getContent());

    Mockito.verify(mockResponse).sendRedirect("/user/test_username");
  }

  /**
   * Simple test to see if doPost cleans up html content.
   * @throws IOException
   * @throws ServletException
   * @throws PersistentDataStoreException
   */
  @Test
  public void testDoPost_CleansHtmlContent() throws IOException, ServletException, PersistentDataStoreException {
    Mockito.when(mockRequest.getRequestURI()).thenReturn("/user/test_username");
    Mockito.when(mockSession.getAttribute("user")).thenReturn("test_username");

    User fakeUser = new User(UUID.randomUUID(), "test_username", "test_username", Instant.now());
    Mockito.when(mockUserStore.getUser("test_username")).thenReturn(fakeUser);


    Mockito.when(mockRequest.getParameter("aboutme"))
        .thenReturn("Contains <b>html</b> and <script>JavaScript</script> content.");

    profileServlet.doPost(mockRequest, mockResponse);

    ArgumentCaptor<AboutMe> aboutMeArgumentCaptor = ArgumentCaptor.forClass(AboutMe.class);
    Mockito.verify(mockAboutMeStore).addAboutMe(aboutMeArgumentCaptor.capture());
    Assert.assertEquals(
        "Contains html and  content.", aboutMeArgumentCaptor.getValue().getContent());

    Mockito.verify(mockResponse).sendRedirect("/user/test_username");
  }
  }