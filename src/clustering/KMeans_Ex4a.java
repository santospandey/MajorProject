/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;

public class KMeans_Ex4a
{
    private static final int NUM_CLUSTERS = 2;    // Total clusters.
    private static final int TOTAL_DATA = 9;      // Total data points.
        
/*
    Variables defined by myself.
    */    
    public static ArrayList< Double > initial = new ArrayList<  >();                
    public static ArrayList< ArrayList< Double > > NewSample = new ArrayList< >(); // for storing the data like SAMPLES.
    
    private static ArrayList<Data> dataSetnext = new ArrayList<>();
    private static ArrayList<Centroid> centroidsnext = new ArrayList<>();
        
    
    public static double SAMPLES[][] = new double[][]{{1.0, 1.0}, 
                                                                {1.5, 2.0}, 
                                                                {3.0, 4.0}, 
                                                                {5.0, 7.0}, 
                                                                {3.5, 5.0}, 
                                                                {4.5, 5.0}, 
                                                                {3.5, 4.5},
                                                                {5.9, 3.2},
                                                                {2.3, 3.2}};
                                                                          
    private static double mean_x;
    private static double mean_y;
    
    private static ArrayList<Data> dataSet = new ArrayList<>();
    private static ArrayList<Centroid> centroids = new ArrayList<>();
        
    
/**
 * Initialize the centroids of the clusters  
 */
        
    public static void initialize()
    {                 
        System.out.println("Centroids initialized at:");
        centroids.add(new Centroid(4.5, 5.0)); // lowest set.
        centroids.add(new Centroid(5.0, 7.0)); // highest set.
        
               
        System.out.println("     (" + centroids.get(0).X() + ", " + centroids.get(0).Y() + ")");
        System.out.println("     (" + centroids.get(1).X() + ", " + centroids.get(1).Y() + ")");
        System.out.print("\n");
        
        double sum_x = 0;
        double sum_y = 0;
        for(int i = 0; i<SAMPLES.length; i++)
        {
            sum_x += SAMPLES[i][0];
            sum_y += SAMPLES[i][1];
        }
        mean_x = sum_x/SAMPLES.length;
        mean_y = sum_y/SAMPLES.length;
        System.out.println(mean_x);
        System.out.println(mean_y);
        return;
    }
    
    private static void kMeanCluster()
    {
        final double bigNumber = Math.pow(10, 10);    // some big number that's sure to be larger than our data range.
        double minimum = bigNumber;                   // The minimum value to beat. 
        double distance = 0.0;                        // The current minimum value.
        int sampleNumber = 0;
        int cluster = 0;
        boolean isStillMoving = true;
        Data newData = null;
        
                
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(dataSet.size() < TOTAL_DATA)
        {
            // creates a new Data of Data type and assign the first and second values of SampleNUmber.
            newData = new Data(SAMPLES[sampleNumber][0], SAMPLES[sampleNumber][1]);
            dataSet.add(newData);
            minimum = bigNumber;
            
            //loop for no of clusters
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                // calculate the distance between centorid of 'NUM_CLUSTERS' clusters and new data.
                distance = dist(newData, centroids.get(i));
                
                // assign the cluster to min distance index.
                if(distance < minimum){
                    minimum = distance;
                    cluster = i;
                }
            }
            // assign the cluster no to newData.
            newData.cluster(cluster);
            
            // calculate new centroids.
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                int totalX = 0;
                int totalY = 0;
                int totalInCluster = 0;
                
            // calculate totalX and totalY and average them to calculate centroids.
                for(int j = 0; j < dataSet.size(); j++)
                {
                    if(dataSet.get(j).cluster() == i)
                    {
                        totalX += dataSet.get(j).X();
                        totalY += dataSet.get(j).Y();
                        totalInCluster++;
                    }
                }
                
                if(totalInCluster > 0)
                {
                    centroids.get(i).X(totalX / totalInCluster);
                    centroids.get(i).Y(totalY / totalInCluster);
                }
            }
            sampleNumber++;
        }
        
        // Now, keep shifting centroids until equilibrium occurs.
        while(isStillMoving)
        {
            // calculate new centroids.
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                int totalX = 0;
                int totalY = 0;
                int totalInCluster = 0;
                for(int j = 0; j < dataSet.size(); j++)
                {
                    if(dataSet.get(j).cluster() == i){
                        totalX += dataSet.get(j).X();
                        totalY += dataSet.get(j).Y();
                        totalInCluster++;
                    }
                }
                if(totalInCluster > 0){
                    centroids.get(i).X(totalX / totalInCluster);
                    centroids.get(i).Y(totalY / totalInCluster);
                }
            }
            
            // Assign all data to the new centroids
            isStillMoving = false;
            
            for(int i = 0; i < dataSet.size(); i++)
            {
                Data tempData = dataSet.get(i);
                minimum = bigNumber;
                for(int j = 0; j < NUM_CLUSTERS; j++)
                {
                    distance = dist(tempData, centroids.get(j));
                    if(distance < minimum){
                        minimum = distance;
                        cluster = j;
                    }
                }
                tempData.cluster(cluster);
                if(tempData.cluster() != cluster){
                    tempData.cluster(cluster);
                    isStillMoving = true;
                }
            }
        }
        return;
    }
    
    
    /**
     * // Calculate Euclidean distance.
     * @param d - Data object.
     * @param c - Centroid object.
     * @return - double value.
     */    
    private static double dist(Data d, Centroid c)
    {
        return Math.sqrt(Math.pow((mean_y - (c.Y() + d.Y())*0.5), 2) + Math.pow((mean_x - (c.X() + d.X())*0.5), 2));
    }           
    
    private static class Data
    {
        private double mX = 0;
        private double mY = 0;
        private int mCluster = 0;
        
        public Data()
        {
            return;
        }
        
        public Data(double x, double y)
        {
            this.X(x);
            this.Y(y);
            return;
        }
        
        public void X(double x)
        {
            this.mX = x;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double y)
        {
            this.mY = y;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
        
        public void cluster(int clusterNumber)
        {
            this.mCluster = clusterNumber;
            return;
        }
        
        public int cluster()
        {
            return this.mCluster;
        }
    }
    
    private static class Centroid
    {
     //stores the center of the tuples.       
        private double mX = 0.0;
        private double mY = 0.0;
        
        public Centroid()
        {
            return;
        }
                
        public Centroid(double newX, double newY)
        {
            this.mX = newX;
            this.mY = newY;
            return;
        }
        
        public void X(double newX)
        {
            this.mX = newX;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double newY)
        {
            this.mY = newY;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
    }
    
    public static void main(String args[])
    {
        initialize();
        kMeanCluster();
        
        // Print out clustering results.
        for(int i = 0; i < NUM_CLUSTERS; i++)
        {
            System.out.println("Cluster " + i + " includes:");
            for(int j = 0; j < TOTAL_DATA; j++)
            {
                if(dataSet.get(j).cluster() == i)
                {
                    System.out.println("     (" + dataSet.get(j).X() + ", " + dataSet.get(j).Y() + ")");
                }
            } // j
            System.out.println();
        } // i
        
        // Print out centroid results.
        System.out.println("Centroids finalized at:");
        for(int i = 0; i < NUM_CLUSTERS; i++)
        {
            System.out.println("     (" + centroids.get(i).X() + ", " + centroids.get(i).Y() + ")");
        }
        
        System.out.print("\n");
        return;
    }
}