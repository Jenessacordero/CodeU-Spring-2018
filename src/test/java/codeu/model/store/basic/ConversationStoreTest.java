package codeu.model.store.basic;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConversationStoreTest {

  private ConversationStore conversationStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UUID DESTINATION_ID_ONE = UUID.randomUUID();
  private final UUID DESTINATION_ID_TWO = UUID.randomUUID();
  private final Conversation CONVERSATION_ONE =
      new Conversation(
          UUID.randomUUID(), UUID.randomUUID(), DESTINATION_ID_ONE, "conversation_one", Instant.ofEpochMilli(1000));
  
  private final Conversation CONVERSATION_TWO =
	      new Conversation(
	          UUID.randomUUID(), UUID.randomUUID(), DESTINATION_ID_TWO, "conversation_two", Instant.ofEpochMilli(1000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    conversationStore = ConversationStore.getTestInstance(mockPersistentStorageAgent);

    final List<Conversation> conversationList = new ArrayList<>();
    conversationList.add(CONVERSATION_ONE);
    conversationList.add(CONVERSATION_TWO);
    conversationStore.setConversations(conversationList);
  }

  @Test
  public void testGetConversationWithTitle_found() {
    Conversation resultConversationOne =
        conversationStore.getConversationWithTitle(CONVERSATION_ONE.getTitle());
    Conversation resultConversationTwo =
            conversationStore.getConversationWithTitle(CONVERSATION_TWO.getTitle());

    assertEquals(CONVERSATION_ONE, resultConversationOne);
    assertEquals(CONVERSATION_TWO, resultConversationTwo);
  }

  @Test
  public void testGetConversationWithTitle_notFound() {
    Conversation resultConversation = conversationStore.getConversationWithTitle("unfound_title");

    Assert.assertNull(resultConversation);
  }

  @Test
  public void testIsTitleTaken_true() {
    boolean isTitleTaken = conversationStore.isTitleTaken(CONVERSATION_ONE.getTitle());

    Assert.assertTrue(isTitleTaken);
  }

  @Test
  public void testIsTitleTaken_false() {
    boolean isTitleTaken = conversationStore.isTitleTaken("unfound_title");

    Assert.assertFalse(isTitleTaken);
  }

  @Test
  public void testAddConversation() {
    Conversation inputConversation =
        new Conversation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "test_conversation", Instant.now());

    conversationStore.addConversation(inputConversation);
    Conversation resultConversation =
        conversationStore.getConversationWithTitle("test_conversation");

    assertEquals(inputConversation, resultConversation);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputConversation);
  }
  
  @Test
  public void testGetConvosInDestination() {
	  List<Conversation> resultConversationsOne = conversationStore.getConvosInDestination(DESTINATION_ID_ONE);
	  List<Conversation> resultConversationsTwo = conversationStore.getConvosInDestination(DESTINATION_ID_TWO);

	    Assert.assertEquals(1, resultConversationsOne.size());
	    Assert.assertEquals(1, resultConversationsTwo.size());
 }

  private void assertEquals(Conversation expectedConversation, Conversation actualConversation) {
    Assert.assertEquals(expectedConversation.getId(), actualConversation.getId());
    Assert.assertEquals(expectedConversation.getOwnerId(), actualConversation.getOwnerId());
    Assert.assertEquals(expectedConversation.getDestinationId(), actualConversation.getDestinationId());
    Assert.assertEquals(expectedConversation.getTitle(), actualConversation.getTitle());
    Assert.assertEquals(
        expectedConversation.getCreationTime(), actualConversation.getCreationTime());
  }
}
