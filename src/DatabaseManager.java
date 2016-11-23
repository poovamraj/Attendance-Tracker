import java.sql.*;
public class DatabaseManager {

	Statement stmt;
	int result;
	ResultSet rs;
	String st_result;
	
	public DatabaseManager()
	{
	try
	{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost/server_database","root","root");
	stmt = con.createStatement( );
 
   }
	catch(SQLException e)
	{
		
	} catch (ClassNotFoundException e) {
		
		e.printStackTrace();
	}
  }
	
	public String executeQuery(String query,String column)
	{
		
		try
		{
		rs=stmt.executeQuery(query);
		rs.next();
		st_result=rs.getString(column);
		}
		catch(SQLException E)
		{
			
		}
		System.out.println(st_result);
		return st_result;
	}
	
	public int int_executeQuery(String query,String column)
	{
		
		try
		{
		rs=stmt.executeQuery(query );
		rs.next();
		result=rs.getInt(column);
		}
		catch(SQLException E)
		{
			
		}
		System.out.println(result);
		return result;
	}
	public void updateQuery(String SQL)
	{
		try
		{
		
		stmt.executeUpdate(SQL);
		}
		catch(SQLException E)
		{
			
		}
	}

	
}	
