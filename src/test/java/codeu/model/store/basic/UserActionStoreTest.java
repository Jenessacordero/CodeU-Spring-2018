package codeu.model.store.basic;

import codeu.model.data.UserAction;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class UserActionStoreTest {

  private UserActionStore userActionStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UserAction USER_ACTION_ONE =
      new UserAction(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "action one",
          Instant.ofEpochMilli(1000));
  private final UserAction USER_ACTION_TWO =
      new UserAction(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "action two",
          Instant.ofEpochMilli(2000));
  private final UserAction USER_ACTION_THREE =
      new UserAction(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "action three",
          Instant.ofEpochMilli(3000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    userActionStore = UserActionStore.getTestInstance(mockPersistentStorageAgent);

    final List<UserAction> userActionList = new ArrayList<>();
    userActionList.add(0, USER_ACTION_ONE);
    userActionList.add(0, USER_ACTION_TWO);
    userActionList.add(0, USER_ACTION_THREE);
    userActionStore.setUserActions(userActionList);
  }

  @Test
  public void testReturnAllUserActions() {
    List<UserAction> resultUserActions = userActionStore.returnAllUserActions();

    Assert.assertEquals(3, resultUserActions.size());
    assertEquals(USER_ACTION_ONE, resultUserActions.get(2));
    assertEquals(USER_ACTION_TWO, resultUserActions.get(1));
    assertEquals(USER_ACTION_THREE, resultUserActions.get(0));
  }

  @Test
  public void testAddUserAction() {
    UserAction inputUserAction =
        new UserAction(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test action",
            Instant.now());

    userActionStore.addUserAction(inputUserAction);
    UserAction resultUserAction = userActionStore.returnAllUserActions().get(0);

    assertEquals(inputUserAction, resultUserAction);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputUserAction);
  }

  private void assertEquals(UserAction expectedUserAction, UserAction actualUserAction) {
    Assert.assertEquals(expectedUserAction.getId(), actualUserAction.getId());
    Assert.assertEquals(expectedUserAction.getUserId(), actualUserAction.getUserId());
    Assert.assertEquals(expectedUserAction.getMessage(), actualUserAction.getMessage());
    Assert.assertEquals(expectedUserAction.getCreationTime(), actualUserAction.getCreationTime());
  }
}
