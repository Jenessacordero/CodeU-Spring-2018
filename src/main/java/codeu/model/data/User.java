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

package codeu.model.data;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String passwordHash;
  private final Instant creation;
  private int personalMessages;
  private int numWords;
  private HashMap<String, String> votesDictionary;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param passwordHash the password of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String passwordHash, Instant creation) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
    this.personalMessages = 0;
    this.numWords = 0;
    this.votesDictionary = new HashMap<String, String>();
  }

  public User(UUID id, String name, String passwordHash, Instant creation, HashMap<String, String> votesDictionary) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
    this.personalMessages = 0;
    this.numWords = 0;
    this.votesDictionary = votesDictionary;
  }
  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }
  
  /** Returns the password hash of this User. */
  public String getPasswordHash() {
    return passwordHash;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the number of messages the user has sent */
  public int getNumPersonalMessages() { return personalMessages; }

  /** Returns the sum of all lengths of messages sent by this user */
  public int getnumWords() { return numWords; }

  /** Modifies the users message count if they send another message */
  public void changeNumPersonalMessageCount() { this.personalMessages += 1; }

  /** Modifies the users word count if they send another message */
  public void changeNumWords(String message) { this.numWords += (message.length()); }

  public HashMap<String, String> getVotesDictionary() {
    return votesDictionary;
  }

  public void setVotesDictionary(HashMap<String, String> votesDictionary) {
    this.votesDictionary = votesDictionary;
  }
}
