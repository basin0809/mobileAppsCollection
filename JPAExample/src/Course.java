
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * University Course example of a JPA annotated class.
 * The Javadoc comments were omitted to make it easier to display the example.
 * @author Ken Baclawski
 */
@Entity
@Access(AccessType.FIELD)
public class Course {
    @Id @GeneratedValue
    public int code;
    @Basic
    public String name;
    @Basic
    public String description;
    @ManyToMany(targetEntity=Student.class, mappedBy="enrollment")
    public Set<Student> roster = new HashSet<Student>();

    public Course() {}
    public Course(String name, String description) {
	this.name = name;
	this.description = description;
    }
    public String toString() {
	return "Course " + code + " " + name + " " + description;
    }
}
