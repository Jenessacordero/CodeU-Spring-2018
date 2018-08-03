package codeu.model.store.basic;

import codeu.model.data.Destination;
import codeu.model.store.basic.DestinationStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;
import com.google.appengine.api.datastore.Text;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DestinationStoreTest {

  private DestinationStore destinationStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final Destination DESTINATION_ONE =
	      new Destination(
          UUID.randomUUID(), UUID.randomUUID(), "destination_one", Instant.ofEpochMilli(1000), new Text(""), 5);
  
  private final Destination DESTINATION_TWO =
	      new Destination(
	          UUID.randomUUID(), UUID.randomUUID(), "destination_two", Instant.ofEpochMilli(1000), new Text(""), 0);

  private final Destination DESTINATION_THREE =
          new Destination(
                  UUID.randomUUID(), UUID.randomUUID(), "destination_three", Instant.ofEpochMilli(1000), new Text(""), -2);

  private final Destination DESTINATION_FOUR =
          new Destination(
                  UUID.randomUUID(), UUID.randomUUID(), "destination_three", Instant.ofEpochMilli(1000), new Text(""), -9);

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    destinationStore = DestinationStore.getTestInstance(mockPersistentStorageAgent);

    final List<Destination> destinationList = new ArrayList<>();
    destinationList.add(DESTINATION_THREE);
    destinationList.add(DESTINATION_ONE);
    destinationList.add(DESTINATION_FOUR);
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
  public void testAddDestination() throws PersistentDataStoreException {
    Destination inputDestination =
        new Destination(UUID.randomUUID(), UUID.randomUUID(), "test_destination", Instant.now(), new Text(""), 5);

    destinationStore.addDestination(inputDestination);

    Destination resultDestination =
    		destinationStore.getDestinationWithTitle("test_destination");

    assertEquals(inputDestination, resultDestination);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputDestination);
  }

  @Test
  public void test_rankings_correct() throws IOException, ServletException {
    List<Destination> fakeRankedList = new ArrayList<>();
    fakeRankedList.add(DESTINATION_ONE);
    fakeRankedList.add(DESTINATION_TWO);
    fakeRankedList.add(DESTINATION_THREE);
    fakeRankedList.add(DESTINATION_FOUR);

    List<Destination> resultRankedList = destinationStore.getRankedDestinations();

    Assert.assertEquals(fakeRankedList, resultRankedList);
  }

  private void assertEquals(Destination expectedDestination, Destination actualDestination) {
    Assert.assertEquals(expectedDestination.getId(), actualDestination.getId());
    Assert.assertEquals(expectedDestination.getOwnerId(), actualDestination.getOwnerId());
    Assert.assertEquals(expectedDestination.getTitle(), actualDestination.getTitle());
    Assert.assertEquals(
    		expectedDestination.getCreationTime(), actualDestination.getCreationTime());
    Assert.assertEquals(expectedDestination.getBanner(), actualDestination.getBanner());
  }

}