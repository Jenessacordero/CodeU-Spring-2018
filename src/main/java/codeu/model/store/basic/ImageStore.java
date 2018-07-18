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
import codeu.model.data.Image;
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
public class ImageStore {

    /** Singleton instance of UserActionStore. */
    private static ImageStore instance;

    /**
     * Returns the singleton instance of ImageStore that should be shared between all servlet
     * classes. Do not call this function from a test; use getTestInstance() instead.
     */
    public static ImageStore getInstance() {
        if (instance == null) {
            instance = new ImageStore(PersistentStorageAgent.getInstance());
        }
        return instance;
    }

    /**
     * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
     *
     * @param persistentStorageAgent a mock used for testing
     */
    public static ImageStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
        return new ImageStore(persistentStorageAgent);
    }

    /**
     * The PersistentStorageAgent responsible for loading Images from and saving Images to
     * Datastore.
     */
    private PersistentStorageAgent persistentStorageAgent;

    /** The in-memory list of UserActions. */
    private List<Image> images;

    public List<Image>returnAllImages() {
        return images;
    }


    /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
    private ImageStore(PersistentStorageAgent persistentStorageAgent) {
        this.persistentStorageAgent = persistentStorageAgent;
        images = new ArrayList<>();
    }

    /** Add a new userAction to the current set of UserActions known to the application. */
    public void addImage(Image image) {
        images.add(0, image);
        persistentStorageAgent.writeThrough(image);
    }

    public List<Image> returnImagesInDestination(Destination destination) {
        List<Image> list = new ArrayList<>();
        for (Image image : this.images) {
            if (image.returnDestination().equals(destination.getTitle())) {
                list.add(image);
            }
        }
        return list;
    }

    /** Sets the List of UserActions stored by this UserActionStore. */
    public void setImages(List<Image> images) {
        this.images = images;
    }
}