package codeu.model.store.basic;

import codeu.model.data.Tip;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class TipStoreTest {

  private TipStore tipStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UUID DESTINATION_ID_ONE = UUID.randomUUID();
  private final UUID DESTINATION_ID_TWO = UUID.randomUUID();
  private final Tip TIP_ONE =
      new Tip(
          UUID.randomUUID(),
          DESTINATION_ID_ONE,
          UUID.randomUUID(),
          "tip one",
          Instant.ofEpochMilli(1000));
  private final Tip TIP_TWO =
      new Tip(
          UUID.randomUUID(),
          DESTINATION_ID_ONE,
          UUID.randomUUID(),
          "tip two",
          Instant.ofEpochMilli(2000));
  private final Tip TIP_THREE =
      new Tip(
          UUID.randomUUID(),
          DESTINATION_ID_TWO,
          UUID.randomUUID(),
          "tip three",
          Instant.ofEpochMilli(3000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    tipStore = TipStore.getTestInstance(mockPersistentStorageAgent);

    final List<Tip> tipList = new ArrayList<>();
    tipList.add(TIP_ONE);
    tipList.add(TIP_TWO);
    tipList.add(TIP_THREE);
    tipStore.setTips(tipList);
  }

  @Test
  public void testGetTipsInDestination() {
    List<Tip> resultTips1 = tipStore.getTipsInDestination(DESTINATION_ID_ONE);
    List<Tip> resultTips2 = tipStore.getTipsInDestination(DESTINATION_ID_TWO);
    

    Assert.assertEquals(2, resultTips1.size());
    Assert.assertEquals(1, resultTips2.size());
    assertEquals(TIP_ONE, resultTips1.get(0));
    assertEquals(TIP_TWO, resultTips1.get(1));
    assertEquals(TIP_THREE, resultTips2.get(0));
  }

  @Test
  public void testAddTip() {
    UUID inputDestinationId = UUID.randomUUID();
    Tip inputTip =
        new Tip(
            UUID.randomUUID(),
            inputDestinationId,
            UUID.randomUUID(),
            "test tip",
            Instant.now());

    tipStore.addTip(inputTip);
    Tip resultTip = tipStore.getTipsInDestination(inputDestinationId).get(0);

    assertEquals(inputTip, resultTip);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputTip);
  }

  private void assertEquals(Tip expectedTip, Tip actualTip) {
    Assert.assertEquals(expectedTip.getId(), actualTip.getId());
    Assert.assertEquals(expectedTip.getDestinationId(), actualTip.getDestinationId());
    Assert.assertEquals(expectedTip.getAuthorId(), actualTip.getAuthorId());
    Assert.assertEquals(expectedTip.getContent(), actualTip.getContent());
    Assert.assertEquals(expectedTip.getCreationTime(), actualTip.getCreationTime());
  }
}
