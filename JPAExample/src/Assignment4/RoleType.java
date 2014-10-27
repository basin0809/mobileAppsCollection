package Assignment4;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.EAGER;

/**
 * Person persistent entity.
 * <pre>
	create table RoleType(
	  id int primary key auto_increment,
	  type varchar(100) not null
	);
 * </pre>
 * @author Xipeng Wang
 */
@Entity
@Access(FIELD)
public class RoleType {
    /** Primary key. */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    /** type attribute. */
    @Column(name="type", length=100, nullable =false)
    private String type;
    
    /**Constructor**/
    public RoleType(String name){this.type = name;}
    }