package codeu.model.data;

import com.google.appengine.api.datastore.Text;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

public class BannerTest {
        @Test
        public void testCreate() {
            UUID id = UUID.randomUUID();
            String destination = "random";
            Text filename = new Text("random");
            Instant now = Instant.now();

            Banner banner = new Banner(filename, destination, id, now);

            Assert.assertEquals(banner.returnID(), id);
            Assert.assertEquals(destination, banner.returnDestination());
            Assert.assertEquals(filename, banner.returnBanner());
            Assert.assertEquals(now, banner.returnCreation());
        }
}
