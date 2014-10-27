package Assignment4;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.EAGER;

/**
 * Person persistent entity.
 * <pre>
	create table Person(
	  id int primary key auto_increment,
	  name varchar(100) not null
	);
 * </pre>
 * @author Xipeng Wang
 */
@Entity
@Access(FIELD)
public class Person {
    /** Primary key. */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    /** name attribute. */
    @Column(name="name", length=100, nullable =false)
    private String name;
    
    /**Constructor**/
    public Person(String name){this.name = name;}
    }
