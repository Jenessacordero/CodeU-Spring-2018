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

import codeu.model.data.Destination;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class DestinationStore {

  /** Singleton instance of DestinationStore. */
  private static DestinationStore instance;

  /**
   * Returns the singleton instance of DestinationStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static DestinationStore getInstance() {
    if (instance == null) {
      instance = new DestinationStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static DestinationStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new DestinationStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Destinations from and saving Destinations
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Destinations. */
  private List<Destination> destinations;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private DestinationStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    destinations = new ArrayList<>();
  }

/** Access the current set of destinations known to the application. */
  public List<Destination> getAllDestinations() {
    return destinations;
  }

  /** Add a new destination to the current set of destinations known to the application. */
  public void addDestination(Destination destination) {
	  destinations.add(destination);
	  persistentStorageAgent.writeThrough(destination);
  }

  /** Check whether a Destination title is already known to the application. */
  public boolean isTitleTaken(String title) {
    // This approach will be pretty slow if we have many Conversations.
    for (Destination destination : destinations) {
      if (destination.getTitle().equals(title)) {
        return true;
      }
    }
    return false;
  }

  /** Find and return the Destination with the given title. */
  public Destination getDestinationWithTitle(String title) {
    for (Destination destination : destinations) {
      if (destination.getTitle().equals(title)) {
        return destination;
      }
    }
    return null;
  }

  /** Sets the List of Destinations stored by this DestinationStore. */
  public void setDestinations(List<Destination> destinations) {
    this.destinations = destinations;
  }
}