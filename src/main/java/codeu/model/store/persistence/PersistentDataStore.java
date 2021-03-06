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

package codeu.model.store.persistence;

import codeu.model.data.Conversation;
import codeu.model.data.Destination;
import codeu.model.data.Message;
import codeu.model.data.StatusUpdate;
import codeu.model.data.Tip;
import codeu.model.data.User;
import codeu.model.data.UserAction;
import codeu.model.data.AboutMe;
import codeu.model.data.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.SortDirection;
import sun.security.krb5.internal.crypto.Des;


import java.time.Instant;
import java.util.*;

/**
 * This class handles all interactions with Google App Engine's Datastore service. On startup it
 * sets the state of the applications's data objects from the current contents of its Datastore. It
 * also performs writes of new of modified objects back to the Datastore.
 */
public class PersistentDataStore {

  // Handle to Google AppEngine's Datastore service.
  private DatastoreService datastore;

  /**
   * Constructs a new PersistentDataStore and sets up its state to begin loading objects from the
   * Datastore service.
   */
  public PersistentDataStore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Loads all User objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<User> loadUsers() throws PersistentDataStoreException {

    List<User> users = new ArrayList<>();

    // Retrieve all users from the datastore.
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        String userName = (String) entity.getProperty("username");
        String password = (String) entity.getProperty("password_hash");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        HashMap<String, String> votesDictionary = new HashMap<>();
        EmbeddedEntity ee = (EmbeddedEntity) entity.getProperty("votesDictionary");
        if (ee != null) {
            for (String key : ee.getProperties().keySet()) {
                votesDictionary.put(key, (String) ee.getProperty(key));
            }
        }

        User user = new User(uuid, userName, password, creationTime, votesDictionary);
        users.add(user);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return users;
  }

  /**
   * Loads all Conversation objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Conversation> loadConversations() throws PersistentDataStoreException {

    List<Conversation> conversations = new ArrayList<>();

    // Retrieve all conversations from the datastore.
    Query query = new Query("chat-conversations").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID ownerUuid = UUID.fromString((String) entity.getProperty("owner_uuid"));
        String title = (String) entity.getProperty("title");
        UUID destinationUuid = UUID.fromString((String) entity.getProperty("destination_uuid"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        Conversation conversation = new Conversation(uuid, ownerUuid, destinationUuid, title, creationTime);
        conversations.add(conversation);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return conversations;
  }

  /**
   * Loads all Message objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Message> loadMessages() throws PersistentDataStoreException {

    List<Message> messages = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-messages").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID conversationUuid = UUID.fromString((String) entity.getProperty("conv_uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String content = (String) entity.getProperty("content");
        Character type = ((String) entity.getProperty("type")).charAt(0);
        Message message = new Message(uuid, conversationUuid, authorUuid, content, creationTime, type);
        messages.add(message);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return messages;
  }
  
  /**
   * Loads all AboutMe objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<AboutMe> loadAboutMes() throws PersistentDataStoreException {

    List<AboutMe> aboutMes = new ArrayList<>();

    // Retrieve all aboutmes from the datastore.
    Query query = new Query("chat-aboutmes");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID owner = UUID.fromString((String) entity.getProperty("owner"));
        String content = (String) entity.getProperty("content");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        AboutMe aboutMe = new AboutMe(uuid, owner, content, creationTime);
        aboutMes.add(aboutMe);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return aboutMes;
  }
  
  public List<StatusUpdate> loadStatusUpdates() throws PersistentDataStoreException {

	    List<StatusUpdate> statusUpdates = new ArrayList<>();

	    // Retrieve all messages from the datastore.
	    Query query = new Query("status-updates").addSort("creation_time", SortDirection.ASCENDING);
	    PreparedQuery results = datastore.prepare(query);

	    for (Entity entity : results.asIterable()) {
	      try {
	        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
	        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
	        String content = (String) entity.getProperty("content");
	        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
	        StatusUpdate statusUpdate = new StatusUpdate(uuid, authorUuid, content, creationTime);
	        statusUpdates.add(statusUpdate);
	      } catch (Exception e) {
	        // In a production environment, errors should be very rare. Errors which may
	        // occur include network errors, Datastore service errors, authorization errors,
	        // database entity definition mismatches, or service mismatches.
	        throw new PersistentDataStoreException(e);
	      }
	    }

	    return statusUpdates;
	  }
  
  public List<UserAction> loadUserActions() throws PersistentDataStoreException {

	    List<UserAction> userActions = new ArrayList<>();

	    // Retrieve all messages from the datastore.
	    Query query = new Query("user-actions").addSort("creation_time", SortDirection.DESCENDING);
	    PreparedQuery results = datastore.prepare(query);

	    for (Entity entity : results.asIterable()) {
	      try {
	        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
	        UUID userUuid = UUID.fromString((String) entity.getProperty("user_uuid"));
	        String message = (String) entity.getProperty("message");
	        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
	        UserAction userAction = new UserAction(uuid, userUuid, message, creationTime);
	        userActions.add(userAction);
	    	  
	      } catch (Exception e) {
	        // In a production environment, errors should be very rare. Errors which may
	        // occur include network errors, Datastore service errors, authorization errors,
	        // database entity definition mismatches, or service mismatches.
	        throw new PersistentDataStoreException(e);
	      }
	    }

	    return userActions;
	  }
  
  public List<Destination> loadDestinations() throws PersistentDataStoreException {

	    List<Destination> destinations = new ArrayList<>();

	    // Retrieve all messages from the datastore.
	    Query query = new Query("destinations").addSort("creation_time", SortDirection.ASCENDING);
	    PreparedQuery results = datastore.prepare(query);

	    for (Entity entity : results.asIterable())
            try {
                UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
                UUID owner = UUID.fromString((String) entity.getProperty("owner"));
                String title = (String) entity.getProperty("title");
                Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
                Text banner = new Text("");
                int votes = Long.valueOf((Long) entity.getProperty("votes")).intValue();
                Destination destination = new Destination(uuid, owner, title, creationTime, banner, votes);
                destinations.add(destination);
            } catch (Exception e) {
                // In a production environment, errors should be very rare. Errors which may
                // occur include network errors, Datastore service errors, authorization errors,
                // database entity definition mismatches, or service mismatches.
                throw new PersistentDataStoreException(e);
            }

	    return destinations;
	  }
  
  public List<Tip> loadTips() throws PersistentDataStoreException {

	    List<Tip> tips = new ArrayList<>();

	    // Retrieve all messages from the datastore.
	    Query query = new Query("tips").addSort("creation_time", SortDirection.ASCENDING);
	    PreparedQuery results = datastore.prepare(query);

	    for (Entity entity : results.asIterable()) {
	      try {
	    	  UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
	          UUID destinationUuid = UUID.fromString((String) entity.getProperty("dest_uuid"));
	          UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
	          Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
	          String content = (String) entity.getProperty("content");
	          Tip tip = new Tip(uuid, destinationUuid, authorUuid, content, creationTime);
	          tips.add(tip);
	      } catch (Exception e) {
	        // In a production environment, errors should be very rare. Errors which may
	        // occur include network errors, Datastore service errors, authorization errors,
	        // database entity definition mismatches, or service mismatches.
	        throw new PersistentDataStoreException(e);
	      }
	    }

	    return tips;
	  }


    public List<Image> loadImages() throws PersistentDataStoreException {

        List<Image> images = new ArrayList<>();

        // Retrieve all messages from the datastore.
        Query query = new Query("images").addSort("creation_time", SortDirection.ASCENDING);
        PreparedQuery results = datastore.prepare(query);

        for (Entity entity : results.asIterable()) {
            try {
                UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
                String destination = (String) entity.getProperty("destination");
                Text filename = (Text) entity.getProperty("filename") ;
                Instant now = Instant.parse((String) entity.getProperty("creation_time"));
                Image image = new Image(filename, destination, uuid, now);
                images.add(image);
            } catch (Exception e) {
                // In a production environment, errors should be very rare. Errors which may
                // occur include network errors, Datastore service errors, authorization errors,
                // database entity definition mismatches, or service mismatches.
                throw new PersistentDataStoreException(e);
            }
        }
        return images;
    }

    public HashMap<String, Banner> loadBanners() throws PersistentDataStoreException {

        HashMap<String, Banner> banners = new HashMap<>();

        // Retrieve all banners from the datastore.
        Query query = new Query("banners").addSort("creation_time", SortDirection.ASCENDING);
        PreparedQuery results = datastore.prepare(query);

        for (Entity entity : results.asIterable()) {
            try {
                UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
                String destination = (String) entity.getProperty("destination");
                Text filename = (Text) entity.getProperty("filename");
                Instant now = Instant.parse((String) entity.getProperty("creation_time"));
                Banner banner = new Banner(filename, destination, uuid, now);
                banners.put(destination, banner);
            } catch (Exception e) {
                // In a production environment, errors should be very rare. Errors which may
                // occur include network errors, Datastore service errors, authorization errors,
                // database entity definition mismatches, or service mismatches.
                throw new PersistentDataStoreException(e);
            }
        }
        return banners;
    }

//	  //TODO implement/modify for ranked destinations -> done!
//    public List<Destination> loadRankedDestinations() throws PersistentDataStoreException {
//
//        List<Destination> rankedDestinations = new ArrayList<>();
////
////        // Retrieve from the datastore.
//        Query query = new Query("rankedDestinations").addSort("creation_time", SortDirection.ASCENDING);
//        PreparedQuery results = datastore.prepare(query);
////
//        for (Entity entity : results.asIterable()) {
//            try {
//                UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
//                UUID owner = UUID.fromString((String) entity.getProperty("owner"));
//                String title = (String) entity.getProperty("title");
//                Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
//                int votes = (int) entity.getProperty("votes");
//                Destination destination = new Destination(uuid, owner, title, creationTime, new Text(""), votes);
//                rankedDestinations.add(destination);
//            } catch (Exception e) {
//                // In a production environment, errors should be very rare. Errors which may
//                // occur include network errors, Datastore service errors, authorization errors,
//                // database entity definition mismatches, or service mismatches.
//                throw new PersistentDataStoreException(e);
//            }
//        }
//        return rankedDestinations;
//    }




  /** Write a User object to the Datastore service. */
  public void writeThrough(User user) {
	  
    Entity userEntity = new Entity("chat-users", user.getId().toString());
    userEntity.setProperty("uuid", user.getId().toString());
    userEntity.setProperty("username", user.getName());
    userEntity.setProperty("password_hash", user.getPasswordHash());
    userEntity.setProperty("creation_time", user.getCreationTime().toString());
    EmbeddedEntity ee = new EmbeddedEntity();
    Map<String, String> votesDictionary = user.getVotesDictionary();

    for (String key : votesDictionary.keySet()) { // TODO: maybe there is a more efficient way of solving this
        ee.setProperty(key, votesDictionary.get(key));
    }
    userEntity.setProperty("votesDictionary", ee);

    datastore.put(userEntity);
  }

  /** Write a Message object to the Datastore service. */
  public void writeThrough(Message message) {
    Entity messageEntity = new Entity("chat-messages", message.getId().toString());
    messageEntity.setProperty("uuid", message.getId().toString());
    messageEntity.setProperty("conv_uuid", message.getConversationId().toString());
    messageEntity.setProperty("author_uuid", message.getAuthorId().toString());
    messageEntity.setProperty("content", message.getContent());
    messageEntity.setProperty("creation_time", message.getCreationTime().toString());
    messageEntity.setProperty("type", message.getType().toString());
    datastore.put(messageEntity);
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Conversation conversation) {
    Entity conversationEntity = new Entity("chat-conversations", conversation.getId().toString());
    conversationEntity.setProperty("uuid", conversation.getId().toString());
    conversationEntity.setProperty("owner_uuid", conversation.getOwnerId().toString());
    conversationEntity.setProperty("title", conversation.getTitle());
    conversationEntity.setProperty("destination_uuid", conversation.getDestinationId().toString());
    conversationEntity.setProperty("creation_time", conversation.getCreationTime().toString());
    datastore.put(conversationEntity);
  }
  
  /** Write an AboutMe object to the Datastore service. 
   * @throws PersistentDataStoreException */
  public void writeThrough(AboutMe aboutMe) throws PersistentDataStoreException {
	// Retrieve all aboutmes from the datastore.
	  Query query = new Query("chat-aboutmes");
	  PreparedQuery results = datastore.prepare(query);

	    for (Entity entity : results.asIterable()) {
	      try {
	    	if(entity.getProperty("owner").equals(aboutMe.getOwner().toString())) {
	    		entity.setProperty("content", aboutMe.getContent());
	    		datastore.put(entity);
	    		return;
	    	}
	      } catch (Exception e) {
	        // In a production environment, errors should be very rare. Errors which may
	        // occur include network errors, Datastore service errors, authorization errors,
	        // database entity definition mismatches, or service mismatches.
	        throw new PersistentDataStoreException(e);
	      }
	    }
	    
	    Entity aboutMeEntity = new Entity("chat-aboutmes", aboutMe.getId().toString());
	    aboutMeEntity.setProperty("uuid", aboutMe.getId().toString());
	    aboutMeEntity.setProperty("owner", aboutMe.getOwner().toString());
	    aboutMeEntity.setProperty("content", aboutMe.getContent());
	    aboutMeEntity.setProperty("creation_time", aboutMe.getCreationTime().toString());
	    datastore.put(aboutMeEntity);
  }
  
  /** Write a StatusUpdate object to the Datastore service. */
  public void writeThrough(StatusUpdate statusUpdate) {
    Entity statusUpdateEntity = new Entity("status-updates", statusUpdate.getId().toString());
    statusUpdateEntity.setProperty("uuid", statusUpdate.getId().toString());
    statusUpdateEntity.setProperty("author_uuid", statusUpdate.getAuthorId().toString());
    statusUpdateEntity.setProperty("content", statusUpdate.getContent());
    statusUpdateEntity.setProperty("creation_time", statusUpdate.getCreationTime().toString());
    datastore.put(statusUpdateEntity);
  }
  
  /** Write a UserAction object to the Datastore service. */
  public void writeThrough(UserAction userAction) {
    Entity userActionEntity = new Entity("user-actions", userAction.getId().toString());
    userActionEntity.setProperty("uuid", userAction.getId().toString());
    userActionEntity.setProperty("user_uuid", userAction.getUserId().toString());
    userActionEntity.setProperty("message", userAction.getMessage());
    userActionEntity.setProperty("creation_time", userAction.getCreationTime().toString());
    datastore.put(userActionEntity);
  }
  
  /** Write a Destination object to the Datastore service. */
  public void writeThrough(Destination destination) throws PersistentDataStoreException{
      Query query = new Query("destinations");
      PreparedQuery results = datastore.prepare(query);

      for (Entity entity : results.asIterable()) {
          try {
              if(entity.getProperty("uuid").equals(destination.getId().toString())) {
                  entity.setProperty("votes", (destination.getVotes()));
                  datastore.put(entity);
                  return;
              }
          } catch (Exception e) {
              // In a production environment, errors should be very rare. Errors which may
              // occur include network errors, Datastore service errors, authorization errors,
              // database entity definition mismatches, or service mismatches.
              throw new PersistentDataStoreException(e);
          }
      }
    Entity destinationEntity = new Entity("destinations", destination.getId().toString());
    destinationEntity.setProperty("uuid", destination.getId().toString());
    destinationEntity.setProperty("owner", destination.getOwnerId().toString());
    destinationEntity.setProperty("title", destination.getTitle());
    destinationEntity.setProperty("creation_time", destination.getCreationTime().toString());
    destinationEntity.setProperty("votes", destination.getVotes());
    destinationEntity.setProperty("banner", destination.getBanner());
    datastore.put(destinationEntity);
  }
  
  /** Write a Tip object to the Datastore service. */
  public void writeThrough(Tip tip) {
    Entity tipEntity = new Entity("tips", tip.getId().toString());
    tipEntity.setProperty("uuid", tip.getId().toString());
    tipEntity.setProperty("dest_uuid", tip.getDestinationId().toString());
    tipEntity.setProperty("author_uuid", tip.getAuthorId().toString());
    tipEntity.setProperty("content", tip.getContent());
    tipEntity.setProperty("creation_time", tip.getCreationTime().toString());
    datastore.put(tipEntity);
  }

    /** Write a Image object to the Datastore service. */
    public void writeThrough(Image image) {
        Entity imageEntity = new Entity("images", image.getId().toString());
        imageEntity.setProperty("uuid", image.getId().toString());
        imageEntity.setProperty("filename", image.returnFilename());
        imageEntity.setProperty("destination", image.returnDestination());
        imageEntity.setProperty("creation_time", image.getCreation().toString());
        datastore.put(imageEntity);
    }

    /** Write a Banner object to the Datastore service. */
    public void writeThrough(Banner banner) {
        Entity bannerEntity = new Entity("banners", banner.returnID().toString());
        bannerEntity.setProperty("destination", banner.returnDestination());
        bannerEntity.setProperty("uuid", banner.returnID().toString());
        bannerEntity.setProperty("filename", banner.returnBanner());
        bannerEntity.setProperty("creation_time", banner.returnCreation().toString());
        datastore.put(bannerEntity);
    }
}

