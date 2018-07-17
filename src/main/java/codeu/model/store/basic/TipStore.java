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

import codeu.model.data.Tip;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class TipStore {

  /** Singleton instance of TipStore. */
  private static TipStore instance;

  /**
   * Returns the singleton instance of TipStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static TipStore getInstance() {
    if (instance == null) {
      instance = new TipStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static TipStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new TipStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Messages from and saving Messages to
   * Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Tips. */
  private List<Tip> tips;

  public List<Tip> returnAllTips() {
    return tips;
  }

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private TipStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    tips = new ArrayList<>();
  }

  /** Add a new tip to the current set of tips known to the application. */
  public void addTip(Tip tip) {
    tips.add(tip);
    persistentStorageAgent.writeThrough(tip);
  }

  /** Access the current set of Tips within the given Destination. */
  public List<Tip> getTipsInDestination(UUID destinationId) {

    List<Tip> tipsInDestination = new ArrayList<>();

    for (Tip tip : tips) {
      if (tip.getDestinationId().equals(destinationId)) {
    	  tipsInDestination.add(tip);
      }
    }

    return tipsInDestination;
  }
  
  /** Access the current set of Tips by the current user. */
  public List<Tip> getTipsByUser(UUID user) {

    List<Tip> messagesByUser = new ArrayList<>();

    for (Tip tip : tips) {
      if (tip.getAuthorId().equals(user)) {
    	  messagesByUser.add(tip);
      }
    }

    return messagesByUser;
  }

  /** Sets the List of Tips stored by this TipStore. */
  public void setTips(List<Tip> tips) {
    this.tips = tips;
  }
}
