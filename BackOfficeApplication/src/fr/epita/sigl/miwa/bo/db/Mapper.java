package fr.epita.sigl.miwa.bo.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper
{
	private static String sqlRequest = "";
	
	public static int add(String table, String columnsClause, String valuesClause)
	{
		JdbcConnection.getInstance().OpenConnection();
		try
		{
			sqlRequest = "INSERT INTO " + table + "(" + columnsClause + ") VALUES (" + valuesClause + ")";
			
			PreparedStatement ps = JdbcConnection.getInstance().getConnection().prepareStatement(sqlRequest);
			
            int i = ps.executeUpdate();

            if (i != 0)
            {
            	JdbcConnection.getInstance().closeConnection();
            	return i;
            } 
            else
            {
              System.out.println("not Inserted");
              JdbcConnection.getInstance().closeConnection();
              return -1;
            }
         }
         catch (Exception e)
         {
        	 System.out.println("REQUEST:" + sqlRequest);
        	 JdbcConnection.getInstance().closeConnection();
        	 e.printStackTrace();
             return -1;
         }
	}
	
	public static void delete(String table, String idName,int id)
	{
		JdbcConnection.getInstance().OpenConnection();
		try
		{
			PreparedStatement ps = JdbcConnection.getInstance().getConnection().prepareStatement("DELETE FROM " + table + " WHERE " + idName + "=" + id);
 
            int i = ps.executeUpdate();
            if (i != 0)
            {
                System.out.println("Deleted in table " + table);
            }
            else
            {
                System.out.println("not deleted");
            }
            JdbcConnection.getInstance().closeConnection();
        }
		catch (Exception e)
        {
			System.out.println("REQUEST:" + sqlRequest);
			JdbcConnection.getInstance().closeConnection();
             e.printStackTrace();
        }		
	}

	public static void update(String table, String setClause, String whereClause)
	{
		JdbcConnection.getInstance().OpenConnection();
		try
		{
			sqlRequest = "UPDATE " + table + " SET " + setClause + " WHERE " + whereClause;
			PreparedStatement ps = JdbcConnection.getInstance().getConnection().prepareStatement(sqlRequest);

            int i = ps.executeUpdate();
            if (i != 0)
            {
            	System.out.println("updated table " + table);
            }
            else
            {
                System.out.println("not updated");
            }
            JdbcConnection.getInstance().closeConnection();
        }
		catch (Exception e)
		{
			System.out.println("REQUEST:" + sqlRequest);
			JdbcConnection.getInstance().closeConnection();
            e.printStackTrace();
        }
	}

	public static ResultSet get(String selectClause, String table, String whereClause)
	{
		JdbcConnection.getInstance().OpenConnection();
		try
		{
			sqlRequest = "SELECT " + selectClause + " FROM " + table + " WHERE " + whereClause;
			PreparedStatement ps = JdbcConnection.getInstance().getConnection().prepareStatement(sqlRequest);				
			
            ResultSet res = ps.executeQuery();
       
            if (res.next())
            {
            	JdbcConnection.getInstance().closeConnection();
            	return res;
            }
            JdbcConnection.getInstance().closeConnection();
            return null;
        }
		catch (SQLException e)
        {
			System.out.println("REQUEST:" + sqlRequest);
			JdbcConnection.getInstance().closeConnection();
			e.printStackTrace();
            return null;
        }
	}
}
