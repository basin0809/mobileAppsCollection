package Apartment;
import java.util.Set;
import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.EAGER;

/**
 * Apartment persistent entity.
 * <pre>
 * create table Apartment (
 *   id int primary key,
 *   number varchar(31) not null,
 *   building int not null references Building(id)
 *     on update cascade on delete cascade,
 *   unique(building, number)
 * );
 * </pre>
 * <p>
 * Copyright &#169; 2010 Ken Baclawski. All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * <ol>
 * <li>Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 * <li>Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * </ol>
 * <p>
 * THIS SOFTWARE IS PROVIDED BY KEN BACLAWSKI ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL KEN BACLAWSKI OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * The views and conclusions contained in the software and documentation are
 * those of the author and should not be interpreted as representing official
 * policies, either expressed or implied, of Ken Baclawski.
 * @author Ken Baclawski
 */
@Entity
@Access(FIELD)
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"building", "number"}))
public class Apartment {
    /** Primary key. */
    @Id
    public int id;

    /** Number attribute. */
    @Column(name="number", length=31, nullable=false)
    public String number;

    /** Building attribute. */
    @ManyToOne
    @JoinColumn(name="building", nullable=false)
    public Building building;

    /**
     * ApartmentFloor multivalued attribute.
     * <pre>
     * create table ApartmentFloor (
     *   apartment int references Apartment(id)
     *     on update cascade on delete cascade,
     *   floor int,
     *   primary key(apartment, floor)
     * );
     * </pre>
     * Note that @Column(name="floor") specifies the name
     * of the column in the collection table. The default
     * is "element". The other column of the collection
     * table is specified by joinColumns. The name of
     * the Java field is "floors" and does not correspond
     * to anything in the database.
     * <p>
     * Setting fetch to EAGER ensures that when an apartment
     * is retrieved, the floors set will also be retrieved.
     * By default, multivalued attributes are not retrieved.
     */
    @ElementCollection(fetch=EAGER)
    @Column(name="floor")
    @CollectionTable(name="ApartmentFloor", joinColumns=@JoinColumn(name="apartment"))
    public Set<Integer> floors;

    /**
     * Owner association.
     * <pre>
     * create table Owner (
     *   person int references Person(id)
     *     on update cascade on delete cascade,
     *   apartment int references Apartment(id)
     *     on update cascade on delete cascade
     *   primary key(person, apartment)
     * );
     * </pre>
     * The name of the join table as well as its column
     * names are specified with the @JoinTable annotation.
     * The name of the Java field is "owners" and does not
     * correspond to anything in the database. The owners
     * attribute is used in queries, while the join table
     * name as well as the join table column names are
     * never used in queries.
     * <p>
     * Setting fetch to EAGER ensures that when an apartment
     * is retrieved, the owners set will also be retrieved.
     * By default, they are not retrieved.
     */
    @ManyToMany(fetch=EAGER)
    @JoinTable(name="Owner",
	       joinColumns=@JoinColumn(name="apartment"),
	       inverseJoinColumns=@JoinColumn(name="person"))
    public Set<Person> owners;

    /**
     * Update the set of owners.
     * OpenJPA will not detect updates made to fields, and as a result will not
     * persist changes made to them. This seems to be a bug. The OpenJPA
     * documentation claims that if a field has a standard collection type then
     * updates will be detected and persisted. Since owners has a standard
     * collection type (Set), updates should be persisted, but they are not.
     * <p>
     * The enhancer generates a method that persists the change to the owners
     * field, and it can be called directly. Of course, it is only available
     * after the class has been enhanced. It is a static method with the
     * following signature:
     * <pre>
     * public static void pcSetowners(Apartment a, Set<Person> s)
     * </pre>
     * <p>
     * Another way to ensure that the update is detected is to call the dirty
     * method of the EntityManager. Unfortunately, this is not in the
     * EntityManager interface even though it is in the implementation.  So it
     * must be called reflectively.
     * @param owners The new set of owners.
     */
    public void setOwners(Set<Person> owners) {
	this.owners = owners;
    }
}