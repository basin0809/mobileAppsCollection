package Assignment4;


import java.util.List;
import javax.persistence.EntityManager;

public class SolutionIV {

	public static void printDocumentInfo(EntityManager manager, String title){
		javax.persistence.Query q = manager.createQuery
				("select d,t from Document d, Team t " +
		    	 "where d.createdBy=t.id " +
		    	 "and d.title =:name");
		q.setParameter("name", title);
		
		for(Object[] object : (List<Object[]>) q.getResultList()){
			System.out.println(object[0]);
			System.out.println(object[1]);
			int teamId = ((Team)object[1]).getId();
			javax.persistence.Query q2 = manager.createQuery
					("select p " +
			    	 "from Team t, Membership m, Person p, Role r, RoleType rt " +
			    	 "where t.id=:teamId " +
			    	 "and m.isMemberOf=t.id " +
			    	 "and m.hasMember=p.id " +
			    	 "and m.isMemberOf = r.isMemberOf " +
			    	 "and m.hasMember=r.hasMember " +
			    	 "and m.start < d.createdOn " +
			    	 "and (m.end is NULL or (m.end > d.createdOn))" +
			    	 "and r.hasRole=rt.id" +
			    	 "and (rt.type = 'Chair' or rt.type = 'Secretary') ");
			q2.setParameter("teamId", teamId);
			for(Object object2 : (List<Object>) q2.getResultList()){
				System.out.println(object2);
				}
			System.out.println("---------------------");
		}
	}
}
