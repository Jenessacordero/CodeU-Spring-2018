package codeu.model.data;

import com.google.appengine.api.datastore.Text;
import java.time.Instant;
import java.util.UUID;

public class Banner {
    public Text banner;
    public String destination;
    public UUID id;
    public final Instant creation;

    public Banner(Text filename, String destination, UUID id, Instant time) {
        this.banner = filename;
        this.creation = time;
        this.id = id;
        this.destination = destination;
    }

    public Text returnBanner() {
        return banner;
    }

    public UUID returnID() {
        return id;
    }

    public Instant returnCreation() {
        return creation;
    }

    public String returnDestination() {
        return destination;
    }
}
