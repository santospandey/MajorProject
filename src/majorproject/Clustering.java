package majorproject;

import database.Normalization;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Clustering {
    
//  the sample data is stored in the arraylist
    public static ArrayList< ArrayList< Double> > sampledata;      
    
//  for the centroid and the classification of data two array-list is created.        
    public final ArrayList< Data > dataSet ;
    private final ArrayList< Centroid > centers;
    
//  other parameters for clustering the NO_OF_CLUSTERS contains total no of cluster 
//  and NO_OF_FEATURES contains total no of features. 
    private int NO_OF_CLUSTERS; 
    public  int NO_OF_FEATURES;

    public Clustering() 
    {
        sampledata = new ArrayList<>();
        dataSet = new ArrayList<>();
        centers = new ArrayList<>();        
    }
    
    
/**
 * initializes the sample data to the array-list sample-data.
 */    
    public void initialize_data()
    {
        Normalization n = new Normalization();
        n.store_normalize_values();        
        sampledata = n.nor_health_info;
        this.NO_OF_FEATURES = sampledata.get(0).size();
    }
    
        
/**
 *  for the initialization of the centroid 
 *  @param no_of_cluster
 */           
    public void initialize_centroid(int no_of_cluster)
    {   
        this.NO_OF_CLUSTERS = no_of_cluster;
        ArrayList< Double > center = new ArrayList<>();
        ArrayList< Integer > check_store = new ArrayList<>();
        int a;
        int i = 0;                                        
        
        try
        {
            if( NO_OF_CLUSTERS >= 75)
                throw new Exception("no of clusters high");
                        
            while(i < NO_OF_CLUSTERS)
            {
                a = (int) (Math.random()*74);
                if(!check_store.contains(a))
                {
                    check_store.add(a);
                    center = sampledata.get(a);
                    centers.add(new Centroid(center));
                    i++;
                }            
            }            
        }        
         catch (Exception ex) {
            Logger.getLogger(Clustering.class.getName()).log(Level.SEVERE, null, ex);
        }                        
    }
        
    
/**
* 
* @param c object of class Centroid
* @param d object of class Data
* @return  distance between c and d
*/
    public double distance(Centroid c, Data d)
    {
        double distance = 0.0;
        
        for(int i = 1; i < d.features.size(); i++)
        {
            distance += Math.pow(c.center.get(i) - d.features.get(i), 2);
        }
        
            distance = Math.sqrt(distance);
            
        return distance;        
    }

    
/**
 * 
 * @param c data object
 * @param d data object
 * @return 
 */    
    public double datadistance(Data c, Data d)
    {
        double distance = 0.0;
        
        for(int i = 1; i < d.features.size(); i++)
        {
            distance += Math.pow(c.features.get(i) - d.features.get(i), 2);
        }
        
            distance = Math.sqrt(distance);
            
        return distance;        
    }
    
    
/**
 *  performs clustering 
 */
    public void perform_clustering()
    {
        Data newdata = null;
        int sampleno = 0;
        final double bigNumber = Math.pow(10, 10);    // some big number that's sure to be larger than our data range.
        double minimum;
        double distance = 0;
        int cluster = 0;
        boolean isStillMoving = true;
        
//  total for summation of the values of features.
        double[] total = new double[sampledata.get(0).size()];
        for(int j = 0; j < total.length; j++)
        {
           total[j] = 0;
        }                
        
        while(dataSet.size() < sampledata.size())
        {
            newdata = new Data(sampledata.get(sampleno));   
            dataSet.add(newdata);
            
            minimum = bigNumber;
            
            for(int i = 0; i < NO_OF_CLUSTERS; i++)
            {                
                distance = distance(centers.get(i), newdata);
                
                if(distance < minimum)
                {
                    cluster = i;
                    minimum = distance;                    
                }                                                
             }                
                newdata.set_cluster(cluster);  //  set the cluser of new data.                
             
                
                for(int i = 0; i < NO_OF_CLUSTERS; i++)
                {
                    int totalInCluster = 0;                
                    
                    for(int j = 0; j < dataSet.size(); j++)
                    {                        
                         if(dataSet.get(j).get_cluster() == i)
                            {
                                for(int k = 0; k < dataSet.get(j).features.size(); k++)
                                {
                                    total[k] += dataSet.get(j).get_data(k);
                                }                            
                                totalInCluster++;
                            }
                    }
                      
                    if(totalInCluster > 0)
                    {                        
                        for(int j = 0; j < NO_OF_FEATURES; j++)
                        {
                            centers.get(i).center.set(j, total[j]/totalInCluster);                              
                        } 
                    }                    
                }   

            sampleno++;        
        }
    
        
// keep shifting the centroid until equilibrium occurs
        while(isStillMoving)
        {           
            // calculate new centroids.
            for(int i = 0; i < NO_OF_CLUSTERS; i++)
            {
                int totalInCluster = 0;
                
// initialize zero to the elements of total[]. 
                for(int j = 0; j < NO_OF_FEATURES; j++)
                {
                   total[j] = 0;
                }            
                
                for(int j = 0; j < dataSet.size(); j++)
                {                        
                     if(dataSet.get(j).get_cluster() == i)
                        {
                            for(int k = 0; k < dataSet.get(j).features.size(); k++)
                            {
                                total[k] += dataSet.get(j).get_data(k);
                            }                            
                            totalInCluster++;
                        }
                }

                if(totalInCluster > 0)
                {
                    for(int j = 0; j < NO_OF_FEATURES; j++)
                        {
                            centers.get(i).center.set(j, total[j]/totalInCluster);                                               
                        }               
                }                    
            }
                     
// Assign all data to the new centroids
            isStillMoving = false;
            
            for(int i = 0; i < dataSet.size(); i++)
            {
                Data tempData = dataSet.get(i);
                minimum = bigNumber;
                for(int j = 0; j < NO_OF_CLUSTERS; j++)
                {
                    distance = distance(centers.get(j), tempData);
                    
                    if(distance < minimum)
                    {
                        minimum = distance;
                        cluster = j;
                    }
                }
                
                tempData.set_cluster(cluster);
                if(tempData.get_cluster() != cluster){
                    tempData.set_cluster(cluster);
                    isStillMoving = true;
                }
            }        
        }        
    }    
    

    public void silout()
    {        
        ArrayList < Data > clusters = new ArrayList<>();
        
        ArrayList < ArrayList<Data> > clusters_store = new ArrayList<>();
        
        for(int i = 0; i < NO_OF_CLUSTERS; i++)
        {
            for(int j = 0; j < dataSet.size(); j++)
            {
                if(dataSet.get(j).get_cluster() == i)
                {
                    clusters.add(dataSet.get(j));
                }
            }            
            clusters_store.add(clusters);
            clusters = new ArrayList<>();
        }
        
        double distance;
        double min_distance = 100000000;
        int cluster;
        ArrayList< Data > newcluster = new ArrayList<>();        
        Data d = new Data();
//        System.out.println("\n average inner cluster:\n");
        for(int i = 0; i < dataSet.size(); i++)
        {
            distance = 0.0;
            cluster = dataSet.get(i).get_cluster();   
            d = dataSet.get(i);
            newcluster = clusters_store.get(cluster);
            for(int j = 0; j < newcluster.size(); j++)
            {
                distance += datadistance(d, newcluster.get(j));
            }
            
//      this is the average distance between the data points.
            if(newcluster.size() > 1)
            {
                distance = distance/(newcluster.size() - 1);
            }
            else
                distance = 0;
            
            dataSet.get(i).avg_inner_cluster_distance = distance;
//            System.out.println(dataSet.get(i).avg_inner_cluster_distance);
            
        }
        
        Centroid newcentroid = new Centroid();
        ArrayList< Double > dist_arr = new ArrayList<>();
        
//        System.out.println("\n average b0\n");
        for(int i = 0; i < dataSet.size(); i++)
        {
            distance = 0.0;
            cluster = dataSet.get(i).get_cluster();
            d = dataSet.get(i);
            
            for(int j = 0; j < NO_OF_CLUSTERS; j++)
            {
                distance = 0;
                if(cluster != j)
                {
                    for(int k = 0; k<clusters_store.get(j).size(); k++)
                    {
                        distance += datadistance(d, clusters_store.get(j).get(k));
                    } 
                    distance = distance/clusters_store.get(j).size();                
                    dist_arr.add(distance);
                }                
            }                        
            
            double min = Collections.min(dist_arr);
            dataSet.get(i).avg_outer_cluster_distance = min;
                                    
            dist_arr = new ArrayList<>();
        }        
  }

    
public void calculate_siloutte()
{
    double total_siloutte  = 0 ;
    
//    System.out.println("\n fourth");
//    System.out.println(dataSet.get(4).features);
//    System.out.println("\n \n silouttee coefficient:\n");
    for(int i = 0; i<dataSet.size(); i++)
    {
        if(dataSet.get(i).avg_inner_cluster_distance > dataSet.get(i).avg_outer_cluster_distance)
        {
            dataSet.get(i).siloutte_coeff = ((dataSet.get(i).avg_outer_cluster_distance/dataSet.get(i).avg_inner_cluster_distance) - 1);
        }
        else
        {
            dataSet.get(i).siloutte_coeff = ( 1 - (dataSet.get(i).avg_inner_cluster_distance)/dataSet.get(i).avg_outer_cluster_distance);
        }        
//        System.out.println(dataSet.get(i).siloutte_coeff);
        total_siloutte += dataSet.get(i).siloutte_coeff;
    }
/*    
    System.out.println("\n average silouttee:");
    System.out.println(total_siloutte/dataSet.size());
*/
}
                

    public void clusteringdata()             
    {
/*                       
        // Print out clustering results.
        for(int i = 0; i < c.NO_OF_CLUSTERS; i++)
        {
            System.out.println("Cluster " + i + " includes:");
        
            for(int j = 0; j < Clustering.sampledata.size(); j++)
            {
                if(c.dataSet.get(j).get_cluster() == i)
                {
                    System.out.println("     (" + c.dataSet.get(j).avg_inner_cluster_distance + ", " + c.dataSet.get(j).features.get(1) + ", " + c.dataSet.get(j).features.get(2) +")");
                }
            } // j
            System.out.println();
        } // i  
*/        
    }
}
