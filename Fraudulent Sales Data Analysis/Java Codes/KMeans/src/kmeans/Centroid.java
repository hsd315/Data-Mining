/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.util.Vector;
import java.util.Random;
import java.math.*;
import java.lang.Math;
        /**
 *
 * @author eshan
 */
public class Centroid {
    //private double [] attribute;// = new double[8];
    double quant;
    double val;
    double unit_price;
    Random randomGenerator = new Random();
    public int id;
    int true_positive=0;
    int false_positive=0;
    int clustering_type=0;
    
    Vector <Data> data= new Vector<Data>();
   
    
    
    
    public Centroid()
    {
       quant=0;
       val=0;
       unit_price=0.0;
        
    }       
    
    

    
    public void set_attributes(double quant, double val, double unit_price)
    {
        this.quant=quant;
        this.val=val;
        this.unit_price=unit_price;
        
    }
    
    
    public void randomlyInitialize()
    {
        quant=(double)Math.abs(randomGenerator.nextInt(1000))+1;
        val=(double)Math.abs(randomGenerator.nextInt(1000))+1;
        unit_price=(double)Math.abs(randomGenerator.nextInt(1000))+1;
        
       // quant=Math.abs(randomGenerator.nextGaussian()*4982328.0+119460.7); // mean=119460.7, standard_deviation=4982328.0
      //  val=Math.abs(randomGenerator.nextGaussian()*176634.6+61161.52); // mean=61161.52, standard_deviation=176634.6
       // unit_price=Math.abs(randomGenerator.nextGaussian()*382.3894+52.42013); // mean=119460.7, standard_deviation=382.3894
        
    }
    
    public void Initialize(double q, double v, double up)
    {
        quant=q;
        val=v;
        unit_price=up;
    }
    
    
    public double get_Euclidean_distance(double q,double v, double up)
    {
        double total_distance=0;
        total_distance=((quant-q)*(quant-q))+((val-v)*(val-v))+((unit_price-up)*(unit_price-up));
       // total_distance=((unit_price-up)*(unit_price-up));
        return Math.sqrt(total_distance);
    }
    
    public double get_Manhattan_distance(double q,double v, double up)
    {
        double total_distance=0;
        total_distance=Math.abs((quant-q)*(quant-q))+Math.abs((val-v)*(val-v));//+Math.abs((unit_price-up)*(unit_price-up));

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
        
    }
    
    public int get_total_data_count()
    {
        return data.size();
    }
    public void change_atrributeValues()
    {
       
            double total_quant=0;
            double total_val=0;
            double total_unit_price=0;
            
            for(int j=0;j<data.size();j++)
            {
                Data d=data.get(j);
                total_quant+=d.quant;
                total_val+=d.val;
                total_unit_price+=d.unit_price;
            }
            if(data.size()>0)
            {
                 this.quant=(double)total_quant/(double)data.size();
                this.val=(double)total_val/(double)data.size();
            
                 this.unit_price=(double)total_unit_price/(double)data.size();
            }

    }
    
    public int get_ok_count()
    {
        int total_count=0;
        for(int i=0;i<data.size();i++)
        {
            if(data.get(i).result_code==Constants.insp_ok)
                total_count++;
                
        }
        return total_count;
    }
    public int get_fraud_count()
    {
        int total_count=0;
        for(int i=0;i<data.size();i++)
        {
            if(data.get(i).result_code==Constants.insp_fraud)
                total_count++;
                
        }
        return total_count;
    }
    
    public void calculate_true_false_positive()
    {
        int ok_count=get_ok_count();
        int fraud_count=get_fraud_count();
        if(ok_count>fraud_count)
        {
            true_positive=ok_count;
            false_positive=fraud_count;
            clustering_type=Constants.insp_ok;
        }
        else
        {
            true_positive=fraud_count;
            false_positive=ok_count;
            clustering_type=Constants.insp_fraud;
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
        if(index==0)
            return quant;
        else if(index==1)
            return val;
        else
            return unit_price;
    }
    public void set_attributes(double[] attrs)
    {
        for(int i=0;i<attrs.length;i++)
        {
            
        if(i==0)
            quant=attrs[i];
        else if(i==1)
            val=attrs[i];
        else
           unit_price=attrs[2];
        }
    }
    
    
    
    
    
}
