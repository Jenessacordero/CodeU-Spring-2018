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

/** Class representing a Tip. Tips are created by users for a specific location. */
public class Tip {

  private final UUID id;
  private final UUID destination;
  private final UUID author;
  private final String content;
  private final Instant creation;

  /**
   * Constructs a new Tip.
   *
   * @param id the ID of this Tip
   * @param conversation the ID of the Destination this Tip belongs to
   * @param author the ID of the User who sent this Tip
   * @param content the text content of this Tip
   * @param creation the creation time of this Tip
   */
  public Tip(UUID id, UUID destination, UUID author, String content, Instant creation) {
    this.id = id;
    this.destination = destination;
    this.author = author;
    this.content = content;
    this.creation = creation;
  }

  /** Returns the ID of this Tip. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the Conversation this Tip belongs to. */
  public UUID getDestinationId() {
    return destination;
  }

  /** Returns the ID of the User who sent this Tip. */
  public UUID getAuthorId() {
    return author;
  }

  /** Returns the text content of this Tip. */
  public String getContent() {
    return content;
  }

  /** Returns the creation time of this Tip. */
  public Instant getCreationTime() {
    return creation;
  }
}
