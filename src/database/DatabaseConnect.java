/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author santosh
 */
public class DatabaseConnect {
    
    public Connection connection; // manages connection
    public Statement statement; // query statement
    public ResultSet resultSet; // manages results
    final String DRIVER = "com.mysql.jdbc.Driver";
    final String DATABASE_URL = "jdbc:mysql://localhost/RESOURCES";
    
    // create its own instance.
    private static DatabaseConnect db = new DatabaseConnect();
    
   
    private DatabaseConnect(){}
     
    public static DatabaseConnect get_connection()
    {
        return db;
    }
    
    
/**
 * 
 * @param data 
 */
    public void insert_data(int[] data)
    {
        try 
        {
            Class.forName(DRIVER);
            // establish connection to database
            connection = DriverManager.getConnection( DATABASE_URL, "root", "" );
            PreparedStatement prep_stat;
            System.out.println("Connection Established");
        } 

        catch (ClassNotFoundException ex) 
        {
             ex.printStackTrace();
        }

        catch(SQLException s)
        {
            s.printStackTrace();
        }
                 
        try
        {            
            String query = "INSERT INTO Cluster(district_id, cashcrops_clusterno, cerealcrops_clusterno,"
                + "health_clusterno, school_clusterno, transport_clusterno)"
                + "VALUES(?, ?, ?, ?, ?, ?)";

//            statement = connection.createStatement();
            PreparedStatement p = connection.prepareStatement(query);
            p.setInt(1, data[0]);
            p.setInt(2, data[1]);
            p.setInt(3, data[2]);
            p.setInt(4, data[3]);
            p.setInt(5, data[4]);
            p.setInt(6, data[5]);
            p.executeUpdate();                                                
        }
        
        catch(SQLException e)
        {
            e.printStackTrace();            
        }
        
        finally{           
            
            try{
                statement.close();
                connection.close();                                
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    
        
    /**
     *
     * @param query is the query to the database which executes the query.
     * @return the first column of the result of the query 
     * @throws SQLException
     */        
    public ArrayList< ArrayList<Double> > query_data(String query) 
    {
        
        ArrayList< Double > list = new ArrayList<>();
        ArrayList< ArrayList<Double> > table = new ArrayList<>(); 
        
        try 
        {
            Class.forName(DRIVER);
            // establish connection to database
            connection = DriverManager.getConnection( DATABASE_URL, "root", "" );
            System.out.println("Connection Established");
        } 

        catch (ClassNotFoundException ex) 
        {
             ex.printStackTrace();
        }

        catch(SQLException s)
        {
            s.printStackTrace();
        }        
                
          try
          {
            statement = connection.createStatement();
         
            // query database
            resultSet = statement.executeQuery(query);

            // process query results
            ResultSetMetaData metaData = resultSet.getMetaData();

            //get the no of columns of the table
            int numberOfColumns = metaData.getColumnCount();                        
             
                    while ( resultSet.next() )
                    {
                        for(int i = 1; i <= numberOfColumns; i++)
                        {
                            list.add(Double.parseDouble(resultSet.getObject(i).toString()));
                        }
                        
                        table.add(list);
                        list = new ArrayList<>();
                                                                                                                                                           
                    }// end while                    
           }
                                              
     catch(SQLException sql)
     {
            sql.printStackTrace();
     }
           
     finally // ensure resultSet, statement and connection are closed
     {
        try
        {
            resultSet.close();
            statement.close();
            connection.close();
        } // end try
        
        catch ( Exception exception )
        {
            exception.printStackTrace();
        } // end catch        
      }
      
      return table;        
    }     
}    
    
    
    
    
    
          
     

   

