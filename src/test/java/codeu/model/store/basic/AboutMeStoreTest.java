package codeu.model.store.basic;

import codeu.model.data.AboutMe;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class AboutMeStoreTest {

  private AboutMeStore aboutMeStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UUID OWNER_ID_ONE = UUID.randomUUID();
  private final UUID OWNER_ID_TWO = UUID.randomUUID();
  private final UUID OWNER_ID_THREE = UUID.randomUUID();
  private final AboutMe ABOUTME_ONE =
      new AboutMe(
          UUID.randomUUID(),
          OWNER_ID_ONE,
          "bio one",
          Instant.ofEpochMilli(1000));
  private final AboutMe ABOUTME_TWO =
      new AboutMe(
          UUID.randomUUID(),
          OWNER_ID_TWO,
          "bio two",
          Instant.ofEpochMilli(2000));
  private final AboutMe ABOUTME_THREE =
      new AboutMe(
    		  UUID.randomUUID(),
              OWNER_ID_THREE,
              "bio three",
              Instant.ofEpochMilli(3000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    aboutMeStore = AboutMeStore.getTestInstance(mockPersistentStorageAgent);

    final List<AboutMe> aboutMeList = new ArrayList<>();
    aboutMeList.add(ABOUTME_ONE);
    aboutMeList.add(ABOUTME_TWO);
    aboutMeList.add(ABOUTME_THREE);
    aboutMeStore.setAboutMes(aboutMeList);
  }

  @Test
  public void testGetAboutMeByUser() {
    AboutMe resultAboutMe = aboutMeStore.getAboutMeByUser(OWNER_ID_ONE);

    assertEquals(ABOUTME_ONE, resultAboutMe);
  }

  @Test
  public void testAddAboutMe() throws PersistentDataStoreException {
    AboutMe newAboutMe =
        new AboutMe(
            UUID.randomUUID(),
            OWNER_ID_ONE,
            "bio four",
            Instant.now());

    aboutMeStore.addAboutMe(newAboutMe);
    AboutMe resultAboutMe = aboutMeStore.getAboutMeByUser(OWNER_ID_ONE);

    assertEquals(newAboutMe, resultAboutMe);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(newAboutMe);
  }

  private void assertEquals(AboutMe expectedAboutMe, AboutMe actualAboutMe) {
    Assert.assertEquals(expectedAboutMe.getId(), actualAboutMe.getId());
    Assert.assertEquals(expectedAboutMe.getOwner(), actualAboutMe.getOwner());
    Assert.assertEquals(expectedAboutMe.getContent(), actualAboutMe.getContent());
    Assert.assertEquals(expectedAboutMe.getCreationTime(), actualAboutMe.getCreationTime());
  }
}
