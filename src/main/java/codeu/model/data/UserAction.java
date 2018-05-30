package codeu.model.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class UserAction {
    private Instant time;
    private String name;
    private char type;
    private String chatName = null;
    private String context = null;
    private static List<UserAction> allActions = new ArrayList<>();


    /**
     * Constructs a new User.
     *
     * @param creation is the time the action occured
     * @param name the name of the user
     * @param type is the type of action : new user (u) , new chat (c) , new message (m)
     * @param chatName name of the chat the action relates to
     * @param context is the content of the sent message
     */
    // this constructor is for sent message action
    public UserAction(Instant creation, String name, char type, String chatName, String context) {
        this.time = creation;
        this.name = name;
        this.type = type;
        this.chatName = chatName;
        this.context = context;

    }
    // for new chat created
    public UserAction(Instant creation, String name, char type, String chatName) {
        this.time = creation;
        this.name = name;
        this.type = type;
        this.chatName = chatName;
    }

    // for new user
    public UserAction(Instant creation, char type, String name) {
        this.type = type;
        this.time = creation;
        this.name = name;
    }

    public char getType() {
        return this.type;
    }

    public void addAction(UserAction a) {
        allActions.add(a);
    }

    public List<UserAction> returnActions() {
        return allActions;
    }
}
