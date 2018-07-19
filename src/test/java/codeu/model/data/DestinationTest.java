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

import com.google.appengine.api.datastore.Text;
import org.junit.Assert;
import org.junit.Test;

public class DestinationTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID owner = UUID.randomUUID();
    String title = "Test_Title";
    Instant creation = Instant.now();
    Text banner = new Text("");

    Destination destination = new Destination(id, owner, title, creation, banner);

    Assert.assertEquals(id, destination.getId());
    Assert.assertEquals(owner, destination.getOwnerId());
    Assert.assertEquals(title, destination.getTitle());
    Assert.assertEquals(creation, destination.getCreationTime());
    Assert.assertEquals(banner, destination.getBanner());
  }
}