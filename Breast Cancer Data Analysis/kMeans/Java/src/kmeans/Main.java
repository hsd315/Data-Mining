/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kmeans;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eshan
 */
public class Main {
    public static Vector<Data> allData = new Vector<Data>();
    public static Vector<Data> trainingData = new Vector<Data>();
    public static Vector<Data> testData = new Vector<Data>();
    public static int Euclidean_Distance=0;
    public static int Manhattan_Distance=1;



    /**
     * @param args the command line arguments
     */
    public static void readFile(){
        // Variables
        BufferedReader datain;
        String line;

        // Open File
        try {
            datain = new BufferedReader(new FileReader("cancer_data_after_pruning.csv"));
        } catch (FileNotFoundException ex) {
            datain = null;
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void prepareDataset(boolean v_fold, int index)
    {
        trainingData.clear();
        testData.clear();
        if(v_fold==false)
        {
            for(int i=0;i<allData.size();i++)
            {
                Data d=allData.get(i);
                trainingData.add(d);
            }
        }
        else
        {
            int v=10;
            int set_length=allData.size()/v;
            int start=index*set_length;
           // System.out.println("Start "+ start);
            if(index%2==0)
                    set_length+=1;
            for(int i=0;i<allData.size();i++)
            {
                Data d=allData.get(i);
                
                if(i>=start && i<start+set_length)
                {
                    
                    testData.add(d);
                }
                else
                {
                    trainingData.add(d);
                }
                
            }
        }
    }
    
    
    public static Vector<Centroid> kMeans(int k, int no_of_attributes,int distance_matrix)
    {
        int i=0;
        Vector <Centroid> centroidList= new Vector<Centroid>();
        //Vector <Centroid> prevList= new Vector<Centroid>();
        Centroid centroid;//=new Centroid();
        Threshold threshold=new Threshold();
        threshold.set_init_centroids(k);
        for(i=0;i<k;i++)
        {
            centroid=new Centroid(no_of_attributes);
            centroid.randomlyInitialize();
            centroidList.add(centroid);
            
           // centroid.print_attributes();
        }
        i=0;
        //for(i=0;i<100;i++)
        while(!threshold.isDone(centroidList) && i<20)
        {
            for(int l=0;l<k;l++)
            {
                centroidList.get(l).reset();
                
                
            }
            for(int j=0;j<trainingData.size();j++)
            {
                int delta[]=new int[100];
                Data data=trainingData.get(j);
                delta=trainingData.get(j).get_attribute();
                double minimum=10000000.0;
                int min_index=-1;
                for(int l=0;l<k;l++)
                {
                    centroid=centroidList.get(l);
                    double distance=0.0;
                    
                    if(distance_matrix==Euclidean_Distance)
                        distance=centroid.get_Euclidean_distance(delta);
                    if(distance_matrix==Manhattan_Distance)
                        distance=centroid.get_Manhattan_distance(delta);
                    
                    if(distance<minimum)
                    {
                        minimum=distance;                               
                        min_index=l;
                    }
                   // System.out.println("Data: "+ j+" , centroid: "+l + " , distance: "+ distance  );
                }
               // System.out.println("Minimum Index: "+min_index);
                centroidList.get(min_index).addData(data);
              //  System.out.println("j"+j+"length: "+delta.length);
            }
            for(int l=0;l<k;l++)
            {
                //prevList.add(centroidList.get(l));
                
                System.out.println(" Step: "+ i +"Cluster: "+l+" , Count: "+centroidList.get(l).get_total_data_count()+", Benign: "+ centroidList.get(l).get_benign_count()+" ,Malignant: "+centroidList.get(l).get_malignant_count());
               // System.out.println("PPV : " +);
                centroidList.get(l).change_atrributeValues();
               // centroidList.get(l).print_attributes();
                
            }
            i++;
        }
        return centroidList;
        
        
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
    
    public static double calculatePPV_vfold(Vector<Centroid> result_centroids,int k)
    {
        double ppv=0.0;
        int true_positive=0;
        int false_positive=0;
        Centroid centroid=new Centroid();
        for(int i=0;i<k;i++)
        {
            result_centroids.get(i).calculate_true_false_positive();
        }
        for(int j=0;j<testData.size();j++)
        {
            int delta[]=new int[100];
            Data data= testData.get(j);
            delta=data.get_attribute();
            double minimum=10000000.0;
            int min_index=-1;
            for(int l=0;l<k;l++)
            {
                centroid=result_centroids.get(l);
                double distance=0.0;

               // if(distance_matrix==Euclidean_Distance)
                 distance=centroid.get_Euclidean_distance(delta);
                //if(distance_matrix==Manhattan_Distance)
                    //distance=centroid.get_Manhattan_distance(delta);

                if(distance<minimum)
                {
                    minimum=distance;                               
                    min_index=l;
                }
               // System.out.println("Data: "+ j+" , centroid: "+l + " , distance: "+ distance  );
            }
            //int p=0;
            if(result_centroids.get(min_index).clustering_malignant==data.result)
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
       // true_positive+=result_centroids.get(i).get_true_positive();
       // false_positive+=result_centroids.get(i).get_false_positive();
            
       // System.out.println("True Positive" +true_positive +", False Positive: "+false_positive);
        ppv=(double)true_positive/(double)(true_positive+false_positive);
        return ppv;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Vector<Centroid> final_centroids=new Vector<Centroid>();
        int k=2;
        int set_attributes=10;
        readFile(); // read Data
        
        try {
            FileWriter writer = new FileWriter("problem_5b.tex");
            writer.append("\\begin{table}[h]\n\\centering\n\\begin{tabular}{c|c|c}\n");
            writer.append("$C_{km=2}(\\Delta^*)$ & $PPV_{Euclidean}$ & $PPV_{Manhattan}$ \\\\ \n \\hline\n");
        
        prepareDataset(false,0);  // question 5
        for(set_attributes=10;set_attributes>0;set_attributes-=2)
        {
            if(set_attributes!=2)
            {
                System.out.print("A_1,....,A_"+(set_attributes-1));
                writer.append("$A_1,\\ldots,A_"+(set_attributes-1)+"$&");
            }
            else
            {
                System.out.print("A_1,A_"+(set_attributes));
                writer.append("$A_1,A_"+(set_attributes)+"$&");
            }
          //  System.out.println("Set Attributes"+set_attributes);
            final_centroids=kMeans(k,set_attributes,0);
            double ppv=calculatePPV(final_centroids,k);
            System.out.printf("PPV: %.4f",ppv);
            String round_PPV = String.format("%.4f", ppv);
            writer.append(round_PPV+"&");
            
            final_centroids=kMeans(k,set_attributes,1);
            ppv=calculatePPV(final_centroids,k);
            System.out.printf(", PPV: %.4f", ppv);
            round_PPV = String.format("%.4f", ppv);
            writer.append(round_PPV+"\\\\ \n");
            System.out.println();
            
        }
        writer.append("\\end{tabular}\n\\end{table}\n");
       
        
        
      //  System.out.println(allData.get(0).toString());
        System.out.println("\n\nV-fold Cross Validation\n\n");
        writer.append("\\begin{table}[h]\n\\centering\n\\begin{tabular}{c||c|c|c}\n");
        writer.append("Train & Test & $PPV Result_{Euclidean}$ &$PPV Result_{Manhattan}$ \\\\ \n \\hline\n\\hline");
        double weighted_ppv1=0.0;
        double weighted_ppv2=0.0;
        for(int i=0;i<10;i++)
        {
            prepareDataset(true, i);
            
            final_centroids=kMeans(k,10,0);
            double ppv=calculatePPV_vfold(final_centroids,k);
            System.out.printf("PPV: %.4f",ppv);
            String round_ppv=String.format("%.4f", ppv);
            writer.append("$C_{km=2}(D^*-{d_{"+(i+1)+"}^*})$ & $C_{km=2}(d_"+(i+1)+"^*)$ &"+round_ppv+" &");
            weighted_ppv1+=ppv;
            final_centroids.clear();
            
            final_centroids=kMeans(k,10,1);
            ppv=calculatePPV_vfold(final_centroids,k);
            System.out.printf(", PPV: %.4f",ppv);
            round_ppv = String.format("%.4f", ppv);
            writer.append(round_ppv+"\\\\ \n");
            weighted_ppv2+=ppv;
            
            
            /// System.out.printf(", PPV: %.4f", ppv);
         //   System.out.println();
           // System.out.println("index: "+i+" Training Data Size: "+ trainingData.size() +", Test Data Size: "+testData.size());
             
            System.out.println();
        }
        System.out.printf("Weighted_PPV: %.4f, %.4f \n ",weighted_ppv1/10.0,weighted_ppv2/10.0);
        writer.append("\\end{tabular}\n\\end{table}\n");
         writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

}
