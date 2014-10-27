

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * University Student example of a JPA annotated class.
 * The Javadoc comments were omitted to make it easier to display the example.
 * @author Ken Baclawski
 */
@Entity
@Access(AccessType.FIELD)
public class Student {
    @Basic
    public String name;
    @Basic
    public String email;
    @Basic @Temporal(TemporalType.DATE)
    public Date matriculation;
    @ManyToMany
    @JoinTable(name="Enrollment",
	       joinColumns=@JoinColumn(name="student"),
        inverseJoinColumns=@JoinColumn(name="course", referencedColumnName="code")
    )
    public Set<Course> enrollment = new HashSet<Course>();

    public Student() {}
    public Student(String name, String email) {
	this.name = name;
	this.email = email;
	this.matriculation = new Date();
    }
    public String toString() {
	return "Student " + name + " " + email + " year " + matriculation;
    }
}
