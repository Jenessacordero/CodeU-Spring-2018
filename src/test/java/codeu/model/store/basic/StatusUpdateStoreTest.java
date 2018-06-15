package codeu.model.store.basic;

import codeu.model.data.StatusUpdate;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class StatusUpdateStoreTest {

  private StatusUpdateStore statusUpdateStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final StatusUpdate STATUS_UPDATE_ONE =
      new StatusUpdate(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "update one",
          Instant.ofEpochMilli(1000));
  private final StatusUpdate STATUS_UPDATE_TWO =
      new StatusUpdate(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "update two",
          Instant.ofEpochMilli(2000));
  private final StatusUpdate STATUS_UPDATE_THREE =
      new StatusUpdate(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "update three",
          Instant.ofEpochMilli(3000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    statusUpdateStore = StatusUpdateStore.getTestInstance(mockPersistentStorageAgent);

    final List<StatusUpdate> statusUpdateList = new ArrayList<>();
    statusUpdateList.add(STATUS_UPDATE_ONE);
    statusUpdateList.add(STATUS_UPDATE_TWO);
    statusUpdateList.add(STATUS_UPDATE_THREE);
    statusUpdateStore.setStatusUpdates(statusUpdateList);
  }

  @Test
  public void testReturnAllStatusUpdates() {
    List<StatusUpdate> resultStatusUpdates = statusUpdateStore.returnAllStatusUpdates();

    Assert.assertEquals(3, resultStatusUpdates.size());
    assertEquals(STATUS_UPDATE_ONE, resultStatusUpdates.get(0));
    assertEquals(STATUS_UPDATE_TWO, resultStatusUpdates.get(1));
    assertEquals(STATUS_UPDATE_THREE, resultStatusUpdates.get(2));
  }

  @Test
  public void testAddStatusUpdate() {
    StatusUpdate inputStatusUpdate =
        new StatusUpdate(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test update",
            Instant.now());

    statusUpdateStore.addStatusUpdate(inputStatusUpdate);
    StatusUpdate resultStatusUpdate = statusUpdateStore.returnAllStatusUpdates().get(3);

    assertEquals(inputStatusUpdate, resultStatusUpdate);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputStatusUpdate);
  }

  private void assertEquals(StatusUpdate expectedStatusUpdate, StatusUpdate actualStatusUpdate) {
    Assert.assertEquals(expectedStatusUpdate.getId(), actualStatusUpdate.getId());
    Assert.assertEquals(expectedStatusUpdate.getAuthorId(), actualStatusUpdate.getAuthorId());
    Assert.assertEquals(expectedStatusUpdate.getContent(), actualStatusUpdate.getContent());
    Assert.assertEquals(expectedStatusUpdate.getCreationTime(), actualStatusUpdate.getCreationTime());
  }
}
