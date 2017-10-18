/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eshan
 */
public class KMeans {

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
            Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void prepareDataset(boolean vfold,int index,int v)
    {
        
      //  int v=10;
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
    
    
    
    public static Vector<Centroid> kMeans(int k,int distance_matrix, Vector<Data>datatype)
    {
        int i=0;
        Vector <Centroid> centroidList= new Vector<Centroid>();
        //Vector <Centroid> prevList= new Vector<Centroid>();
        Centroid centroid;//=new Centroid();
        Threshold threshold=new Threshold();
        threshold.set_init_centroids(k);
        for(i=0;i<k;i++)
        {
            
            centroid=new Centroid();
          //  if(i==0)
        //        centroid.Initialize(train_ok_Data.get(i).quant,train_ok_Data.get(i).val,train_ok_Data.get(i).unit_price);
         //   else
            //    centroid.Initialize(train_fraud_Data.get(i).quant,train_fraud_Data.get(i).val,train_fraud_Data.get(i).unit_price);
           centroid.randomlyInitialize();
            //centroid.randomlyInitialize();
            centroidList.add(centroid);
            
           // centroid.print_attributes();
        }
        i=0;
        //for(i=0;i<100;i++)
        while(!threshold.isDone(centroidList)&&i<100)
        {
            int ok_dominant=0,fraud_dominant=0;
            for(int l=0;l<k;l++)
            {
                centroidList.get(l).reset();
                
                
            }
            for(int j=0;j<datatype.size();j++)
            {
                //int delta[]=new int[100];
                Data data=datatype.get(j);
                //delta=trainingData.get(j).get_attribute();
                double minimum=100000000000000000000000000000000000.0;
                int min_index=-1;
                for(int l=0;l<k;l++)
                {
                    
                    centroid=centroidList.get(l);
                    double distance=0.0;
                    
                    if(distance_matrix==Constants.Euclidean_Distance)
                        distance=centroid.get_Euclidean_distance(data.quant,data.val,data.unit_price);
                    if(distance_matrix==Constants.Manhattan_Distance)
                        distance=centroid.get_Manhattan_distance(data.quant,data.val,data.unit_price);
                    
                 //   System.out.println("Minimum: "+minimum+", Distance: "+distance);
                    if(distance<minimum)
                    {
                        minimum=distance;                               
                        min_index=l;
                    }
                   // System.out.println("Data: "+ j+" , centroid: "+l + " , distance: "+ distance  );
                }
           //     System.out.println("Minimum Index: "+min_index);
                centroidList.get(min_index).addData(data);
              //  System.out.println("j"+j+"length: "+delta.length);
            }
           // System.out.println(centroidList.size());
            
            for(int l=0;l<k;l++)
            {
                
                //prevList.add(centroidList.get(l));
                if(centroidList.get(l).get_total_data_count()>0)
                {
                 //    System.out.println(" Step: "+ i +"Cluster: "+l+" , Count: "+centroidList.get(l).get_total_data_count()+", Ok: "+ centroidList.get(l).get_ok_count()+" ,Fraud: "+centroidList.get(l).get_fraud_count());
                     if(centroidList.get(l).get_ok_count()>centroidList.get(l).get_fraud_count())
                     {
                         ok_dominant++;
                         
                     }
                     else
                     {
                         fraud_dominant++;
                     }
                }
                else
                {
                    centroidList.remove(l);
                    Centroid ct= new Centroid();
                    ct.randomlyInitialize();
                    centroidList.add(ct);
                }
               // System.out.println("PPV : " +);
                centroidList.get(l).change_atrributeValues();
                
               // centroidList.get(l).print_attributes();  
            }
           // System.out.println("index: "+i+" Fraud: "+fraud_dominant+", OK "+ok_dominant);
            i++;
        }
        
        return centroidList;
        
        
    }
    
    public static void standardize(double mean_q,double sd_q,double mean_v,double sd_v,double mean_up, double sd_up)
    {
        for(int i=0;i<allData.size();i++)
        {
            Data d= allData.get(i);
            double quant=(d.quant-mean_q)/sd_q;
            double val=(d.val-mean_v)/sd_v;
            double unit=(d.unit_price-mean_up)/sd_up;
            allData.get(i).set(quant, unit, val);
        }
    }
    public static void normalize()
    {
        double min_q=10000;
        double max_q=0;double min_v=10000;double max_v=0;double min_up=10000; double max_up=0;
        for(int i=0;i<allData.size();i++)
        {
            Data d=allData.get(i);
            if(min_q>d.quant)
                min_q=d.quant;
            if(min_v>d.val)
                min_v=d.val;
            if(min_up>d.unit_price)
                min_up=d.unit_price;
            
            if(max_q<d.quant)
                max_q=d.quant;
            
            if(max_v<d.val)
                max_v=d.val;
            
            if(max_up<d.unit_price)
                max_up=d.unit_price;
        }
        //System.out.println("Quant min: "+min_q+", max: "+max_q+" ; Val min: "+min_v+", max: "+max_v+"; UP min: "+min_up+", max: "+max_up);
        for(int i=0;i<allData.size();i++)
        {
            Data d= allData.get(i);
            double quant=1+(d.quant-min_q)*(1000-1)/(max_q-min_q);
            double val=1+(d.val-min_v)*(1000-1)/(max_v-min_v);
            double unit=1+(d.quant-min_up)*(1000-1)/(max_up-min_up);
            //double val=(d.val-mean_v)/sd_v;
          //  double unit=(d.unit_price-mean_up)/sd_up;
            allData.get(i).set(quant, unit, val);
        }
    }
    
    public static double calculatePPV(Vector<Centroid> result_centroids,int k)
    {
        double ppv=0.0;
        int true_positive=0;
        int false_positive=0;
        for(int i=0;i<k;i++)
        {
            result_centroids.get(i).calculate_true_false_positive();
            true_positive+=result_centroids.get(i).get_true_positive();
            false_positive+=result_centroids.get(i).get_false_positive();
            
        }
        ppv=(double)true_positive/(double)(true_positive+false_positive);
        return ppv;
    }
    
     public static Result calculatePPV_vfold(Vector<Centroid> result_centroids,Vector<Data> datatype,int k,int type)
    {
        double ppv=0.0;
        int true_positive=0;
        int false_positive=0;
        int ok_count=0;
        int fraud_count=0;
        Result r=new Result();
        Centroid centroid=new Centroid();
        for(int i=0;i<result_centroids.size();i++)
        {
            
                result_centroids.get(i).calculate_true_false_positive();
           
        }
        for(int j=0;j<datatype.size();j++)
        {
            int delta[]=new int[100];
            Data data= datatype.get(j);
           // delta=data.get_attribute();
            double minimum=1000000000000000000000.0;
            int min_index=-1;
            for(int l=0;l<result_centroids.size();l++)
            {
                centroid=result_centroids.get(l);
                double distance=0.0;

               // if(distance_matrix==Euclidean_Distance)
                 distance=centroid.get_Euclidean_distance(data.quant,data.val,data.unit_price);
                //if(distance_matrix==Manhattan_Distance)
                    //distance=centroid.get_Manhattan_distance(delta);

                if(distance<minimum)
                {
                    minimum=distance;                               
                    min_index=l;
                }
   //             System.out.println("Data: "+ j+" , centroid: "+l + " , distance: "+ distance  );
            }
            //int p=0;
            if(type==0)
            {
                if(result_centroids.get(min_index).clustering_type==data.result_code)
                {
                    true_positive++;
                  //  p++;
                   // System.out.println("Data: "+data.result+ "P: "+p);
                }
                else
                {
                    false_positive++;
                }
            }
            else
            {
                if(result_centroids.get(min_index).clustering_type==Constants.insp_ok)
                {
                    ok_count++;
                    datatype.get(j).result_code=Constants.insp_ok;
                    datatype.get(j).result="ok";
                  //  p++;
                   // System.out.println("Data: "+data.result+ "P: "+p);
                }
                else
                {
                    fraud_count++;
                    datatype.get(j).result_code=Constants.insp_fraud;
                    datatype.get(j).result="fraud";
                }
                
            }
            
        }
       // true_positive+=result_centroids.get(i).get_true_positive();
       // false_positive+=result_centroids.get(i).get_false_positive();
            
       // System.out.println("True Positive" +true_positive +", False Positive: "+false_positive);
        ppv=(double)true_positive/(double)(true_positive+false_positive);
        r.true_positive=true_positive;
        r.false_positive=false_positive;
        r.ok_count=ok_count;
        r.fraud_count=fraud_count;
        r.ppv=ppv;
        return r;
    }
   
	    
 
	   
 
	    //generate whatever data you want
     
    public static void clear()
    {
        train_ok_Data.clear();
        test_v_fold_Data.clear();
        train_v_fold_Data.clear();
        train_fraud_Data.clear();
        
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
    
     public static Vector<Centroid> clear_result(Vector<Centroid> result)
     {
         for(int i=0;i<result.size();i++)
         {
             if(result.get(i).get_total_data_count()==0)
                 result.remove(i);
                 
         }
         return  result;
     }
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        readFile();
        normalize();
        divide_data();
        Vector<Centroid> final_centroids=new Vector<Centroid>();
        int [] k_list={2,10,50,100,200,500,1000,2000,5000};
        int k=200;
        int v=10;
        Result r;
     //   standardize(8773.817, 1027892, 14612.9, 65138.86, 20.58679, 118.3741);
      //  data_print();
//        normalize(100.0,473883883.0,1005.0,4642955,.00000247,26460.700);
        //normalize();
     //   data_print();
       // prepareDataset(false, 0);
        try{
      //   FileWriter writer = new FileWriter("k_means.tex");
         FileWriter writer2=new FileWriter("k_means_diff_k.tex");
       //  writer.append("\\begin{table}[h]\n\\centering\n\\begin{tabular}{c|c|c|c|c|c|}\n");
     //   writer.append("Train & Test & Total Data &True Positive &False Positive&PPV \\\\ \n \\hline\n\\hline");
        
        writer2.append("\\begin{table}[h]\n\\centering\n\\begin{tabular}{c|c|c|c|}\n");
        writer2.append("k & Weighted PPV for 10-fold & Total Ok & Total Fraud \\\\ \n \\hline\n\\hline\n");
        String name ="Unknown_Output k_means_with_k_";
        for(int p=0;p<k_list.length;p++)
        {
                k=k_list[p];
                double weighted_ppv=0.0;
            //   kMeans(k, Constants.Euclidean_Distance, trainData);
                for(int i=0;i<v;i++)
                {

                    clear();
                    prepareDataset(true, i,v);

                    final_centroids=kMeans(k,Constants.Euclidean_Distance,train_v_fold_Data);
                    final_centroids=clear_result(final_centroids);
                    r=calculatePPV_vfold(final_centroids,test_v_fold_Data,k,0);
                    weighted_ppv+=r.ppv;
                    System.out.printf("True Positive: %d, False Postive: %d, PPV: %.4f",r.true_positive,r.false_positive,r.ppv);
                    String round_ppv=String.format("%.4f", r.ppv);
             //       writer.append("$(D^*-{d_{"+(i+1)+"}^*})$ & $d_{"+(i+1)+"}^*$ & "+test_v_fold_Data.size()+" & "+r.true_positive+" & "+r.false_positive+" & "+round_ppv+" \\\\ \n");

                    final_centroids.clear();
                // String round_ppv=String.format("%.4f", ppv);
                    System.out.println();
                // writer.append("$C_{km=2}(D^*-{d_{"+(i+1)+"}^*})$ & $C_{km=2}(d_"+(i+1)+"^*)$ &"+round_ppv+" &");
                }
                System.out.println("Weighted PPV: "+String.format("%.4f",weighted_ppv/10.0));
             //   writer.append("\\end{tabular}\n\\end{table}\n\n");
             //   writer.append("Weighted_PPV= "+String.format("%.4f", weighted_ppv/10.0));
                writer2.append(k+" & "+String.format("%.4f", weighted_ppv/10.0)+" & ");

                clear();
                final_centroids.clear();
                prepareDataset(false, 0, v);

                final_centroids=kMeans(k,Constants.Euclidean_Distance,trainData);
                final_centroids=clear_result(final_centroids);
                r=calculatePPV_vfold(final_centroids, testData, k, 1);

                System.out.println("Total Ok: "+r.ok_count +", Total Fraud: "+ r.fraud_count);
               // writer.append("\nOn Test Set: \\hline \n \n Total Ok Data: "+r.ok_count+", Total Fraud Data: "+r.fraud_count);
                writer2.append(r.ok_count+" & "+r.fraud_count+"\\\\ \n");
            //    writer.close();
                name+=String.valueOf(k)+".csv";
                generateCsvFile(name);
        }
        writer2.close();
        } catch (IOException ex) {
            Logger.getLogger(KMeans.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
