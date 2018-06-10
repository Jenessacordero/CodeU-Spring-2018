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

import codeu.model.data.AboutMe;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.persistence.PersistentDataStoreException;
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
public class AboutMeStore {

  /** Singleton instance of ConversationStore. */
  private static AboutMeStore instance;

  /**
   * Returns the singleton instance of AboutMeStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static AboutMeStore getInstance() {
    if (instance == null) {
      instance = new AboutMeStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static AboutMeStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new AboutMeStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Conversations from and saving 'About Me's
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Conversations. */
  private List<AboutMe> aboutMes;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private AboutMeStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    aboutMes = new ArrayList<>();
  }


  /** Add a new aboutMe to the current set of conversations known to the application. 
 * @throws PersistentDataStoreException */
  public void addAboutMe(AboutMe newAboutMe) throws PersistentDataStoreException {
	  for (AboutMe aboutMe: aboutMes) {
		  if (aboutMe.getOwner().equals(newAboutMe.getOwner())) {
		        int index = aboutMes.indexOf(aboutMe);
		        aboutMes.set(index, newAboutMe);
		        persistentStorageAgent.writeThrough(newAboutMe);
		        return;
		      }
	  }
    aboutMes.add(newAboutMe);
    persistentStorageAgent.writeThrough(newAboutMe);
  }
  
  /** Access the current About Me the current user. */
  public AboutMe getAboutMeByUser(UUID user) {

    for (AboutMe aboutMe : aboutMes) {
      if (aboutMe.getOwner().equals(user)) {
    	  return aboutMe;
      }
    }
    
    AboutMe userAboutMe = new AboutMe(
            UUID.randomUUID(),
            user,
            "",
            Instant.now());

    return userAboutMe;
  }
  
  /**
   * Sets the List of AboutMes stored by this UserStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setAboutMes(List<AboutMe> aboutMes) {
    this.aboutMes = aboutMes;
  }

}