package codeu.model.store.basic;

import codeu.model.data.Destination;
import codeu.model.data.Conversation;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DestinationStoreTest {

  private DestinationStore destinationStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final Destination DESTINATION_ONE =
      new Destination(
          UUID.randomUUID(), UUID.randomUUID(), "destination_one", Instant.ofEpochMilli(1000));
  
  private final Destination DESTINATION_TWO =
	      new Destination(
	          UUID.randomUUID(), UUID.randomUUID(), "destination_two", Instant.ofEpochMilli(1000));
	  

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    destinationStore = DestinationStore.getTestInstance(mockPersistentStorageAgent);

    final List<Destination> destinationList = new ArrayList<>();
    destinationList.add(DESTINATION_ONE);
    destinationList.add(DESTINATION_TWO);
    destinationStore.setDestinations(destinationList);
  }

  @Test
  public void testGetDestinationWithTitle_found() {
    Destination resultDestinationOne =
    		destinationStore.getDestinationWithTitle(DESTINATION_ONE.getTitle());
    Destination resultDestinationTWO =
    		destinationStore.getDestinationWithTitle(DESTINATION_TWO.getTitle());

    assertEquals(DESTINATION_ONE, resultDestinationOne);
    assertEquals(DESTINATION_TWO, resultDestinationTWO);
  }

  @Test
  public void testGetConversationWithTitle_notFound() {
    Destination resultDestination = destinationStore.getDestinationWithTitle("unfound_title");

    Assert.assertNull(resultDestination);
  }

  @Test
  public void testIsTitleTaken_true() {
    boolean isTitleTaken = destinationStore.isTitleTaken(DESTINATION_ONE.getTitle());

    Assert.assertTrue(isTitleTaken);
  }

  @Test
  public void testIsTitleTaken_false() {
    boolean isTitleTaken = destinationStore.isTitleTaken("unfound_title");

    Assert.assertFalse(isTitleTaken);
  }

  @Test
  public void testAddDestination() {
    Destination inputDestination =
        new Destination(UUID.randomUUID(), UUID.randomUUID(), "test_destination", Instant.now());

    destinationStore.addDestination(inputDestination);
    
    Destination resultDestination =
    		destinationStore.getDestinationWithTitle("test_destination");

    assertEquals(inputDestination, resultDestination);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputDestination);
  }

  private void assertEquals(Destination expectedDestination, Destination actualDestination) {
    Assert.assertEquals(expectedDestination.getId(), actualDestination.getId());
    Assert.assertEquals(expectedDestination.getOwnerId(), actualDestination.getOwnerId());
    Assert.assertEquals(expectedDestination.getTitle(), actualDestination.getTitle());
    Assert.assertEquals(
    		expectedDestination.getCreationTime(), actualDestination.getCreationTime());
  }
}