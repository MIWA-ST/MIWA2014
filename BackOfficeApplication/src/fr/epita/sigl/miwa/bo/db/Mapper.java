package fr.epita.sigl.miwa.bo.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper
{
	private String sqlRequest = "";
	
	public int add(String table, String columnsClause, String valuesClause)
	{
		try
		{
			this.sqlRequest = "INSERT INTO " + table + "(" + columnsClause + ") VALUES (" + valuesClause + ")";
			
			PreparedStatement ps = JdbcConnection.getInstance().getConnection().prepareStatement(this.sqlRequest);
			
            int i = ps.executeUpdate();

            if (i != 0)
            {
            	System.out.println("Request: " + this.sqlRequest);
            	System.out.println("Inserted in table " + table);
            	return i;
            } 
            else
            {
              System.out.println("not Inserted");
              return -1;
            }
         }
         catch (Exception e)
         {
             return -1;
         }
	}
	
	public void delete(String table, int id)
	{
		try
		{
			PreparedStatement ps = JdbcConnection.getInstance().getConnection().prepareStatement("DELETE FROM " + table + " WHERE ID=" + id);
 
            int i = ps.executeUpdate();
            if (i != 0)
            {
            	System.out.println("Request: " + this.sqlRequest);
                System.out.println("Deleted in table " + table);
            }
            else
            {
                System.out.println("not deleted");
            }
        }
		catch (Exception e)
        {
             e.printStackTrace();
        }		
	}

	public void update(String table, String setClause, String whereClause)
	{
		try
		{
			this.sqlRequest = "UPDATE " + table + " SET " + setClause + " WHERE " + whereClause;
			PreparedStatement ps = JdbcConnection.getInstance().getConnection().prepareStatement(this.sqlRequest);

            int i = ps.executeUpdate();
            if (i != 0)
            {
            	System.out.println("request: " + this.sqlRequest);
            	System.out.println("updated table " + table);
            }
            else
            {
                System.out.println("not updated");
            }
        }
		catch (Exception e)
		{
            
        }
	}

	public ResultSet get(String selectClause, String table, String whereClause)
	{		
		try
		{
			this.sqlRequest = "SELECT " + selectClause + " FROM " + table + " WHERE " + whereClause;
			PreparedStatement ps = JdbcConnection.getInstance().getConnection().prepareStatement(this.sqlRequest);				
			
            ResultSet res = ps.executeQuery();
       
            if (res.next())
            {
            	return res;
            }
            
            return null;
        }
		catch (SQLException e)
        {
            return null;
        }
	}
}
