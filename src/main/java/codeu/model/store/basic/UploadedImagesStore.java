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

import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import codeu.model.data.Images;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class UploadedImagesStore {

    /** Singleton instance of UserActionStore. */
    private static UploadedImagesStore instance;

    /**
     * Returns the singleton instance of MessageStore that should be shared between all servlet
     * classes. Do not call this function from a test; use getTestInstance() instead.
     */
    public static UploadedImagesStore getInstance() {
        if (instance == null) {
            instance = new UploadedImagesStore(PersistentStorageAgent.getInstance());
        }
        return instance;
    }

    /**
     * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
     *
     * @param persistentStorageAgent a mock used for testing
     */
    public static UploadedImagesStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
        return new UploadedImagesStore(persistentStorageAgent);
    }

    /**
     * The PersistentStorageAgent responsible for loading Images from and saving UserActions to
     * Datastore.
     */
    private PersistentStorageAgent persistentStorageAgent;

    /** The in-memory list of Images. */
    private List<Images> UploadedImages;

    public List<Images> returnAllImages() {
        return UploadedImages;
    }


    /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
    private UploadedImagesStore(PersistentStorageAgent persistentStorageAgent) {
        this.persistentStorageAgent = persistentStorageAgent;
        UploadedImages = new ArrayList<>();
    }

    /** Add a new userAction to the current set of Images known to the application. */
    public void addImage(Images image) {
        UploadedImages.add(image);
        persistentStorageAgent.writeThrough(image);
    }

    /** Sets the List of UserActions stored by this ImageStore. */
    public void setImageStore(List<Images> images) {
        this.UploadedImages = images;
    }
}