package CompositePrimaryKey;
import javax.persistence.*;

/**
 * A city.
 * @author Ken Baclawski
 */
@Entity
public class City {
    /** The name of the city is the primary key. */
    @Id
    private String id;

    /**
     * Construct a city.
     * @param name The name of the city.
     */
    public City(String name) {
        id = name;
    }
}