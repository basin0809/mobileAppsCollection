
import java.util.List;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/** 
 * A very simple, stand-alone program that stores a new entity in the
 * database and then performs a query to retrieve it.
 * @author Ken Baclawski
 */
public class Test {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

	// Create the new objects to be persisted.
	Student s = new Student("Fred", "fred@neu.edu");
	Course c = new Course("CS5200", "Database Management");
	s.enrollment.add(c);

	// Configure and create the factory.
	java.util.Map<Object,Object> map = new java.util.HashMap<Object,Object>();
	map.put("openjpa.ConnectionUserName", "root");
	map.put("openjpa.ConnectionPassword", "basin576095");
	map.put("openjpa.ConnectionURL", "jdbc:mysql://127.0.0.1/basin");
	map.put("openjpa.ConnectionDriverName", "com.mysql.jdbc.Driver");
	map.put("openjpa.jdbc.SynchronizeMappings", "buildSchema");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("university", map);

        // Create a new EntityManager from the EntityManagerFactory. The
        // EntityManager is the main object in the persistence API, and is
        // used to create, delete, and query objects, as well as access
        // the current transaction
        EntityManager em = factory.createEntityManager();

        // Begin a new local transaction so that we can persist a new entity
        em.getTransaction().begin();

        // Create and persist a new Message entity
        em.persist(s);
        em.persist(c);

        // Commit the transaction, which will cause the entity to
        // be stored in the database
        em.getTransaction().commit();

        // It is always good practice to close the EntityManager so that
        // resources are conserved.
        em.close();

        // Using a new entity manager, iterate over all students, then courses.
        em = factory.createEntityManager();
        javax.persistence.Query q = em.createQuery("select s,e from Student s join s.enrollment e");
        for (Object object : (List<Object>) q.getResultList()) {
	    for (Object o : (Object[])object) {
		System.out.println(o);
	    }
        }
        em.close();

        // Now query over the courses, then students.
        em = factory.createEntityManager();
        q = em.createQuery("select c,s from Course c join c.roster s");
        for (Object object : q.getResultList()) {
	    for (Object o : (Object[])object) {
		System.out.println(o);
	    }
        }
        em.close();

        factory.close();
    }
}

