package CompositePrimaryKey;


import javax.persistence.*;

/**
 * A province.
 * @author Ken Baclawski
 */
@Entity
public class Province {
    /** The name of the province is the primary key. */
    @Id
    private String id;

    /**
     * Construct a province.
     * @param name The name of the province.
     */
    public Province(String name) {
        id = name;
    }
}