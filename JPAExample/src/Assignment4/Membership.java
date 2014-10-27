package Assignment4;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import Apartment.Building;

/**
 * Membership persistent entity.
 * <pre>
	create table Membership(
	  hasMember int,
	  isMemberOf int,
	  start date not null,
	  end date,
	  foreign key(hasMember) references Person(id) on update cascade on delete cascade,
	  foreign key(isMemberOf) references Team(id) on update cascade on delete cascade,
	  primary key(hasMember, isMemberOf)
	);
 * </pre>
 * <p>
 * @author Xipeng Wang
 */
@Entity
@Access(FIELD)
@IdClass(MembershipEmbeddedId.class)
public class Membership {
	
	/** start attribute. */
    @Column(name="start", nullable=false)
    public java.util.Date start;

    /** end attribute. */
    @JoinColumn(name="end")
    public java.util.Date end;
	
	/** The person. */
    @Id
    @ManyToOne(cascade=ALL, fetch=EAGER)
    @JoinColumn(name="hasMember")
    private Person hasMember;

    /** The team. */
    @Id
    @ManyToOne(cascade=ALL, fetch=EAGER)
    @JoinColumn(name="isMemberOf")
    private Team isMemberOf;

    /**
     * Role multivalued attribute.
     * <pre>
     create table Role(
		  hasMember int,
		  isMemberOf int,
		  hasRole int,
		  foreign key(hasMember, isMemberOf) references Membership(hasMember, isMemberOf) on update cascade on delete cascade,
		  foreign key(hasRole) references RoleType(id) on update cascade on delete cascade,
		  primary key(hasMember, isMemberOf, hasRole)
     * </pre>
     * Note that @Column(name="hasRole") specifies the name
     * of the column in the collection table. The default
     * is "element". The other column of the collection
     * table is specified by joinColumns. The name of
     * the Java field is "roles" and does not correspond
     * to anything in the database.
     * <p>
     * Setting fetch to EAGER ensures that when a membership
     * is retrieved, the roles set will also be retrieved.
     * By default, multivalued attributes are not retrieved.
     */
    @ElementCollection(fetch=EAGER)
    @Column(name="hasRole")
    @CollectionTable(name="Role", 
    joinColumns={@JoinColumn(name="isMemberOf"),
    		     @JoinColumn(name="hasMember")})
    private Set<RoleType> roles;
    
    /**Constructor**/
    public Membership(java.util.Date start, java.util.Date end, Person person, Team team, Set roles)
    	{this.start = start;
    	this.end =end;
    	this.hasMember=person;
    	this.isMemberOf=team;
    	this.roles=roles;}
    
    
    
    

}