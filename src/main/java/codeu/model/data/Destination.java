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
import java.util.UUID;

/**
 * Class representing a destination. Destinations are
 * created by a User and contain Conversations.
 */
public class Destination {
  public final UUID id;
  public final UUID owner;
  public final String title;
  public final Instant creation;
  public String banner;
  private int votes;

  /**
   * Constructs a new Conversation.
   *
   * @param id the ID of this Destination
   * @param owner the ID of the User who created this Destination
   * @param title the title of this Destination
   * @param creation the creation time of this Destination
   * @param votes the number of votes from users it has, default is zero if not defined
   */

  public Destination(UUID id, UUID owner, String title, Instant creation, String banner, int votes) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.votes = votes;
    this.banner = banner;
  }

  public Destination(UUID id, UUID owner, String title, Instant creation, String banner) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.banner = banner;
    this.votes = 0;
  }

  /** Returns the ID of this Destination. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the User who created this Destination. */
  public UUID getOwnerId() {
    return owner;
  }

  /** Returns the title of this Destination. */
  public String getTitle() {
    return title;
  }

  /** Returns the creation time of this Destination. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the banner of this Destination. */
  public String getBanner() {
    return this.banner;
  }

  public void changeBanner(String filename) {
    this.banner = filename;
  }

  public int getVotes() {
    return votes;
  }

  /** Called when a user likes a destination. */
  public void upVote() {
    this.votes += 1;
  }

  /** Called when a user dislikes a destination. */
  public void downVote() {
    this.votes += -1;
  }
}
