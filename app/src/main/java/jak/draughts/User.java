package jak.draughts;

/**
 * The User class is converted to a JSON object and stored in
 * Firebase. The variable <tt>id</tt> stores the document
 * reference id, pointing to the document containing
 * the relevant user object.
 */
public class User {

    private String id; // aka document id, containing the user
    private String name; // username

    public User() {}

    public User(String id) {
        this(id, "Guest");
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
