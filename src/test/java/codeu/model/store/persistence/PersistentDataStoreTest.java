package codeu.model.store.persistence;

import codeu.model.data.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PersistentDataStore. The PersistentDataStore class relies on DatastoreService,
 * which in turn relies on being deployed in an AppEngine context. Since this test doesn't run in
 * AppEngine, we use LocalServiceTestHelper to do all of the AppEngine setup so we can test. More
 * info: https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
public class PersistentDataStoreTest {

  private PersistentDataStore persistentDataStore;
  private final LocalServiceTestHelper appEngineTestHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setup() {
    appEngineTestHelper.setUp();
    persistentDataStore = new PersistentDataStore();
  }

  @After
  public void tearDown() {
    appEngineTestHelper.tearDown();
  }

  @Test
  public void testSaveAndLoadUsers() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    String nameOne = "test_username_one";
    String passwordHashOne = "$2a$10$BNte6sC.qoL4AVjO3Rk8ouY6uFaMnsW8B9NjtHWaDNe8GlQRPRT1S";
    Instant creationOne = Instant.ofEpochMilli(1000);
    User inputUserOne = new User(idOne, nameOne, passwordHashOne, creationOne);

    UUID idTwo = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String nameTwo = "test_username_two";
    String passwordHashTwo = "$2a$10$ttaMOMMGLKxBBuTN06VPvu.jVKif.IczxZcXfLcqEcFi1lq.sLb6i";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    User inputUserTwo = new User(idTwo, nameTwo, passwordHashTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputUserOne);
    persistentDataStore.writeThrough(inputUserTwo);

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what we saved matches what we loaded
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(idOne, resultUserOne.getId());
    Assert.assertEquals(nameOne, resultUserOne.getName());
    Assert.assertEquals(passwordHashOne, resultUserOne.getPasswordHash());
    Assert.assertEquals(creationOne, resultUserOne.getCreationTime());

    User resultUserTwo = resultUsers.get(1);
    Assert.assertEquals(idTwo, resultUserTwo.getId());
    Assert.assertEquals(nameTwo, resultUserTwo.getName());
    Assert.assertEquals(passwordHashTwo, resultUserTwo.getPasswordHash());
    Assert.assertEquals(creationTwo, resultUserTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadConversations() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID ownerOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID destinationOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String titleOne = "Test_Title";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Conversation inputConversationOne = new Conversation(idOne, ownerOne, destinationOne, titleOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID ownerTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    UUID destinationTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String titleTwo = "Test_Title_Two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Conversation inputConversationTwo = new Conversation(idTwo, ownerTwo, destinationTwo, titleTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputConversationOne);
    persistentDataStore.writeThrough(inputConversationTwo);

    // load
    List<Conversation> resultConversations = persistentDataStore.loadConversations();

    // confirm that what we saved matches what we loaded
    Conversation resultConversationOne = resultConversations.get(0);
    Assert.assertEquals(idOne, resultConversationOne.getId());
    Assert.assertEquals(ownerOne, resultConversationOne.getOwnerId());
    Assert.assertEquals(destinationOne, resultConversationOne.getDestinationId());
    Assert.assertEquals(titleOne, resultConversationOne.getTitle());
    Assert.assertEquals(creationOne, resultConversationOne.getCreationTime());

    Conversation resultConversationTwo = resultConversations.get(1);
    Assert.assertEquals(idTwo, resultConversationTwo.getId());
    Assert.assertEquals(ownerTwo, resultConversationTwo.getOwnerId());
    Assert.assertEquals(destinationTwo, resultConversationTwo.getDestinationId());
    Assert.assertEquals(titleTwo, resultConversationTwo.getTitle());
    Assert.assertEquals(creationTwo, resultConversationTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadMessages() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID conversationOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID authorOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Character type = 'm';
    Message inputMessageOne =
        new Message(idOne, conversationOne, authorOne, contentOne, creationOne, type);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID conversationTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    UUID authorTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String contentTwo = "test content one";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Character type2 = 'm';
    Message inputMessageTwo =
        new Message(idTwo, conversationTwo, authorTwo, contentTwo, creationTwo, type2);

    // save
    persistentDataStore.writeThrough(inputMessageOne);
    persistentDataStore.writeThrough(inputMessageTwo);

    // load
    List<Message> resultMessages = persistentDataStore.loadMessages();

    // confirm that what we saved matches what we loaded
    Message resultMessageOne = resultMessages.get(0);
    Assert.assertEquals(idOne, resultMessageOne.getId());
    Assert.assertEquals(conversationOne, resultMessageOne.getConversationId());
    Assert.assertEquals(authorOne, resultMessageOne.getAuthorId());
    Assert.assertEquals(contentOne, resultMessageOne.getContent());
    Assert.assertEquals(creationOne, resultMessageOne.getCreationTime());
    Assert.assertEquals(type, resultMessageOne.getType());

    Message resultMessageTwo = resultMessages.get(1);
    Assert.assertEquals(idTwo, resultMessageTwo.getId());
    Assert.assertEquals(conversationTwo, resultMessageTwo.getConversationId());
    Assert.assertEquals(authorTwo, resultMessageTwo.getAuthorId());
    Assert.assertEquals(contentTwo, resultMessageTwo.getContent());
    Assert.assertEquals(creationTwo, resultMessageTwo.getCreationTime());
    Assert.assertEquals(type2, resultMessageTwo.getType());
  }
  
  @Test
  public void testSaveAndLoadAboutMes() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID ownerOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    AboutMe inputAboutMeOne =
        new AboutMe(idOne, ownerOne, contentOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID ownerTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String contentTwo = "test content two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    AboutMe inputAboutMeTwo =
        new AboutMe(idTwo, ownerTwo, contentTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputAboutMeOne);
    persistentDataStore.writeThrough(inputAboutMeTwo);

    // load
    List<AboutMe> resultAboutMes = persistentDataStore.loadAboutMes();

    // confirm that what we saved matches what we loaded
    AboutMe resultAboutMeOne = resultAboutMes.get(0);
    Assert.assertEquals(idOne, resultAboutMeOne.getId());
    Assert.assertEquals(ownerOne, resultAboutMeOne.getOwner());
    Assert.assertEquals(contentOne, resultAboutMeOne.getContent());
    Assert.assertEquals(creationOne, resultAboutMeOne.getCreationTime());

    AboutMe resultAboutMeTwo = resultAboutMes.get(1);
    Assert.assertEquals(idTwo, resultAboutMeTwo.getId());
    Assert.assertEquals(ownerTwo, resultAboutMeTwo.getOwner());
    Assert.assertEquals(contentTwo, resultAboutMeTwo.getContent());
    Assert.assertEquals(creationTwo, resultAboutMeTwo.getCreationTime());
  }
  
  @Test
  public void testSaveAndLoadStatusUpdates() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID authorOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    StatusUpdate inputStatusUpdateOne =
        new StatusUpdate(idOne, authorOne, contentOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID authorTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String contentTwo = "test content two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    StatusUpdate inputStatusUpdateTwo =
        new StatusUpdate(idTwo, authorTwo, contentTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputStatusUpdateOne);
    persistentDataStore.writeThrough(inputStatusUpdateTwo);

    // load
    List<StatusUpdate> resultStatusUpdates = persistentDataStore.loadStatusUpdates();

    // confirm that what we saved matches what we loaded
    StatusUpdate resultStatusUpdateOne = resultStatusUpdates.get(0);
    Assert.assertEquals(idOne, resultStatusUpdateOne.getId());
    Assert.assertEquals(authorOne, resultStatusUpdateOne.getAuthorId());
    Assert.assertEquals(contentOne, resultStatusUpdateOne.getContent());
    Assert.assertEquals(creationOne, resultStatusUpdateOne.getCreationTime());

    StatusUpdate resultStatusUpdateTwo = resultStatusUpdates.get(1);
    Assert.assertEquals(idTwo, resultStatusUpdateTwo.getId());
    Assert.assertEquals(authorTwo, resultStatusUpdateTwo.getAuthorId());
    Assert.assertEquals(contentTwo, resultStatusUpdateTwo.getContent());
    Assert.assertEquals(creationTwo, resultStatusUpdateTwo.getCreationTime());
  }
  
  @Test
  public void testSaveAndLoadUserActions() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID userOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String messageOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    UserAction inputUserActionOne =
        new UserAction(idOne, userOne, messageOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID userTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String messageTwo = "test content two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    UserAction inputUserActionTwo =
        new UserAction(idTwo, userTwo, messageTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputUserActionOne);
    persistentDataStore.writeThrough(inputUserActionTwo);

    // load
    List<UserAction> resultUserActions = persistentDataStore.loadUserActions();

    // confirm that what we saved matches what we loaded
    UserAction resultUserActionOne = resultUserActions.get(1);
    Assert.assertEquals(idOne, resultUserActionOne.getId());
    Assert.assertEquals(userOne, resultUserActionOne.getUserId());
    Assert.assertEquals(messageOne, resultUserActionOne.getMessage());
    Assert.assertEquals(creationOne, resultUserActionOne.getCreationTime());

    UserAction resultUserActionTwo = resultUserActions.get(0);
    Assert.assertEquals(idTwo, resultUserActionTwo.getId());
    Assert.assertEquals(userTwo, resultUserActionTwo.getUserId());
    Assert.assertEquals(messageTwo, resultUserActionTwo.getMessage());
    Assert.assertEquals(creationTwo, resultUserActionTwo.getCreationTime());
  }
  
  @Test
  public void testSaveAndLoadDestinations() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID userOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String titleOne = "test title one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Destination inputDestinationOne =
        new Destination(idOne, userOne, titleOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID userTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String titleTwo = "test title two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Destination inputDestinationTwo =
        new Destination(idTwo, userTwo, titleTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputDestinationOne);
    persistentDataStore.writeThrough(inputDestinationTwo);

    // load
    List<Destination> resultDestinations = persistentDataStore.loadDestinations();

    // confirm that what we saved matches what we loaded
    Destination resultDestinationOne = resultDestinations.get(0);
    Assert.assertEquals(idOne, resultDestinationOne.getId());
    Assert.assertEquals(userOne, resultDestinationOne.getOwnerId());
    Assert.assertEquals(titleOne, resultDestinationOne.getTitle());
    Assert.assertEquals(creationOne, resultDestinationOne.getCreationTime());

    Destination resultDestinationTwo = resultDestinations.get(1);
    Assert.assertEquals(idTwo, resultDestinationTwo.getId());
    Assert.assertEquals(userTwo, resultDestinationTwo.getOwnerId());
    Assert.assertEquals(titleTwo, resultDestinationTwo.getTitle());
    Assert.assertEquals(creationTwo, resultDestinationTwo.getCreationTime());

  }

  @Test
  public void testSaveAndLoadImages() throws PersistentDataStoreException {
    String filename = "test";
    String destination = "test";
    UUID id = UUID.randomUUID();
    Instant now = Instant.ofEpochMilli(1000);
    Image image = new Image(filename, destination, id, now);

    String filename2 = "random";
    String destination2 = "random";
    UUID id2 = UUID.randomUUID();
    Instant now2 = Instant.ofEpochMilli(2000);
    Image image2 = new Image(filename2, destination2, id2, now2);

    // save
    persistentDataStore.writeThrough(image);
    persistentDataStore.writeThrough(image2);

    // load
    List<Image> resultImages = persistentDataStore.loadImages();

    // confirm that what we saved matches what we loaded
    Image resultImageOne = resultImages.get(0);
    Assert.assertEquals(filename, resultImageOne.returnFilename());
    Assert.assertEquals(destination, resultImageOne.returnDestination());
    Assert.assertEquals(id, resultImageOne.getId());
    Assert.assertEquals(now, resultImageOne.getCreation());

    Image resultImageTwo = resultImages.get(1);
    Assert.assertEquals(filename2, resultImageTwo.returnFilename());
    Assert.assertEquals(destination2, resultImageTwo.returnDestination());
    Assert.assertEquals(id2, resultImageTwo.getId());
    Assert.assertEquals(now2, resultImageTwo.getCreation());

  }
}
