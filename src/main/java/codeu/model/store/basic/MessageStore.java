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

package codeu.model.store.basic;

import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.util.*;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class MessageStore {

  /** Singleton instance of MessageStore. */
  private static MessageStore instance;

  /**
   * Returns the singleton instance of MessageStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static MessageStore getInstance() {
    if (instance == null) {
      instance = new MessageStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static MessageStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new MessageStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Messages from and saving Messages to
   * Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Messages stored by conversation ID */
  private HashMap<UUID, LinkedList<Message>> messages;

    /** The in-memory list of Messages stored by User ID */
    private HashMap<UUID, LinkedList<Message>> UserMessages;

  public List<Message> returnAllMessages() {
    List<Message> totalMessages = new ArrayList<>();
    Set<UUID> conversationIDs = messages.keySet();
    for (UUID ids : conversationIDs) {
        LinkedList<Message> conversationMessages = messages.get(ids);
        for (Message messages : conversationMessages) {
            totalMessages.add(messages);
        }
    }
    return totalMessages;
  }

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private MessageStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    messages = new LinkedHashMap<>();
  }

  /** Add a new message to the current set of messages known to the application. */
  public void addMessage(Message message) {
    // adds messages sorted by conversationID
      LinkedList<Message> messagesInConversation = new LinkedList<>();
      if (messages.containsKey(message.getConversationId())) {
          messagesInConversation = messages.get(message.getConversationId());
      }
    messagesInConversation.add(message);
    messages.put(message.getConversationId(), messagesInConversation);

    // adds messages stored by UserID
      LinkedList<Message> messagesByUser = new LinkedList<>();
      if (UserMessages.containsKey(message.getAuthorId())) {
          messagesByUser = UserMessages.get(message.getAuthorId());
      }
      messagesByUser.add(message);
      UserMessages.put(message.getAuthorId(), messagesByUser);
    persistentStorageAgent.writeThrough(message);
  }

  /** Access the current set of Messages within the given Conversation. */
  public LinkedList<Message> getMessagesInConversation(UUID conversationId) {
    return messages.get(conversationId);
  }
  
  /** Access the current set of Messages by the current user. */
  public LinkedList<Message> getMessagesByUser(UUID user) {
    return UserMessages.get(user);
  }


  /** Sets the List of Messages stored by this MessageStore. */
  public void setMessages(HashMap<UUID, LinkedList<Message>> messagesByConversation,
                          HashMap<UUID, LinkedList<Message>> messagesByUser) {
    this.messages = messagesByConversation;
    this.UserMessages = messagesByUser;
  }
}
