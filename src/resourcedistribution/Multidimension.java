package resourcedistribution;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author santosh
 */
public class Multidimension {
    
//  contains data element and mean of each group.    
    private int data[][];
    private ArrayList< Float > mean;
    
//  total no of groups.
    private final int total = 6;
    
//  total elements in each groups.
    private final int no_element = 4;
    
//  dimension of data.    
    private final int dimension = 3;
    
//  groups_mean contains the mean of each groups.
    private ArrayList< ArrayList< Float > > group_mean;
    
//  the groups of the elements.
    private ArrayList< ArrayList< ArrayList< Integer > > > group;

//  initializes the constructor.    
    public Multidimension(){
        this.group = new ArrayList<>();
        this.group_mean = new ArrayList<>();        
        
        this.data = new int[][]{{8, 7, 5}, {16, 5, 12}, {4, 3, 8}, {78, 32, 45}, {11, 23, 19}, {45, 56, 37}, {23, 34, 34},
            {19, 12, 7}, {8, 7, 13}, {16, 5, 34}, {4, 3, 9}, {78, 32, 76}, {11, 23, 23}, {45, 56, 67}, {23, 34, 53}, {19, 12, 23},
            {12, 4, 4}, {5, 9, 12}, {7, 0, 4}, {3, 9, 12}, {41, 21, 7}, {15, 29, 14}, {17, 0, 8}, {43, 9, 10} };
        
        ArrayList< Integer > tuple = new ArrayList();
        
        ArrayList< ArrayList< Integer > > list = new ArrayList();
        
        int i = 0;
        int j = 0;        
        
        while(i < total)
        {
            for( j = 0; j < no_element; j++)
            {   
                for(int k = 0; k < dimension; k++)
                {
                    tuple.add(data[i*no_element + j][k]);
                }
                    list.add(tuple);
                    tuple = new ArrayList();                                                                                    
            }            
            group.add(list);
            list = new ArrayList();            
            i++;        
        }
        
        System.out.println(group);                                
    }
    
    
/**
 * 
 * @param group
 * @return 
 */
    public ArrayList<Float> calculate_mean(ArrayList< ArrayList< Integer > > group)
    {
        ArrayList< Float > mean = new ArrayList<>();
        ArrayList< Integer > sum = new ArrayList<>();        
        
        int dimension = group.get(0).size();
        
        for(int i = 0; i < dimension; i++)
        {
            sum.add(i, 0);            
        }
        
        for(int i = 0; i < group.size(); i++)
        {            
            for(int j = 0; j < dimension; j++)
            {
                sum.set(j, (sum.get(j)+group.get(i).get(j)));
            }            
        }
        
        for(int i = 0; i<dimension; i++)
        {
            mean.add((float)sum.get(i)/group.size());
        }        
        
        return mean;
    }
    
    
    /**
     * 
     * @param data
     * @return 
     */
    public ArrayList<Float> Mean_2D( int[][] data)
    {
        float sum_x = 0;
        float sum_y = 0;
        ArrayList< Float > mean = new ArrayList<>();
        
        for(int i = 0; i < data.length; i++) 
        {
            sum_x += data[i][0];
            sum_y += data[i][1];
        }        
        mean.add(sum_x/data.length);
        mean.add(sum_y/data.length);
        return mean;                
    }
    
    
    /***
     * 
     * @param a
     * @param b
     * @return 
     */
    public float distance_multi(ArrayList< Float > a, ArrayList< Float > b)
    {
        float distance = 0;
        for(int i = 0; i < a.size(); i++)
        {
            distance += pow((a.get(i) - b.get(i)), 2);
        }
        
        return (float) sqrt((double) distance);
    }
    
    
    /***
     * 
     */
    public void equal_distribution()
    { 
        ArrayList< Float > disp_mean = new ArrayList<>();
        
        for(int i = 0; i < total-1; i++)
        {
            for(int j = i+1; j < total; j++)
            {
                multi_shuffle(i, j);
            }            
        }
        
        System.out.println("\n printing the values:\n");
        
        for(int i = 0; i < group.size(); i++)
        {
            System.out.println("group  " + i);
            for(int j = 0; j < group.get(i).size(); j++)
            {
                System.out.println(group.get(i).get(j));
            }
            
            disp_mean = calculate_mean(group.get(i));
            System.out.println("\n mean" + disp_mean);
            System.out.println("\n");
        }
    }         
    
    
    /***
     * 
     * @param i
     * @param j 
     */
   public void multi_shuffle(int i, int j)
   {
       this.mean = Mean_2D(data);
      
       ArrayList< Integer > temp = new ArrayList<>();
       
       for(int a = 0; a < group.get(i).size(); a++)
       {
           for(int b = 0; b < group.get(j).size(); b++)
           {                                                 
               group_mean = new ArrayList<>();
               mean = new ArrayList<>();
               mean = calculate_mean(group.get(i));
               group_mean.add(mean);
               
               mean = new ArrayList<>();
               mean = calculate_mean(group.get(j));
               group_mean.add(mean);
               
               float d1 = distance_multi(group_mean.get(0), this.mean);
               float d2 = distance_multi(group_mean.get(1), this.mean); 
                        
               // swapping two tuples.
               temp = group.get(j).get(b);
               group.get(j).set(b, group.get(i).get(a));
               group.get(i).set(a, temp);
               
               // calculation of new mean.
               mean = calculate_mean(group.get(i));
               group_mean.add(mean);
               
               mean = new ArrayList<>();
               mean = calculate_mean(group.get(j));
               group_mean.add(mean);
               
               // calculation of new distances.
               float d1_new = distance_multi(group_mean.get(2), mean);
               float d2_new = distance_multi(group_mean.get(3), mean);
               
               if((d1_new + d2_new) > (d1+d2))
               {
                // swapping two tuples.
                   temp = group.get(j).get(b);
                   group.get(j).set(b, group.get(i).get(a));
                   group.get(i).set(a, temp);                                                    
               }                                                            
           }              
       }
   } 
}
               
/*       
       ArrayList< Float > mean = new ArrayList<>();
       mean = calculate_mean(group.get(i));
       group_mean.add(mean);
       
       mean = new ArrayList<>();
       mean = calculate_mean(group.get(j));
       group_mean.add(mean);
*/       
//     System.out.println(group_mean);