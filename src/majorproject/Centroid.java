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

public class Centroid {
     public ArrayList< Double > center;
     
     Centroid()
     {
         center = new ArrayList<  >();
//         center = null;
     }
     
     Centroid(ArrayList< Double > c)
     {
         center = new ArrayList<  >();
         this.center = (ArrayList<Double>) c.clone();
     }
     
     public void set_data(ArrayList< Double > data)
     {
          this.center.clear();
          this.center = data;
     }
     
     public double get_data(int index)
     {
          return center.get(index);
     }
}
