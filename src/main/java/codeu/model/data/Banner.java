package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

public class Banner {
    public String banner;
    public String destination;
    public UUID id;
    public final Instant creation;

    public Banner(String filename, String destination, UUID id, Instant time) {
        this.banner = filename;
        this.creation = time;
        this.id = id;
        this.destination = destination;
    }

    public String returnBanner() {
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
