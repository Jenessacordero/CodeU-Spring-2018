/** Class creates a new Image object including an image ID as well as a filename */

package codeu.model.data;

import java.util.UUID;

public class Images {
    private String filename;
    private final UUID id;


    /**
     * Constructs a new Image
     *
     *
     * @param filename delegated filename with extension (.jpg, .png)
     * @param id random generated ID
     */
    public Images(String filename, UUID id) {
        this.filename = filename;
        this.id = id;
    }

    public String returnFileName() {
        return this.filename;
    }

    public UUID getID() {
        return this.id;
    }
}
