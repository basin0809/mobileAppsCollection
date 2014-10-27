import java.sql.*;
import java.sql.Date;
import java.util.*;
/*
 Develop a Java method using JDBC that finds the documents having a specified first part of the title. 
 For each such document print its title, the name of the team that produced it, 
 and the names of the chairs and secretaries of the team at the time when the document was created. 
 The method should look like this:
 public void printDocumentInfo(Connection connection, String firstPartOfTitle) ...
 For each document print its title and team exactly once no matter how many chairs and secretaries there are.
 * */
public class SolutionI {

    public SolutionI() {}
    
    public void printDocumentInfo(Connection connection, String firstPartOfTitle) throws SQLException{
    	String sql="select d.id, d.title, d.createdOn, d.content, t.name from Document d, Team t " +
    			   "where d.createdBy=t.id and d.title like '?%'";
    	String sql2 = sql.replace("?",firstPartOfTitle);
    	PreparedStatement findDocuments = connection.prepareStatement(sql2);
    	ResultSet documents = findDocuments.executeQuery();
    	PreparedStatement findTeamInfo = connection.prepareStatement
    	("select t.name, p.id, p.name, m.start, m.ending, rt.typeName " +
    			"from Team t, Document d, Membership m, Person p, Roler r, RoleType rt " +
    			"where d.id=? and d.createdBy=t.id and m.isMemberOf=t.id and  m.hasMember=p.id and m.isMemberOf = r.isMemberOf and m.hasMember=r.hasMember and r.hasRole=rt.id");
    	while (documents.next()) {
			
			findTeamInfo.setInt(1, documents.getInt(1));
			ResultSet teamInfo = findTeamInfo.executeQuery();
			System.out.println("Title: "+documents.getString(2)+"\n"+"Team name: "+documents.getString(5));
			while (teamInfo.next()) {
				if(teamInfo.getDate(4).before(documents.getDate(3)) && 
				  (teamInfo.getDate(5)== null ? true:teamInfo.getDate(5).after(documents.getDate(3))) 
				  &&(teamInfo.getString(6).equals("Chair") || teamInfo.getString(6).equals("Secretary"))
				  ){
			
					System.out.println("name:"+teamInfo.getString(3)+" role: "+teamInfo.getString(6));
				}
				
			}
			
			System.out.println();
		}
    } 
    public static void main(String...args) throws Exception{
   	SolutionI solutionI = new SolutionI();
    	try {
    	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    	    Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=AssignmentIV", "sa", "19900809");
    	    solutionI.printDocumentInfo(connection, "Westmarch");
    	   
    	} catch (Exception e) {
    	    System.err.println("Unable to connect to the database due to " + e);
    	}
    	
    }
}

