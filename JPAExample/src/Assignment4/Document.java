package Assignment4;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.EAGER;

/**
 * Document persistent entity.
 * <pre>
	create table Document(
	  id int primary key auto_increment,
	  title varchar(1000),
	  createdBy int not null,
	  createdOn date not null,
	  content longtext,
	  foreign key(createdBy) references Team(id) on update cascade on delete no action,
	);
 * </pre>
 * @author Xipeng Wang
 */
@Entity
@Access(FIELD)
public class Document {
    /** Primary key. */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    /** title attribute. */
    @Column(name="title", length=1000)
    private String title;
    
    /** content attribute. */
    @Lob
    @Column(name="content")
    private String content;
    
    /** createdBy attribute. */
    @ManyToOne
    @JoinColumn(name="createdBy", nullable=false)
    private Team createdBy;
    
    /** createdOn attribute. */
    @Column(name="createdOn", nullable=false)
    private Date createdOn;
    
    /**Constructor**/
    public Document(String name, String content, Team team, Date date)
    	{this.title = name;
    	this.content =content;
    	this.createdBy=team;
    	this.createdOn=date;}
    
    /**Get method**/
    public int getId(){return this.id;}
    public Team getCreatedBy(){return this.createdBy;}
    }
