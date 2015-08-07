/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author santosh
 */
public class Normalization {
    
//  for storing the value in the table.
        public ArrayList<ArrayList<Double>> cash_crops; 
        public ArrayList<ArrayList<Double>> cereal_crops;
        public ArrayList<ArrayList<Double>> health_info;
        public ArrayList<ArrayList<Double>> district_info;        
        
        
//for storing normalized values in table.
        public ArrayList<ArrayList<Double>> nor_cash_crops; 
        public ArrayList<ArrayList<Double>> nor_cereal_crops;
        public ArrayList<ArrayList<Double>> nor_health_info;
        public ArrayList<ArrayList<Double>> nor_district_info;
        
        
// for the database connection.
        public DatabaseConnect database_connection;
                
        
   public Normalization()
    {
        
// connect to the database.
        database_connection = DatabaseConnect.get_connection();

// allocating memory for arraylist containing original data.        
        this.cash_crops = new ArrayList<>();
        this.cereal_crops = new ArrayList<>();
        this.health_info = new ArrayList<>();
        this.district_info = new ArrayList<>();   


// allocating memory for the arraylist containing normalized data.        
        this.nor_cash_crops = new ArrayList<>();
        this.nor_cereal_crops = new ArrayList<>();
        this.nor_health_info = new ArrayList<>();
        this.nor_district_info = new ArrayList<>();        
    }   
   
   
   /**
   the function store the result of the query to the corresponding variables.
   after that it do the normalization using function "do_normalize(LinkedList)" 
   store it in the same variables.
   **/   
    public void store_normalize_values()
    {        
        cash_crops = database_connection.query_data("SELECT district_id, oilseed_area, oilseed_prod, oilseed_yield,"
                + " potato_area, potato_prod, potato_yield,"
                + " sugarcane_area, sugarcane_prod, sugarcane_yield,"
                + " tobacco_area, tobacco_prod, tobacco_yield"
                + " FROM cash_crops" );

        cereal_crops = database_connection.query_data("SELECT district_id, paddy_area, paddy_prod, paddy_yield, "
                + "wheat_area, wheat_prod,  wheat_yield, maze_area, maze_prod, maze_yield, "
                + "millet_area, millet_prod, millet_yield, barley_area, barley_prod, barley_yield "
                + "FROM cerial_crops");

        health_info = database_connection.query_data("SELECT district_id, no_of_hospital, no_of_hospital_patient, "
                + "no_of_PHCC, no_of_PHCC_patient, no_of_healthpost, no_of_healthpost_patient, "
                + "no_of_subhealthpost, no_of_subhealthpost_patient, no_of_PHC_clinic, no_of_PHC_clinic_patient, "
                + "no_of_EPI_clinic, no_of_EPI_clinic_patient, no_of_Pvt_Health_Inst, no_of_Pvt_Health_Inst_patient "
                + "FROM Health_info");

        district_info = database_connection.query_data("SELECT district_id, district_area, district_population, "
                + "population_land_ratio, population_density FROM District_info"); 
                
        nor_cash_crops = transpose(cash_crops);
        nor_cereal_crops = transpose(cereal_crops);
        nor_district_info = transpose(district_info);
        nor_health_info = transpose(health_info);
        
        ArrayList< Integer > a = new ArrayList<>();
        a.add(0);

//      data after normalization is stored in the same table.
        nor_cash_crops = do_normalize(a, nor_cash_crops);
        nor_cereal_crops = do_normalize(a, nor_cereal_crops);
        nor_district_info = do_normalize(a, nor_district_info);
        nor_health_info = do_normalize(a, nor_health_info);
                
        nor_cash_crops = transpose(nor_cash_crops);
        nor_cereal_crops = transpose(nor_cereal_crops);
        nor_district_info = transpose(nor_district_info);
        nor_health_info = transpose(nor_health_info);
        
/*        
        for(int i = 0; i < nor_cereal_crops.size(); i++)
        {
            System.out.println(nor_cash_crops.get(i));            
        }
*/                
    }

    
/**
 * @param index  is the index no of features to whom we don't want to normalize.it appears as it is in output.
 * @param arraylist is the regular data of the database. It include single feature of table.
 * @return the normalized form of the feature.
 * The normalization is done [normalized_value = (value - min)/(max - min)].
 */   
    public static ArrayList< ArrayList<Double> > do_normalize( ArrayList<Integer> index, ArrayList< ArrayList<Double> > arraylist)
    {
        Double min;
        Double max;
        ArrayList<Double> temp = new ArrayList<>();
        
//      arraylist for storing normalized table.
        ArrayList< ArrayList<Double> > normalized_table= new ArrayList<>(arraylist);
        
        for(int i = 0; i < arraylist.size(); i++)
        {
            if(!index.contains(i))
            {
                min = arraylist.get(i).get(0);
                max = arraylist.get(i).get(0);

                for (Double arraylist1 : arraylist.get(i)) 
                {
                    if (arraylist1 < min) 
                    {
                        min = arraylist1;
                    }                

                    if (arraylist1 > max) 
                    {
                        max = arraylist1;
                    }
                }
       
                for(int j = 0; j < arraylist.get(i).size(); j++)
                {
                    temp.add((arraylist.get(i).get(j) - min)/(max - min));
                }
                    normalized_table.set(i, temp);
                    temp = new ArrayList<>();       
            }                      
        }        
        return normalized_table;                   
    }
       

    
/**
 *  
 * @param a is the array-list of array-list of double.It transforms a into map where rows and columns
 * are interchanged.
 * @return map containing district id and the features of district.
 */   
 public HashMap<Integer, ArrayList<Double>> create_map(ArrayList< ArrayList<Double> > a)
 {     
          HashMap<Integer, ArrayList<Double>> map = new HashMap<>();          
          ArrayList<Double> temp = new ArrayList<>();
          
          for(int i = 0; i < 75; i++)
          {
              for(int j = 0; j < cash_crops.size(); j++)
              {
                  temp.add(cash_crops.get(j).get(i));
              }
              
              map.put(i+1, temp);
              temp = new ArrayList<>();
          }
          
            return map;          
 }
 
 
/**
 * 
 * @param matrix is two dimension array containing original table.
 * @return transformation of the matrix.
 */ 
public ArrayList<ArrayList<Double>> transpose(ArrayList<ArrayList<Double>> matrix)
{
    ArrayList< ArrayList<Double> > new_matrix = new ArrayList<>();
    ArrayList< Double > temp = new ArrayList();
    
    for(int i = 0; i < matrix.get(0).size(); i++)
    {
        for(int j = 0; j < matrix.size(); j++)
        {
            temp.add(matrix.get(j).get(i));
        }
            new_matrix.add(temp);            
            temp = new ArrayList<>();
    }
    return new_matrix;    
}
}
