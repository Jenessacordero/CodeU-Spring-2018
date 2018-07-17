package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.StatusUpdate;
import codeu.model.data.Tip;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.data.AboutMe;
import codeu.model.data.Destination;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.DestinationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.StatusUpdateStore;
import codeu.model.store.basic.TipStore;
import codeu.model.store.basic.UserActionStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.AboutMeStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener class that fires when the server first starts up, before any servlet classes are
 * instantiated.
 */
public class ServerStartupListener implements ServletContextListener {

  /** Loads data from Datastore. */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      List<User> users = PersistentStorageAgent.getInstance().loadUsers();
      UserStore.getInstance().setUsers(users);

      List<Conversation> conversations = PersistentStorageAgent.getInstance().loadConversations();
      ConversationStore.getInstance().setConversations(conversations);

      List<Message> messages = PersistentStorageAgent.getInstance().loadMessages();
      MessageStore.getInstance().setMessages(messages);
      
      List<AboutMe> aboutMes = PersistentStorageAgent.getInstance().loadAboutMes();
      AboutMeStore.getInstance().setAboutMes(aboutMes);
      
      List<StatusUpdate> statusUpdates = PersistentStorageAgent.getInstance().loadStatusUpdates();
      StatusUpdateStore.getInstance().setStatusUpdates(statusUpdates);
      
      List<UserAction> userActions = PersistentStorageAgent.getInstance().loadUserActions();
      UserActionStore.getInstance().setUserActions(userActions);
      
      List<Destination> destinations = PersistentStorageAgent.getInstance().loadDestinations();
      DestinationStore.getInstance().setDestinations(destinations);
      
      List<Tip> tips = PersistentStorageAgent.getInstance().loadTips();
      TipStore.getInstance().setTips(tips);

      //TODO implement functions -> done!
      List<Destination> rankedDestinations = PersistentStorageAgent.getInstance().loadRankedDestinations();
      DestinationStore.getInstance().setRankedDestinations(rankedDestinations);

    } catch (PersistentDataStoreException e) {
      System.err.println("Server didn't start correctly. An error occurred during Datastore load!");
      System.err.println("This is usually caused by loading data that's in an invalid format.");
      System.err.println("Check the stack trace to see exactly what went wrong.");
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}
}
