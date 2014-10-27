package CompositePrimaryKey;
import javax.persistence.*;

/**
 * A locater specifies a city and province.
 * @author Ken Baclawski
 */
@Embeddable
public class Locater {
    /** The city. */
    String city;
    /** The province. */
    String province;

    /**
     * Test for equality of two locaters.
     * @param object The object with which to compare.
     * @return Whether the object is the same locater as this one.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Locater)) {
            return false;
        }
        Locater locater = (Locater)object;
        return city.equals(locater.city) && province.equals(locater.province);
    }

    /**
     * Compute a hash value compatible with equality.
     * @return The hash value of the locater.
     */
    @Override
    public int hashCode() {
        return city.hashCode() + province.hashCode();
    }
}