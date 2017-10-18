/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eshan
 */
public class NaiveBayes {

    /**
     * @param args the command line arguments
     */
        public static Vector<Data> allData = new Vector<Data>();
        public static Vector<Data> trainData = new Vector<Data>();
        public static Vector<Data> testData = new Vector<Data>();
        public static Vector<Data> train_v_fold_Data = new Vector<Data>();
        public static Vector<Data> test_v_fold_Data = new Vector<Data>();
        
        public static Vector<Data> train_ok_Data = new Vector<Data>();
        public static Vector<Data> train_fraud_Data = new Vector<Data>();
        
        public static Vector<Distribution> id_ok_Dist=new Vector<Distribution>();
        public static Vector<Distribution> prod_ok_Dist=new Vector<Distribution>();
        public static Vector<Distribution> id_fraud_Dist=new Vector<Distribution>();
        public static Vector<Distribution> prod_fraud_Dist=new Vector<Distribution>();
        
        public static Gaussian_Params quant_ok=new Gaussian_Params();//,val_ok,unit_price_ok,quant_fraud,val_fraud,unit_price_fraud;
        public static Gaussian_Params val_ok=new Gaussian_Params();
        public static Gaussian_Params unit_price_ok=new Gaussian_Params();
        
        public static Gaussian_Params quant_fraud=new Gaussian_Params();
        public static Gaussian_Params val_fraud=new Gaussian_Params();
        public static Gaussian_Params unit_price_fraud=new Gaussian_Params();
        
        
        
        
       public static int total_ok=0;
        public static int total_fraud=0;
       
        //read the data
    public static void readFile()
    {
    // Variables
        BufferedReader datain;
        String line;

        // Open File
        try {
            datain = new BufferedReader(new FileReader("cleaned_sales_data.csv"));
        } catch (FileNotFoundException ex) {
            datain = null;
            Logger.getLogger(NaiveBayes.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Read The Whole File and Create Dataset
        try {
            for (int i = 0; (line=datain.readLine())!= null; i++) {
              //  System.out.println(line);
                //skip Header
                if(i!=0)
                    allData.add(new Data(i, line));
            }
        } catch (IOException ex) {
            Logger.getLogger(NaiveBayes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public static void readDistribution(Vector<Distribution> dist, String filename)
    {
    // Variables
        BufferedReader datain;
        String line;

        // Open File
        try {
            datain = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            datain = null;
            Logger.getLogger(NaiveBayes.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Read The Whole File and Create Dataset
        try {
            for (int i = 0; (line=datain.readLine())!= null; i++) {
              //  System.out.println(line);
                //skip Header
                if(i!=0)
                {
                    String tokens[] = line.split("[,]");
                    String id=tokens[0].replaceAll("\"", "");
                    int frequency=Integer.parseInt(tokens[1]);
                    if(frequency>0)
                        dist.add(new Distribution(id,frequency));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NaiveBayes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    //prepare for v fold
    public static void prepareDataset(boolean vfold,int index,int v)
    {
        
           // int v=10;
            int set_length=(trainData.size()/v)+1;
            int start=index*set_length;
            if(index>8)
                    set_length-=1;
            
        //    System.out.println("Start "+ start);
            
            for(int i=0;i<trainData.size();i++)
            {
                Data d=trainData.get(i);
                
                if(vfold==true)
                {

                    if(i>=start && i<start+set_length)
                    {

                        test_v_fold_Data.add(d);
                    }
                    else
                    {
                        train_v_fold_Data.add(d);
                         if(d.result_code==Constants.insp_ok)
                            train_ok_Data.add(d);
                        else
                            train_fraud_Data.add(d);
                    }
                }
                else
                {
                     if(d.result_code==Constants.insp_ok)
                            train_ok_Data.add(d);
                        else
                            train_fraud_Data.add(d);
                }
                
            }
            
        
    }
    
    public static void divide_data()
    {
        for(int i=0;i<allData.size();i++)
        {
            Data d=allData.get(i);
            if(d.result_code==Constants.insp_unkn)
            {
                testData.add(d);
            }
            else
            {
                trainData.add(d);
               
            }
        }
        
    }
    public static void data_print()
    {
        for(int i=0;i<trainData.size();i++)
        {
            System.out.print("index "+i);
            trainData.get(i).print();
        }
    }
    public static void distribution_print(Vector<Distribution> dist)
    {
        int total=0;
        for(int i=0;i<dist.size();i++)
        {
          //  dist.get(i).print();
            total+=dist.get(i).frequency;
        }
        System.out.println("Total: "+total);
    }
    
    public static int search(Vector<Distribution> dist, String key)
    {
        for(int i=0;i<dist.size();i++)
        {
            if(dist.get(i).id.compareTo(key)==0)
                return i;
        }
        return -1;
        
    }
    
    public static double calculate_variance(double mean,Vector<Data> data,int type)
    {
        double total_var=0;
        double value=0;
        for(int i=0;i<data.size();i++)
        {
            Data d=data.get(i);
            if(type==0)
                value=(double)d.quant;
            if(type==1)
                value=(double)d.val;
            if(type==2)
                value=(double)d.unit_price;
            
            total_var+=((value-mean)*(value-mean));
        }
        int denom=(data.size()-1);
        return total_var/(double)(denom);
    }
    
    public static void calculate_informations_ok(Vector<Data> datatype)
    {
        int index_id=-1;
        int index_prod=-1;
        double total_quant=0,total_val=0,total_unit_price=0;
        double mean,variance;
        
        for(int i=0;i<datatype.size();i++)
        {
            Data d=datatype.get(i);
            index_id=search(id_ok_Dist, d.sales_id);
            
            if(index_id==-1)
            {
                Distribution new_id_dist=new Distribution();
                new_id_dist.id=d.sales_id;
                new_id_dist.frequency=1;
                id_ok_Dist.add(new_id_dist);
                
            }
            else
            {
                Distribution id_dist=id_ok_Dist.get(index_id);
                id_dist.incFrequency();
            }
            
            index_prod=search(prod_ok_Dist,d.product_id);
            
            if(index_prod==-1)
            {
                Distribution new_prod_dist=new Distribution();
                new_prod_dist.id=d.product_id;
                new_prod_dist.frequency=1;
                prod_ok_Dist.add(new_prod_dist);
                
            }
            else
            {
                Distribution prod_dist=prod_ok_Dist.get(index_prod);
                prod_dist.incFrequency();
            }
            total_quant+=(double)d.quant;
            total_val+=(double)d.val;
            total_unit_price+=(double)d.unit_price;
        }
        mean=total_quant/datatype.size();
        quant_ok.mean=mean;
        variance=calculate_variance(mean, datatype, 0);
        quant_ok.vairance=variance;
        quant_ok.standard_devation=Math.sqrt(variance);
        
        mean=total_val/datatype.size();
        val_ok.mean=mean;
        variance=calculate_variance(mean, datatype, 1);
        val_ok.vairance=variance;
        val_ok.standard_devation=Math.sqrt(variance);
        
        mean=total_unit_price/datatype.size();
        unit_price_ok.mean=mean;
        variance=calculate_variance(mean, datatype, 2);
        unit_price_ok.vairance=variance;
        unit_price_ok.standard_devation=Math.sqrt(variance);
        
        
        
        
    }
    
    public static void calculate_informations_fraud(Vector<Data> datatype)
    {
        int index_id=-1;
        int index_prod=-1;
        double total_quant=0,total_val=0,total_unit_price=0;
        double mean,variance;
        
        for(int i=0;i<datatype.size();i++)
        {
            Data d=datatype.get(i);
            index_id=search(id_fraud_Dist, d.sales_id);
            
            if(index_id==-1)
            {
                Distribution new_id_dist=new Distribution();
                new_id_dist.id=d.sales_id;
                new_id_dist.frequency=1;
                id_fraud_Dist.add(new_id_dist);
                
            }
            else
            {
                Distribution id_dist=id_fraud_Dist.get(index_id);
                id_dist.incFrequency();
            }
            
            index_prod=search(prod_fraud_Dist,d.product_id);
            
            if(index_prod==-1)
            {
                Distribution new_prod_dist=new Distribution();
                new_prod_dist.id=d.product_id;
                new_prod_dist.frequency=1;
                prod_fraud_Dist.add(new_prod_dist);
                
            }
            else
            {
                Distribution prod_dist=prod_fraud_Dist.get(index_prod);
                prod_dist.incFrequency();
            }
            total_quant+=(double)d.quant;
            total_val+=(double)d.val;
            total_unit_price+=(double)d.unit_price;
            
        }
        
        mean=total_quant/datatype.size();
        quant_fraud.mean=mean;
        variance=calculate_variance(mean, datatype, 0);
        quant_fraud.vairance=variance;
        quant_fraud.standard_devation=Math.sqrt(variance);
        
        mean=total_val/train_fraud_Data.size();
        val_fraud.mean=mean;
        variance=calculate_variance(mean, datatype, 1);
        val_fraud.vairance=variance;
        val_fraud.standard_devation=Math.sqrt(variance);
        
        mean=total_unit_price/train_fraud_Data.size();
        unit_price_fraud.mean=mean;
        variance=calculate_variance(mean, datatype, 2);
        unit_price_fraud.vairance=variance;
        unit_price_fraud.standard_devation=Math.sqrt(variance);
    }
    
    public static double calculate_gaussian( double value,Gaussian_Params param)
    {
        double mean, std,variance;
        //if(type==0)
       // {
            mean=param.mean;
            std=param.standard_devation;
            variance=param.vairance;
            
       // }
       // else
        //{
        //    mean=fraud_mean;
         //   std=fraud_std;
          //  variance=fraud_variance;
        //}
       // System.out.println("Unit Price "+ value);
        double exp=(-1.0)*((value-mean)*(value-mean))/(2*variance);
      //  System.out.println("exp "+exp);
        double prob=Math.exp(exp)/ (Math.sqrt(2*Math.PI)*std);
        return prob;
    }
    
    public static double m_estimate(int m,double p,int n_c, int n)
    {
        return ((double)(n_c+m*p)/(double)(n+m));
        
    }
    public static Result naive_bayes_algorithm(Vector<Data> datatype,int type)
    {
        int index_id=-1;
        int index_prod=-1;
        double prob_id=0;
        double prob_prod=0;
        double prob_unit_price=0;
        double prob_quant=0;
        double prob_val=0;
        double conditional_prob=0;
        double prior_prob=0;
        double posterior_prob=0;
        double max=0;
        int max_type=0;
        int fraud_count=0;
        int ok_count=0;
        int total=0;
        int false_positive=0,true_positive=0;
        int m=10;
        double p=0;
        Result r=new Result();
        
        Vector<Distribution> temp_id,temp_prod;
        Gaussian_Params quant_param,val_param,unit_price_param;
        for(int i=0;i<datatype.size();i++)
        {
            Data d=datatype.get(i);
            max=0.0;
            for(int j=0;j<2;j++)
            {
                
                if(j==0)
                {
                    
                    temp_id=id_ok_Dist;
                    temp_prod=prod_ok_Dist;
                    prior_prob=(double)(total_ok)/(total_ok+total_fraud);
                    total=total_ok;
                    quant_param=quant_ok;
                    val_param=val_ok;
                    unit_price_param=unit_price_ok;
                    p=0.9;
                }
                else
                {
                    temp_id=id_fraud_Dist;
                    temp_prod=prod_fraud_Dist;
                    prior_prob=(double)(total_fraud)/(total_ok+total_fraud);
                    total=total_fraud;
                    quant_param=quant_fraud;
                    val_param=val_fraud;
                    unit_price_param=unit_price_fraud;
                    p=0.1;
                }
                    
                index_id=search(temp_id,d.sales_id);
                if(index_id!=-1)
                {
                    //prob_id=(double)temp_id.get(index_id).frequency/(double)total;
                    prob_id=m_estimate(m, p, temp_id.get(index_id).frequency, total);
                }
                else
                    prob_id=0;
             //   System.out.println("Probability ID: "+prob_id);

                index_prod=search(temp_prod,d.product_id);
                if(index_prod!=-1)
                {
                   // prob_prod=(double)temp_prod.get(index_prod).frequency/(double)total;
                    prob_prod=m_estimate(m, p, temp_prod.get(index_prod).frequency, total);
                }
                else
                    prob_prod=0;
               // System.out.println("Probability PROD: "+prob_prod);
                
                prob_unit_price=calculate_gaussian( d.unit_price,unit_price_param);
                prob_quant=calculate_gaussian(d.quant,quant_param);
                prob_val=calculate_gaussian( d.val,val_param);
             //   System.out.println("Probability Unit Price: "+prob_unit_price);
                 
                conditional_prob=prob_id*prob_prod*prob_quant*prob_val*prob_unit_price;
             //   System.out.println("Conditional Probability: "+prob_unit_price);
                
                posterior_prob=prior_prob*conditional_prob;
               // System.out.println("Posterior "+posterior_prob);
                
                if(posterior_prob>max)
                {
                    max=posterior_prob;
                    max_type=j;
                }
                
                
            }
           // System.out.println("Maximum:"+max+" Index: "+max_type);
            if(type==0)
            {
                 if(max_type==d.result_code)
                    true_positive++;
                 else
                    false_positive++;
            }
            
            else
            {
                if(max_type==Constants.insp_ok)
                 {
                     d.result="ok";
                     //if(d.result_code)
                     d.result_code=Constants.insp_ok;
                     ok_count++;

                 }
                 else
                 {
                     d.result="fraud";
                     d.result_code=Constants.insp_fraud;
                     fraud_count++;
                 }  
            }
        }
        double ppv=(double)(true_positive/(double)(true_positive+false_positive));
        
      //  System.out.println("Total true positive: "+true_positive+", Total false positive: "+false_positive+ " , PPV :"+ppv);
        r.true_positive=true_positive;
        r.false_positive=false_positive;
        r.ok_count=ok_count;
        r.fraud_count=fraud_count;
        r.ppv=ppv;
        return r;
    }
    private static void generateCsvFile(String sFileName)
   {
	try
	{
	    FileWriter writer = new FileWriter(sFileName);
            
            writer.append("ID");
            writer.append(',');
            
            writer.append("Prod");
            writer.append(',');
            
            writer.append("Quant");
            writer.append(',');
            
            writer.append("Val");
            writer.append(',');
            
            writer.append("Insp");       
	    writer.append('\n');
	    
	    for(int i=0;i<testData.size();i++)
            {
                writer.append(testData.get(i).sales_id);
                writer.append(',');
                writer.append(testData.get(i).product_id);
                writer.append(',');
                writer.append(String.valueOf(testData.get(i).quant));
                writer.append(',');
                writer.append(String.valueOf(testData.get(i).val));
                writer.append(',');
                writer.append(String.valueOf(testData.get(i).result));
                writer.append(',');

                writer.append(',');
                writer.append('\n');
            }
 
	    
 
	   
 
	    //generate whatever data you want
 
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
    }
    public static void clearAll()
    {
        
        id_ok_Dist.clear();
        id_fraud_Dist.clear();
        prod_ok_Dist.clear();
        prod_fraud_Dist.clear();
        train_ok_Data.clear();
        test_v_fold_Data.clear();
        train_v_fold_Data.clear();
        train_fraud_Data.clear();
    }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        int v=10;
        double ppv=0;
        double weighted_ppv=0;
        Result r;
        readFile();
        divide_data();
        try{
            FileWriter writer = new FileWriter("naive_bayes.tex");
            writer.append("\\begin{table}[h]\n\\centering\n\\begin{tabular}{c|c|c|c|c|c|}\n");
            writer.append("Train & Test & Total Data &True Positive &False Positive&PPV \\\\ \n \\hline\n\\hline");
        
        
     //   calculate_informations_ok();
     //   calculate_informations_fraud();
      //  data_print();
      //  readDistribution(id_ok_Dist, "ID_ok.csv");
     //   readDistribution(prod_ok_Dist, "prod_ok.csv");
        //readDistribution(id_fraud_Dist, "ID_fraud.csv");
        //readDistribution(prod_fraud_Dist, "prod_fraud.csv");
        //System.out.println(test_fraud_Data.size());
        //System.out.println(id_fraud_Dist.size());
        //System.out.println(prod_fraud_Dist.size());
        //System.out.println(id_ok_Dist.size());
        //System.out.println(prod_ok_Dist.size());
    //    distribution_print(id_ok_Dist);
      //  distribution_print(id_fraud_Dist);
       // distribution_print(prod_ok_Dist);
    //    distribution_print(prod_fraud_Dist);
            for(int i=0;i<v;i++)
            {
                clearAll();
                

                

                prepareDataset(true,i,v);
                
                total_fraud=train_fraud_Data.size();
                total_ok=train_ok_Data.size();
               // System.out.println("Index: "+i+", train size: "+train_v_fold_Data.size()+", test size: "+test_v_fold_Data.size());
                //System.out.println("Index: "+i+", Ok size: "+train_ok_Data.size()+", Fraid size: "+train_fraud_Data.size());
                calculate_informations_ok(train_v_fold_Data);
                calculate_informations_fraud(train_v_fold_Data);
                r=naive_bayes_algorithm(test_v_fold_Data,0);
                System.out.printf("True Positive: %d, False positive: %d, PPV: %.4f",r.true_positive,r.false_positive,r.ppv);
                String round_ppv=String.format("%.4f", r.ppv);
                writer.append("$(D^*-{d_{"+(i+1)+"}^*})$ & $d_{"+(i+1)+"}^*$ & "+test_v_fold_Data.size()+" & "+r.true_positive+" & "+r.false_positive+" & "+round_ppv+" \\\\ \n");
                weighted_ppv+=r.ppv;
                System.out.println();
              }
            
            System.out.printf("Weighted_PPV: %.4f \n ",weighted_ppv/(double)v);
            
            writer.append("\\end{tabular}\n\\end{table}\n\n");
            writer.append("Weighted_PPV= "+String.format("%.4f", weighted_ppv/10.0));
            clearAll();
            prepareDataset(false, 0, v);
            total_fraud=train_fraud_Data.size();
            total_ok=train_ok_Data.size();
            calculate_informations_ok(trainData);
            calculate_informations_fraud(trainData);
            
            r=naive_bayes_algorithm(testData, 1);
            System.out.println("Total Ok: "+r.ok_count +", Total Fraud: "+ r.fraud_count);
            writer.append("\nOn Test Set: \\hline \n \n Total Ok Data: "+r.ok_count+", Total Fraud Data: "+r.fraud_count);
            
            
            writer.close();
            generateCsvFile("Unknown_Output naive bayes_m_r_median.csv");
        } catch (IOException ex) {
            Logger.getLogger(NaiveBayes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
