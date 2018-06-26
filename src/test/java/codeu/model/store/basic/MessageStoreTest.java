package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class MessageStoreTest {

  private MessageStore messageStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UUID CONVERSATION_ID_ONE = UUID.randomUUID();
  private final Message MESSAGE_ONE =
      new Message(
          UUID.randomUUID(),
          CONVERSATION_ID_ONE,
          UUID.randomUUID(),
          "message one",
          Instant.ofEpochMilli(1000), 'm');
  private final Message MESSAGE_TWO =
      new Message(
          UUID.randomUUID(),
          CONVERSATION_ID_ONE,
          UUID.randomUUID(),
          "message two",
          Instant.ofEpochMilli(2000), 'm');
  private final Message MESSAGE_THREE =
      new Message(
          UUID.randomUUID(),
          UUID.randomUUID(),
          UUID.randomUUID(),
          "message three",
          Instant.ofEpochMilli(3000), 'm');

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    messageStore = MessageStore.getTestInstance(mockPersistentStorageAgent);

    final HashMap<UUID, LinkedList<Message>> messageListbyConvo = new HashMap<>();
    LinkedList temp1 = new LinkedList();
    LinkedList temp2 = new LinkedList();
    LinkedList temp3 = new LinkedList();
    temp1.add(MESSAGE_ONE);
    temp1.add(MESSAGE_TWO);
    temp3.add(MESSAGE_THREE);
    messageListbyConvo.put(MESSAGE_ONE.getConversationId(), temp1);
    messageListbyConvo.put(MESSAGE_THREE.getConversationId(), temp3);

    final HashMap<UUID, LinkedList<Message>> messageListByUser = new HashMap<>();
    messageListByUser.put(MESSAGE_ONE.getAuthorId(), temp1);
    messageListByUser.put(MESSAGE_TWO.getAuthorId(), temp2);
    messageListByUser.put(MESSAGE_THREE.getAuthorId(), temp3);
    messageStore.setMessages(messageListbyConvo, messageListByUser);
  }

  @Test
  public void testGetMessagesInConversation() {
    List<Message> resultMessages = messageStore.getMessagesInConversation(CONVERSATION_ID_ONE);

    Assert.assertEquals(2, resultMessages.size());
    assertEquals(MESSAGE_ONE, resultMessages.get(0));
    assertEquals(MESSAGE_TWO, resultMessages.get(1));
  }

  @Test
  public void testAddMessage() {
    UUID inputConversationId = UUID.randomUUID();
    Message inputMessage =
        new Message(
            UUID.randomUUID(),
            inputConversationId,
            UUID.randomUUID(),
            "test message",
            Instant.now(), 'm');

    messageStore.addMessage(inputMessage);
    Message resultMessage = messageStore.getMessagesInConversation(inputConversationId).getLast();

    assertEquals(inputMessage, resultMessage);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputMessage);
  }

  private void assertEquals(Message expectedMessage, Message actualMessage) {
    Assert.assertEquals(expectedMessage.getId(), actualMessage.getId());
    Assert.assertEquals(expectedMessage.getConversationId(), actualMessage.getConversationId());
    Assert.assertEquals(expectedMessage.getAuthorId(), actualMessage.getAuthorId());
    Assert.assertEquals(expectedMessage.getContent(), actualMessage.getContent());
    Assert.assertEquals(expectedMessage.getCreationTime(), actualMessage.getCreationTime());
  }
}
