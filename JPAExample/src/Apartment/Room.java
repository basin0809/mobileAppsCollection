package Apartment;


import javax.persistence.*;
import static javax.persistence.AccessType.FIELD;

/**
 * Room persistent entity.
 * <pre>
 * create table Room (
 *   id int primary key,
 *   area double not null,
 *   type int not null references RoomType(id)
 *     on update cascade,
 *   apartment int not null references Apartment(id)
 *     on update cascade on delete cascade
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
public class Room {
    /** Primary key. */
    @Id
    public int id;

    /** Area attribute. */
    @Column(nullable=false)
    public double area;

    /** Type association. */
    @ManyToOne
    @JoinColumn(name="type", nullable=false)
    public RoomType type;

    /** Apartment association. */
    @ManyToOne
    @JoinColumn(name="apartment", nullable=false)
    public Apartment apartment;
}