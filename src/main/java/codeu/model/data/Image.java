package codeu.model.data;

import com.google.appengine.api.datastore.Text;
import java.time.Instant;
import java.util.UUID;

public class Image {
    private Text filename;
    private String destination;
    private UUID id;
    private final Instant creation;

    /**
     * Constructs a new Image
     *
     * @param filename delegated filename url
     * @param destination destination of photo
     */
    public Image(Text filename, String destination, UUID id, Instant creation) {
        this.filename = filename;
        this.destination = destination;
        this.id = id;
        this.creation = creation;
    }

    public Text returnFilename() { return this.filename; }

    public String returnDestination() { return destination; }

    public UUID getId() { return this.id; }

    public Instant getCreation() { return this.creation; }
}
