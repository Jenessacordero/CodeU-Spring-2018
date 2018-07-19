package codeu.model.store.basic;

import codeu.model.data.Banner;
import codeu.model.data.Image;
import codeu.model.data.UserAction;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.appengine.api.datastore.Text;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class BannerStoreTest {

    private BannerStore bannerStore;
    private PersistentStorageAgent mockPersistentStorageAgent;

    private final Banner one =
            new Banner(new Text("one"), "one", UUID.randomUUID(), Instant.now());
    private final Banner two =
            new Banner(new Text("two"), "two", UUID.randomUUID(), Instant.now());
    private final Banner three =
            new Banner(new Text("three"), "three", UUID.randomUUID(), Instant.now());

    @Before
    public void setup() {
        mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
        bannerStore = BannerStore.getTestInstance(mockPersistentStorageAgent);

        final HashMap<String, Banner> bannerList = new HashMap<>();
        bannerList.put("one", one);
        bannerList.put("two", two);
        bannerList.put("three", three);
        bannerStore.setBanner(bannerList);
    }

    @Test
    public void testReturnImages() {
        HashMap<String, Banner> resultBanners = bannerStore.returnBanners();

        Assert.assertEquals(3, resultBanners.size());
        assertEquals(one, resultBanners.get("one"));
        assertEquals(two, resultBanners.get("two"));
        assertEquals(three, resultBanners.get("three"));
    }

    @Test
    public void testAddUserAction() {
        Banner banner = new Banner(new Text("test"), "test", UUID.randomUUID(), Instant.now());

        bannerStore.addBanner(banner.returnDestination(), banner);
        Banner resultBanner = bannerStore.returnBanners().get("test");

        assertEquals(banner, resultBanner);
        Mockito.verify(mockPersistentStorageAgent).writeThrough(banner);
    }

    private void assertEquals(Banner expected, Banner actual) {
        Assert.assertEquals(expected.returnID(), actual.returnID());
        Assert.assertEquals(expected.returnDestination(), actual.returnDestination());
        Assert.assertEquals(expected.returnBanner(), actual.returnBanner());
    }
}
