package codeu.model.data;

import com.google.appengine.api.datastore.Text;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;

public class ImageTest {

    @Test
    public void testCreate() {
        UUID id = UUID.randomUUID();
        String destination = "random";
        Text filename = new Text("random");
        Instant now = Instant.now();

        Image image = new Image(filename, destination, id, now);

        Assert.assertEquals(image.getId(), id);
        Assert.assertEquals(destination, image.returnDestination());
        Assert.assertEquals(filename, image.returnFilename());
        Assert.assertEquals(now, image.getCreation());
    }
}
