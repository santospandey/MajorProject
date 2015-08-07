/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package majorproject;

import java.util.ArrayList;

/**
 *
 * @author santosh
 */
public class Data {
   public ArrayList< Double > features;
   private int mCluster;
   public double avg_inner_cluster_distance;
   public double avg_outer_cluster_distance;
   public double siloutte_coeff;
      
   public Data()
   {
       features = new ArrayList<>();
       features = null;
   }
   
   public Data(ArrayList< Double > arr)
   {
       this.features = arr;
   }
   
   public void set_data(ArrayList< Double > data)
   {
       this.features.clear();
       this.features = data;
   }
   
   public double get_data(int index)
   {
       return features.get(index);
   }
   
   public void set_cluster(int cluster_no)
   {
       this.mCluster = cluster_no;
   }
   
   public int get_cluster()
   {
       return this.mCluster;
   }   
}
