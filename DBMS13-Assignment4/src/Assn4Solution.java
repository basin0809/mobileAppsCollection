import java.sql.*;
import java.util.*;

public class Assn4Solution {
	
	/*
	 * The data connection
	 * */
	private Connection connection;
	
	/*
	 * connect database
	 * */
	public Assn4Solution(String driver, String url, String user, String password){
		
		try{
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user,password);
			
		}catch(Exception e){
			System.err.println("Unable to connect to the database due to " + e);
		}
	}
	
	/*
	 * solution for q1
	 * finds the documents having a specified first part of the title. 
	 * For each such document print its title, the name of the team that produced it, 
	 * and the names of the chairs and secretaries of the team at the time when the 
	 * document was created
	 * */
	public static void printDocumentInfoZh(Connection connection, String fisrtPartOfTitle) throws SQLException{
		PreparedStatement pdf = connection.prepareStatement
				("select d.title, t.name, p.name " +
				 "from Document d, Team t, Person p, Membership m, Roler r, RoleType rt " +
				 "where p.id = m.hasMember " +
				 " and  t.id = m.isMemberOf " +
				 " and  d.createdBy = t.id " +
				 " and  m.hasMember = r.hasMember " +
				 " and  m.isMemberOf = r.isMemberOf " +
				 " and  r.hasRole = rt.id " +
				 " and  d.title like ? " +
				 " and (rt.typeName = ? or rt.typeName = ?) " +
				 " and  m.start <= d.createdOn " +
				 " and  (m.ending is NULL or m.ending >= d.createdOn) " +
				 "order by d.title, t.name");
		pdf.setString(1, fisrtPartOfTitle + "%");
		pdf.setString(2, "Chair");
		pdf.setString(3, "Secretary");
		
		ResultSet rs = pdf.executeQuery();
		String tmpTitle, tmpName,currTitle, currName;
		tmpTitle = tmpName = "";
		while(rs.next()){
			currTitle = rs.getString(1);
			currName = rs.getString(2);
			if(currTitle.equals(tmpTitle) && currName.equals(tmpName))
				;
			else{
				System.out.print("Title: " + currTitle + "\n");
				tmpTitle = currTitle;
				
				System.out.println("Team Name: " + currName + "\n");
				tmpName = currName;	
			}
			System.out.println("chair/secretery： " + rs.getString(3));
		}
		
	}



	
	public static void printDocumentInfoYi(Connection connection, String firstPartOfTitle) throws SQLException {
		PreparedStatement findDocuments = connection.prepareStatement
		    ("select d.title, t.name, p.name from Document d, Team t, Membership m, Person p, RoleType rt, Roler r " +
		    		"where d.createdBy = t.id " +
		    		"and d.title like ? "+
		    		"and m.isMemberOf = t.id " +
		    		"and m.hasMember = p.id " +
		    		"and r.hasMember = p.id " +
		    		"and r.isMemberOf = t.id " +
		    		"and r.hasRole = rt.id " +
		    		"and (rt.typeName = 'Chair' or rt.typeName = 'Secretary') " +
		    		"and m.start < d.createdOn " +
		    		"and (m.ending is NULL or m.ending > d.createdOn)" +
		    		"order by d.title, t.name;");
		
		
		
		
		
		findDocuments.setString(1, firstPartOfTitle+"%");
		ResultSet documents = findDocuments.executeQuery();
		String tempTitle = "";
		String tempName = "";
		while(documents.next()){
			String currentTitle = documents.getString(1);
			String currentTeamName = documents.getString(2);
			String personName = documents.getString(3);
			if(!currentTitle.equals(tempTitle)||!currentTeamName.equals(tempName)){
				System.out.println("Document Title: "+currentTitle+"  "+"Team Name: "+currentTeamName);
				tempName = currentTeamName;
				tempTitle = currentTitle;
			}
			System.out.println("Person Names: "+personName);
		}
    }

	
	
	public static void printDocumentInfoJi(Connection connection, String firstPartOfTitle) throws SQLException{
		PreparedStatement retrieveDocument=connection.prepareStatement
				("select d.title, t.name, rt.typeName, p.name from Membership m, Roler r,Team t, Person p, Document d, RoleType rt "+
				 "where m.hasMember = p.id "+
				 "and t.id = m.isMemberOf "+
				 "and m.hasMember = r.hasMember "+
				 "and m.isMemberOf = r.isMemberOf "+
				 "and r.hasRole = rt.id "+
				 "and (rt.typeName = 'Chair' or rt.typeName = 'Secretary') "+
				 "and (m.ending > d.createdOn or m.ending is NULL) "+
				 "and m.start < d.createdOn "+
				 "and t.id = d.createdBy "+
				 "and d.title like ? "+
				 "order by d.title, t.name"
				 );
		retrieveDocument.setString(1, firstPartOfTitle+'%');
		ResultSet documentResult = retrieveDocument.executeQuery();
		String tempTitle = "";
		String tempName = "";
		while(documentResult.next()){
			String title = documentResult.getString(1);
			String name = documentResult.getString(2);
			String role = documentResult.getString(3);
			String namePerson = documentResult.getString(4);
			if(!title.equals(tempTitle)||!name.equals(tempName))
			{
				tempTitle = title;
				tempName = name;
				System.out.println("Title "+title+" Name "+name+" Role "+role+" PersonName "+namePerson);
			}
			else
			{
				System.out.println("Role "+role+" PersonName "+namePerson);
			}
		}
	}

	
	
    public static void main(String...args) throws Exception{
    	//SolutionI solutionI = new SolutionI();
    	try {
    	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    	    Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=AssignmentIV", "sa", "19900809");
    	    printDocumentInfoYi(connection, "Z");
    	   
    	} catch (Exception e) {
    	    System.err.println("Unable to connect to the database due to " + e);
    	}
    	
    }
}
