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

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.UserAction;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class UserActionStore {

  /** Singleton instance of UserActionStore. */
  private static UserActionStore instance;

  /**
   * Returns the singleton instance of MessageStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static UserActionStore getInstance() {
    if (instance == null) {
      instance = new UserActionStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static UserActionStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new UserActionStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading UserActions from and saving UserActions to
   * Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of UserActions. */
  private List<UserAction> userActions;

  public List<UserAction> returnAllUserActions() {
    return userActions;
  }
  

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private UserActionStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    userActions = new ArrayList<>();
  }

  /** Add a new userAction to the current set of UserActions known to the application. */
  public void addUserAction(UserAction userAction) {
    userActions.add(0, userAction);
    persistentStorageAgent.writeThrough(userAction);
  }
  
  /** Return set of UserActions by User. 
 * @return */
  public List<UserAction> returnUserActionsByUser(UUID user) {
	  List<UserAction> userActionsByUser = new ArrayList<>();
	  
	  for (UserAction userAction : userActions) {
	      if (userAction.getUserId().equals(user)) {
	    	  userActionsByUser.add(userAction);
	      }
	    }

	    return userActionsByUser;
  }

  /** Sets the List of UserActions stored by this UserActionStore. */
  public void setUserActions(List<UserAction> userActions) {
    this.userActions = userActions;
  }
}