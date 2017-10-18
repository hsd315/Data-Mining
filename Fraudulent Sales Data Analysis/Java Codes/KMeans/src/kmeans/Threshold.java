/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.util.Vector;

/**
 *
 * @author eshan
 */
public class Threshold {
    public Vector<Centroid> lastCentroids=new Vector<Centroid>();
    public double thresholdMargin=10;
    
    public void set_init_centroids(int size)
    {
        for(int i=0;i<size;i++)
        {
            Centroid init=new Centroid();
            lastCentroids.add(init);
        }
        
    }
    public void set_last_Centroids(Vector<Centroid> newCentroids)
    {
        lastCentroids.clear();
        for(int i=0;i<newCentroids.size();i++)
        {
            
            //new_Ones=newCentroids.clone().get(i);
          //  int total_attributes=newCentroids.get(i).get_no_of_attributes();
            Centroid new_Ones=new Centroid();
            double[] attributeList= new double[3];
          //  double quant=0;
         //   double val=0;
          //  doubl
            
            for(int j=0;j<3;j++)
                attributeList[j]=newCentroids.get(i).get_specific_attribute(j);
            
            new_Ones.set_attributes(attributeList);
           // new_Ones=newCentroids.get(i);
            lastCentroids.add(new_Ones);
        }
    }
    public boolean isDone(Vector<Centroid> newCentroids)
    {
        boolean complete=true;
        for(int i=0;i<lastCentroids.size();i++)
        {
            Centroid last_Ones=lastCentroids.get(i);
            Centroid new_Ones=newCentroids.get(i);
            for(int j=0;j<3;j++)
            {
                if(Math.abs(last_Ones.get_specific_attribute(j)-new_Ones.get_specific_attribute(j))>thresholdMargin)
                {
                    set_last_Centroids(newCentroids);
                    complete=false;
                    return false;
                }
            }
            
        }
        set_last_Centroids(newCentroids);
        complete=true;
        return true;
        
        
    }
    
}
