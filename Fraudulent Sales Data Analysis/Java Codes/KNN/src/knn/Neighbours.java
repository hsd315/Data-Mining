/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import java.util.Vector;

/**
 *
 * @author eshan
 */
public class Neighbours {
    Vector<Data> nearest;
    double[] dist_array;
    double min;
    double max;
    int max_index=0;
    int k;

    public Neighbours() {
        min=100000000.0;
        max=0.0;
        k=1;
    }
    public Neighbours(int k) {
        nearest=new Vector<Data>();
        for(int i=0;i<k;i++)
        {
            Data d=new Data();
            nearest.add(d);
        }
        dist_array=new double[k];
        min=100000000.0;
        max=0.0;
        this.k=k;
    }
    
    public int check_distance(double distance)
    {
        int flag=0;
        if(nearest.size()<k)
            return 1;
        else
        {
            if(distance<min)
            {
               flag=1; 
            }
            else if(distance>=min && distance<=max)
            {
                flag=1;
            }
            else
            {
                flag=0;
            }
            return flag;
            
        }
        //return flag;
    }
    public void add_data(Data d,double distance)
    {
        if(nearest.size()>=k)
        {
            nearest.remove(k-1);
            dist_array[k-1]=0.0;
            dist_array[k-1]=distance;
            nearest.add(d);
            max=calculate_max();
        }
        else
        {
            if(distance<min)
                min=distance;
            dist_array[max_index++]=distance;
            if(nearest.size()==0)
            {
                max=distance;
            }
            
            else
            {
                max=calculate_max();
                
                
            }
            nearest.add(d);
            
                
                
        }
        
    }
    
    public double calculate_max()
    {
        double max_v=0.0;
        for(int i=0;i<max_index;i++)
        {
            if(dist_array[i]>max_v)
            {
                max_v=dist_array[i];
            }
        }
        return max_v;
        
    }
    public int return_result()
    {
        int count_ok=0,count_fraud=0;
        for(int i=0;i<nearest.size();i++)
        {
            Data d=nearest.get(i);
            if(d.result_code==Constants.insp_ok)
                count_ok++;
            else
                count_fraud++;
            
        }
        if(count_ok>count_fraud)
            return 0;
        else
            return 1;
    }
}
