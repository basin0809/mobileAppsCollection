package Apartment;

import java.util.Set;
import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.FetchType.EAGER;

/**
 * RoomType persistent entity.
 * <pre>
 * create table RoomType (
 *   id int primary key,
 *   description varchar(5000)
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
public class RoomType {
    /** Primary key. */
    @Id
    public int id;

    /** Description attribute. */
    @Column(length=5000)
    public String description;

    /**
     * Multivalued attribute names.
     * <pre>
     * create table RoomTypeName (
     *   type int references RoomType(id)
     *     on update cascade on delete cascade,
     *   name varchar(255),
     *   primary key(type, name)
     * );
     * </pre>
     * Note that @Column(name="name") specifies the name
     * of the column in the collection table. The default
     * is "element". The other column of the collection
     * table is specified by joinColumns. The name of
     * the Java field is "names" and does not correspond
     * to anything in the database. The names
     * attribute is used in queries, while the join table
     * name as well as the join table column names are
     * never used in queries.
     * <p>
     * Setting fetch to EAGER ensures that when a type
     * is retrieved, the names set will also be retrieved.
     * By default, multivalued attributes are not retrieved.
     */
    @ElementCollection(fetch=EAGER)
    @Column(name="name")
    @CollectionTable(name="RoomTypeName", joinColumns=@JoinColumn(name="type"))
    public Set<String> names;
}