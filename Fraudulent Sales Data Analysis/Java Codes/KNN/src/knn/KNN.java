/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import java.io.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eshan
 */
public class KNN {

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
            Logger.getLogger(KNN.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(KNN.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
     public static void prepareDataset(boolean vfold,int index,int v)
    {
        
       // int v=10;
        int set_length=(trainData.size()/v)+1;
        int start=index*set_length;
        if(index>8)
                set_length-=1;

     //   System.out.println("Start "+ start);

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
   
    public static void data_print()
    {
        for(int i=0;i<trainData.size();i++)
        {
            System.out.print("index "+i);
            trainData.get(i).print();
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
             //   if(d.result_code==Constants.insp_ok)
                 //   test_ok_Data.add(d);
               // else
                   // test_fraud_Data.add(d);
            }
        }
        
    }
    
    public static double calculate_distance(Data d1, Data d2)
    {
        double distance=0.0;
        distance=((d1.quant-d2.quant)*(d1.quant-d2.quant))+((d1.val-d2.val)*(d1.val-d2.val))+((d1.unit_price-d2.unit_price)*(d1.unit_price-d2.unit_price));
       // total_distance=((unit_price-up)*(unit_price-up));
        return Math.sqrt(distance);
        
    }
    
    public static Result knn_algorithm(int k,Vector<Data> train, Vector<Data> test, int type)
    {
        int true_positive=0;
        int false_positive=0;
        int ok_count=0;
        int fraud_count=0;
        Result r=new Result();
        for(int i=0;i<test.size();i++)
        {
            Neighbours nearest_neighbours=new Neighbours(k);
            for(int j=0;j<train.size();j++)
            {
                double distance=calculate_distance(test.get(i), train.get(j));
                int check=nearest_neighbours.check_distance(distance);
                if(check==1)
                {
                    nearest_neighbours.add_data(train.get(j), distance);
                }
            }
            int get_result=nearest_neighbours.return_result();
            if(type==0)
            {
                if(get_result==test.get(i).result_code)
                {
                    true_positive++;
                }
                else
                {
                    false_positive++;
                }
            }
            else
            {
                
                if(get_result==Constants.insp_ok)
                {
                    ok_count++;
                }
                else
                {
                    fraud_count++;
                }
                
            }
        }
        double ppv=(double)(true_positive/(double)(true_positive+false_positive));
        r.true_positive=true_positive;
        r.false_positive=false_positive;
        r.ok_count=ok_count;
        r.fraud_count=fraud_count;
        r.ppv=ppv;
        return r;
      //  System.out.println("Total true positive: "+true_positive+", Total false positive: "+false_positive+ " , PPV :"+ppv);
    
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
    
    public static void clear()
    {
        train_ok_Data.clear();
        test_v_fold_Data.clear();
        train_v_fold_Data.clear();
        train_fraud_Data.clear();
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
        Result r=new Result();
        double weighted_ppv=0.0;
        int v=10;
        int k=1;
        readFile();
        divide_data();
        try{
            FileWriter writer = new FileWriter("k_nn.tex");
            writer.append("\\begin{table}[h]\n\\centering\n\\begin{tabular}{c|c|c|c|c|c|}\n");
            writer.append("Train & Test & Total Data &True Positive &False Positive&PPV \\\\ \n \\hline\n\\hline");
                for(int i=0;i<10;i++)
                {
                    clear();

                    prepareDataset(true, i,10);
                    r=knn_algorithm(1, train_v_fold_Data, test_v_fold_Data,0);
                    System.out.printf("True Positive: %d, False positive: %d, PPV: %.4f",r.true_positive,r.false_positive,r.ppv);
                    String round_ppv=String.format("%.4f", r.ppv);
                    writer.append("$(D^*-{d_{"+(i+1)+"}^*})$ & $d_{"+(i+1)+"}^*$ & "+test_v_fold_Data.size()+" & "+r.true_positive+" & "+r.false_positive+" & "+round_ppv+" \\\\ \n");
                    weighted_ppv+=r.ppv;
                    System.out.println();
                }
             System.out.printf("Weighted_PPV: %.4f \n ",weighted_ppv/(double)v);
            
            writer.append("\\end{tabular}\n\\end{table}\n\n");
            writer.append("Weighted_PPV= "+String.format("%.4f", weighted_ppv/10.0));
            clear();
          //  prepareDataset(false, 1,10);
            r=knn_algorithm(1, trainData, testData,0);
            System.out.println("Total Ok: "+r.ok_count +", Total Fraud: "+ r.fraud_count);
            writer.append("\nOn Test Set: \\hline \n \n Total Ok Data: "+r.ok_count+", Total Fraud Data: "+r.fraud_count);
            
            writer.close();
            generateCsvFile("knn");
            }catch (IOException ex) {
            Logger.getLogger(KNN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
