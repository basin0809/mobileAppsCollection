package Assignment4;

import java.util.Set;
import javax.persistence.*;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.EAGER;

/**
 * Team persistent entity.
 * <pre>
	create table Team(
	  id int primary key auto_increment,
	  name varchar(50) not null
	);
 * </pre>
 * @author Xipeng Wang
 */
@Entity
@Access(FIELD)
public class Team {
    /** Primary key. */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    /** name attribute. */
    @Column(name="name", length=50, nullable=false)
    private String name;

    /**Constructor**/
    public Team(String name){this.name = name;}
    
    /**Get method**/
    public int getId(){return this.id;}
    
    }
