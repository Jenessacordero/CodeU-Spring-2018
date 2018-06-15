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

import codeu.model.data.StatusUpdate;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class StatusUpdateStore {

  /** Singleton instance of StatusUpdateStore. */
  private static StatusUpdateStore instance;

  /**
   * Returns the singleton instance of MessageStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static StatusUpdateStore getInstance() {
    if (instance == null) {
      instance = new StatusUpdateStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static StatusUpdateStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new StatusUpdateStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading StatusUpdates from and saving StatusUpdates to
   * Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Messages. */
  private List<StatusUpdate> statusUpdates;

  public List<StatusUpdate> returnAllStatusUpdates() {
    return statusUpdates;
  }

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private StatusUpdateStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    statusUpdates = new ArrayList<>();
  }

  /** Add a new message to the current set of statusUpdates known to the application. */
  public void addStatusUpdate(StatusUpdate statusUpdate) {
    statusUpdates.add(statusUpdate);
    persistentStorageAgent.writeThrough(statusUpdate);
  }

  /** Sets the List of StatusUpdates stored by this StatusUpdateStore. */
  public void setStatusUpdates(List<StatusUpdate> statusUpdates) {
    this.statusUpdates = statusUpdates;
  }
}