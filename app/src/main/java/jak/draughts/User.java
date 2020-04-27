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
    private Long rank; // ranking, to be implemented

    public User() {}

    public User(String id, String name, Long rank) {
        this.id = id;
        this.name = name;
        this.rank = rank;
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

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }
}
