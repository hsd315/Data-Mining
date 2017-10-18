/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmhw3_p2;

import java.util.Vector;
import java.util.Random;
import java.math.*;
import java.lang.Math;
        /**
 *
 * @author eshan
 */
public class Centroid {
    private double [] attribute;// = new double[8];
    public int id;
    public int no_of_attributes;
    Vector <Data> data= new Vector<Data>();
    Random randomGenerator = new Random();
    int true_positive=0;
    int false_positive=0;
    boolean clustering_malignant=true;
    
    public Centroid()
    {
        no_of_attributes=8;
        attribute=new double[no_of_attributes];
        
    }       
    
    public Centroid(int total_attribute)
    {
        no_of_attributes=total_attribute-1;
        attribute=new double[no_of_attributes];
        
    }

    
    public void set_attributes(double[] att)
    {
        attribute=att;
        
    }
    public double [] get_attributes()
    {
        return attribute;
        
    }
    
    public int get_no_of_attributes()
    {
        return no_of_attributes;
    }
    
    public void randomlyInitialize()
    {
        for(int i=0;i<no_of_attributes;i++)
        {
            int random=1+randomGenerator.nextInt(10);
            attribute[i]=(double)random ;
        }
    }
    public void print_attributes()
    {
        for(int i=0;i<no_of_attributes;i++)
        {
            System.out.println("Centroid value "+i+" :"+attribute[i]);
        }
    }
    
    public double get_Euclidean_distance(int[] delta)
    {
        double total_distance=0;
        for(int i=0;i<no_of_attributes;i++)
        {
            double difference=(attribute[i]-(double)delta[i+1]);
            total_distance+=(difference*difference);
            
        }
        return Math.sqrt(total_distance);
    }
    
    public double get_Manhattan_distance(int[] delta)
    {
        double total_distance=0;
        for(int i=0;i<no_of_attributes;i++)
        {
            double difference=Math.abs(attribute[i]-(double)delta[i+1]);
            total_distance+=difference;
            
        }
        
        return total_distance;
    }
    
    public void addData(Data d)
    {
        data.add(d);
        
    }
    public void removeAllData()
    {
        
        data.clear();
    }
    public void reset()
    {
        
        data.clear();
        true_positive=0;
        false_positive=0;
        clustering_malignant=true;
    }
    
    public int get_total_data_count()
    {
        return data.size();
    }
    public void change_atrributeValues()
    {
        for(int i=0;i<no_of_attributes;i++)
        {
            int total=0;
            for(int j=0;j<data.size();j++)
            {
                Data d=data.get(j);
                total+=d.get_specific_attribute(i+1);
            }
            this.attribute[i]=(double)total/(double)data.size();
            
            
        }
        
    }
    
    public int get_benign_count()
    {
        int total_count=0;
        for(int i=0;i<data.size();i++)
        {
            if(data.get(i).result==false)
                total_count++;
                
        }
        return total_count;
    }
    public int get_malignant_count()
    {
        int total_count=0;
        for(int i=0;i<data.size();i++)
        {
            if(data.get(i).result==true)
                total_count++;
                
        }
        return total_count;
    }
    
    public void calculate_true_false_positive()
    {
        int benign_count=get_benign_count();
        int malignant_count=get_malignant_count();
        if(benign_count>malignant_count)
        {
            true_positive=benign_count;
            false_positive=malignant_count;
            clustering_malignant=false;
        }
        else
        {
            true_positive=malignant_count;
            false_positive=benign_count;
            clustering_malignant=true;
        }
    }
    public int get_true_positive()
    {
        return true_positive;
    }
    public int get_false_positive()
    {
        return false_positive;
    }
    
    public double get_specific_attribute(int index)
    {
        return attribute[index];
        
    }
    
    
    
}
