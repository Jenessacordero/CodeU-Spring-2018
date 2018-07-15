package codeu.model.store.persistence;

import codeu.model.data.Conversation;
import codeu.model.data.Destination;
import codeu.model.data.Message;
import codeu.model.data.StatusUpdate;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.data.AboutMe;
import codeu.model.data.Image;
import java.time.Instant;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Contains tests of the PersistentStorageAgent class. Currently that class is just a pass-through
 * to PersistentDataStore, so these tests are pretty trivial. If you modify how
 * PersistentStorageAgent writes to PersistentDataStore, or if you swap out the backend to something
 * other than PersistentDataStore, then modify these tests.
 */
public class PersistentStorageAgentTest {

  private PersistentDataStore mockPersistentDataStore;
  private PersistentStorageAgent persistentStorageAgent;

  @Before
  public void setup() {
    mockPersistentDataStore = Mockito.mock(PersistentDataStore.class);
    persistentStorageAgent = PersistentStorageAgent.getTestInstance(mockPersistentDataStore);
  }

  @Test
  public void testLoadUsers() throws PersistentDataStoreException {
    persistentStorageAgent.loadUsers();
    Mockito.verify(mockPersistentDataStore).loadUsers();
  }

  @Test
  public void testLoadConversations() throws PersistentDataStoreException {
    persistentStorageAgent.loadConversations();
    Mockito.verify(mockPersistentDataStore).loadConversations();
  }

  @Test
  public void testLoadMessages() throws PersistentDataStoreException {
    persistentStorageAgent.loadMessages();
    Mockito.verify(mockPersistentDataStore).loadMessages();
  }
  
  @Test
  public void testLoadAboutMes() throws PersistentDataStoreException {
    persistentStorageAgent.loadAboutMes();
    Mockito.verify(mockPersistentDataStore).loadAboutMes();
  }
  
  @Test
  public void testLoadStatusUpdates() throws PersistentDataStoreException {
    persistentStorageAgent.loadStatusUpdates();
    Mockito.verify(mockPersistentDataStore).loadStatusUpdates();
  }
  
  @Test
  public void testLoadUserActions() throws PersistentDataStoreException {
    persistentStorageAgent.loadUserActions();
    Mockito.verify(mockPersistentDataStore).loadUserActions();
  }
  
  @Test
  public void testLoadDestinations() throws PersistentDataStoreException {
    persistentStorageAgent.loadDestinations();
    Mockito.verify(mockPersistentDataStore).loadDestinations();
  }

  @Test
  public void testLoadImage() throws PersistentDataStoreException {
    persistentStorageAgent.loadImages();
    Mockito.verify(mockPersistentDataStore).loadImages();
  }

  @Test
  public void testWriteThroughUser() {
    User user =
        new User(
            UUID.randomUUID(),
            "test_username",
            "$2a$10$5GNCbSPS1sqqM9.hdiE2hexn1w.vnNoR.CaHIztFEhdAD7h82tqX.",
            Instant.now());
    persistentStorageAgent.writeThrough(user);
    Mockito.verify(mockPersistentDataStore).writeThrough(user);
  }

  @Test
  public void testWriteThroughConversation() {
    Conversation conversation =
        new Conversation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now());
    persistentStorageAgent.writeThrough(conversation);
    Mockito.verify(mockPersistentDataStore).writeThrough(conversation);
  }

  @Test
  public void testWriteThroughMessage() {
    Message message =
        new Message(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "test content", Instant.now(), 'm');
    persistentStorageAgent.writeThrough(message);
    Mockito.verify(mockPersistentDataStore).writeThrough(message);
  }
  
  @Test
  public void testWriteThroughAboutMe() throws PersistentDataStoreException {
    AboutMe aboutMe =
        new AboutMe(
            UUID.randomUUID(), UUID.randomUUID(), "test bio", Instant.now());
    persistentStorageAgent.writeThrough(aboutMe);
    Mockito.verify(mockPersistentDataStore).writeThrough(aboutMe);
  }
  
  @Test
  public void testWriteThroughStatusUpdate() throws PersistentDataStoreException {
    StatusUpdate statusUpdate =
        new StatusUpdate(
            UUID.randomUUID(), UUID.randomUUID(), "test update", Instant.now());
    persistentStorageAgent.writeThrough(statusUpdate);
    Mockito.verify(mockPersistentDataStore).writeThrough(statusUpdate);
  }
  
  @Test
  public void testWriteThroughUserAction() throws PersistentDataStoreException {
    UserAction userAction =
        new UserAction(
            UUID.randomUUID(), UUID.randomUUID(), "test action", Instant.now());
    persistentStorageAgent.writeThrough(userAction);
    Mockito.verify(mockPersistentDataStore).writeThrough(userAction);
  }
  
  @Test
  public void testWriteThroughDestination() throws PersistentDataStoreException {
    Destination destination =
        new Destination(
            UUID.randomUUID(), UUID.randomUUID(), "test title", Instant.now());
    persistentStorageAgent.writeThrough(destination);
    Mockito.verify(mockPersistentDataStore).writeThrough(destination);
  }

  @Test
  public void testWriteThroughImage() throws PersistentDataStoreException {
    Image testImage = new Image("test", "test", UUID.randomUUID(), Instant.now());
    persistentStorageAgent.writeThrough(testImage);
    Mockito.verify(mockPersistentDataStore).writeThrough(testImage);
  }
}
