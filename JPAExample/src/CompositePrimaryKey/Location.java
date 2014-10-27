package CompositePrimaryKey;


import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.CascadeType.ALL;

@Entity
@Access(FIELD)
@IdClass(Locater.class)
public class Location {
    /** The city. */
    @Id
    @ManyToOne(cascade=ALL, fetch=EAGER)
    @JoinColumn(name="city")
    private City city;

    /** The province. */
    @Id
    @ManyToOne(cascade=ALL, fetch=EAGER)
    @JoinColumn(name="province")
    private Province province;

    /**
     * Construct a location.
     * @param city The city.
     * @param province The province.
     */
    public Location(City city, Province province) {
        this.city = city;
        this.province = province;
    }
}