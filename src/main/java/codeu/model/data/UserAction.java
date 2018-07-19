package codeu.model.data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.UUID;

/** Class representing a UserAction, which is an action committed by a user. (e.g. Creating a status update, sending a message, creating
 * a profile, etc.)
 *  */
public class UserAction {
	private final UUID id;
    private final UUID user;
    private final String message;
    public final Instant creation;

    /**
     * Constructs a new UserAction.
     *
     * @param UUID of the UserAction
     * @param UUID of the user
     * @param message which states what the UserAction is
     * @param creation the creation time of this UserAction 
     */
    public UserAction(UUID id, UUID user, String message, Instant creation) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.creation = creation;
    }
    
    /** Returns the ID of this UserAction. */
    public UUID getId() {
      return id;
    }
    
    /** Returns the ID of the UserAction's User. */
    public UUID getUserId() {
      return user;
    }
    
    /** Returns the message of this UserAction. */
    public String getMessage() {
      return message;
    }
    
    /** Returns the creation time of this UserAction. */
    public Instant getCreationTime() {
      return creation;
    }
    
    /** Returns the formatted time of this UserAction. */
    public String getFormattedTime() {
    	DateTimeFormatter formatter =
    		    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    		                     .withLocale(Locale.US)
    		                     .withZone(ZoneId.systemDefault());
    	Instant instant = this.getCreationTime();
    	String output = formatter.format(instant);
    	
    	return output;
    }

}
