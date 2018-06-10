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
 * Class representing an 'About Me', which is a bio of the user.
 */
public class AboutMe {
  public final UUID id;
  public final UUID owner;
  public final String content;
  public final Instant creation;

  
  /**
   * Constructs a new AboutMe.
   *
   * @param id the ID of this Message
   * @param the ID of the User who sent this Message
   * @param content the text content of this Message
   * @param creation the creation time of this Message
   */
  public AboutMe(UUID id, UUID owner, String content, Instant creation) {
    this.id = id;
    this.owner = owner;
    this.content = content;
    this.creation = creation;
  }
  
  /** Returns the ID of this Message. */
  public UUID getId() {
    return id;
  }
  
  /** Returns the ID of the User of this bio. */
  public UUID getOwner() {
    return owner;
  }
  
  /** Returns the content of this bio. */
  public String getContent() {
    return content;
  }
  
  /** Returns the creation time of this Conversation. */
  public Instant getCreationTime() {
    return creation;
  }
}