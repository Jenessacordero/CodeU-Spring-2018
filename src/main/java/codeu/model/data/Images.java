/** Class creates a new Image object including an image ID as well as a filename */

package codeu.model.data;

import java.util.UUID;

public class Images {
    private String filename;
    private String destination;
    private UUID id;


    /**
     * Constructs a new Image
     *
     *
     * @param filename delegated filename with extension (.jpg, .png)
     * @param destination
     */
    public Images(String filename, String destination, UUID id) {
        this.filename = filename;
        this.destination = destination;
        this.id = id;

    }

    public String returnFileName() {
        return this.filename;
    }

    public String returnDestination() { return this.destination; }

    public UUID getID() { return this.id; }
}
