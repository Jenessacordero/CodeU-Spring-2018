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

import codeu.model.data.Banner;
import codeu.model.data.Destination;
import codeu.model.data.Image;
import codeu.model.data.UserAction;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.time.Instant;
import java.util.*;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class BannerStore {

    /** Singleton instance of BannerStore. */
    private static BannerStore instance;

    /**
     * Returns the singleton instance of BannerStore that should be shared between all servlet
     * classes. Do not call this function from a test; use getTestInstance() instead.
     */
    public static BannerStore getInstance() {
        if (instance == null) {
            instance = new BannerStore(PersistentStorageAgent.getInstance());
        }
        return instance;
    }

    /**
     * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
     *
     * @param persistentStorageAgent a mock used for testing
     */
    public static BannerStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
        return new BannerStore(persistentStorageAgent);
    }

    /**
     * The PersistentStorageAgent responsible for loading Banner from and saving Banners to
     * Datastore.
     */
    private PersistentStorageAgent persistentStorageAgent;

    /** The in-memory list of Banners. */
    private HashMap<String, Banner> banners;

    public HashMap<String, Banner> returnBanners() {
        return banners;
    }

    /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
    private BannerStore(PersistentStorageAgent persistentStorageAgent) {
        this.persistentStorageAgent = persistentStorageAgent;
        banners = new HashMap<>();
    }

    /** Add a new Banner to the current set of Banner known to the application. */
    public void addBanner(String destination, Banner banner) {
        banners.put(destination, banner);
        persistentStorageAgent.writeThrough(banner);
    }

    public Banner returnBanner(String destination) {
        return banners.get(destination);
    }

    /** Sets the List of Banners stored by this BannerStore. */
    public void setBanner(HashMap<String, Banner> banners) {
        this.banners = banners;
    }
}