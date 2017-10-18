/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package id3problem;

import java.io.*;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgraph.JGraph;
import org.jgrapht.*;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.*;

/**
 *
 * @author Eshan
 */
public class ID3 {

    public static Vector<Data> allData = new Vector<Data>();
    public static Vector<Data> trainData = new Vector<Data>();
    public static Vector<Data> testData = new Vector<Data>();
    
   
    public static Vector<Data> train_v_fold_Data = new Vector<Data>();
    public static Vector<Data> test_v_fold_Data = new Vector<Data>();
    
    public static Vector<Data> train_ok_Data = new Vector<Data>();
    public static Vector<Data> train_fraud_Data = new Vector<Data>();
    
    public static UndirectedGraph<Vertex, DefaultEdge> graph;
    public static UndirectedGraph<Vertex, DefaultEdge> graph1;

    public static void readFile(){
        // Variables
        BufferedReader datain;
        String line;

        // Open File
        try {
            datain = new BufferedReader(new FileReader("cleaned_sales_data.csv"));
        } catch (FileNotFoundException ex) {
            datain = null;
            Logger.getLogger(ID3.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Read The Whole File and Create Dataset
        try {
            for (int i = 0; (line=datain.readLine())!= null; i++) {
                //System.out.println(line);
                if(i!=0)
                     allData.add(new Data(i, line));
            }
        } catch (IOException ex) {
            Logger.getLogger(ID3.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

     public static void prepareDataset(boolean vfold,int index)
    {
        
        int v=10;
        int set_length=(trainData.size()/v)+1;
        int start=index*set_length;
        if(index>8)
                set_length-=1;

       // System.out.println("Start "+ start);

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
             //   if(d.result_code==Constants.insp_ok)
                 //   test_ok_Data.add(d);
               // else
                   // test_fraud_Data.add(d);
            }
        }
        
    }
    public static double calculateEntropy(int positive, int negative,int criteria){
        double total = positive + negative;
        double entropy;
        if((positive+negative)==0) return 0.0;
        if(positive==0) return 0.0;
        if(negative==0) return 0.0;
        double posFrac = positive/total;
        double negFrac = negative/total;
        if(criteria==0)
        {
            entropy = -1*posFrac*Math.log10(posFrac)/Math.log10(2.0) - negFrac*Math.log10(negFrac)/Math.log10(2.0);
        }
        else
        {
            entropy=1-(posFrac*posFrac)-(negFrac*negFrac);
        }
        return entropy;
    }

    public static double calculateGain(double previousEntropy, int gr1, double gr1entropy, int gr2, double gr2entropy){
        int total = gr1+gr2;
        double frac1 = (double)gr1/(double)total;
        double frac2 = (double)gr2/(double)total;
        double gain;

            gain = previousEntropy - (frac1*gr1entropy) - (frac2*gr2entropy);
        
         return gain;
    }

    public static void id3(Vertex root,int criteria){
        //System.out.println("Current node:"+root);
        //System.out.print("total data: "+(root.positive.length+root.negative.length)+" positive: "+root.positive.length+" negative: "+root.negative.length);
        //System.out.println(" direction:" + root.direction);
        if(root.positive.length==0 || root.negative.length==0){
            return;
        }

        int mask = 1, bestAttribute=-1, bestPartition=-1, group1pos=0, group1neg=0, group2pos=0, group2neg=0;
        double bestGain=0, gain=0;
        Random r=new Random();
        for(int attrib=0; attrib<3; attrib++){
            if( (mask&(~root.attributeMask)) != 0){
                // work with i'th attribute
                //System.out.println("work with "+attrib+"'th attribute");
                for(int partition=0; partition<=10; partition++){
                    int gr1pos=0, gr1neg=0;
                    int gr2pos=0, gr2neg=0;
                    double gr1entropy, gr2entropy;
                    

                    // working with positive dataset
                    for(int data=0; data<root.positive.length; data++){
                        if(root.positive[data].atrribute[attrib]<partition){
                            gr1pos++;
                        }
                        else {
                            gr2pos++;
                        }
                    }

                    // working with negative dataset
                    for(int data=0; data<root.negative.length; data++){
                        if(root.negative[data].atrribute[attrib]<partition){
                            gr1neg++;
                        }
                        else {
                            gr2neg++;
                        }
                    }

                        gr1entropy = calculateEntropy(gr1pos, gr1neg,criteria);

                        gr2entropy = calculateEntropy(gr2pos, gr2neg,criteria);
                        gain = calculateGain(root.entropy, gr1pos+gr1neg, gr1entropy, gr2pos+gr2neg, gr2entropy);
                    

                       // System.out.println("\tpartition:"+partition+" gr1entropy:"+gr1entropy+" gr2entropy:"+gr2entropy+" gain:"+gain);
                    if(gain>bestGain) {
                        bestGain = gain;
                        bestAttribute = attrib;
                        bestPartition = partition;
                        group1pos = gr1pos;
                        group1neg = gr1neg;
                        group2pos = gr2pos;
                        group2neg = gr2neg;
                    }
                }
            }
            mask=mask<<1;
        }
        if(bestAttribute==-1){
            //System.out.println("FAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAIL\n");
            return;
        }

        Data group1PosiiveData[] = new Data[group1pos];
        Data group1NegativeData[] = new Data[group1neg];
        Data group2PosiiveData[] = new Data[group2pos];
        Data group2NegativeData[] = new Data[group2neg];
        int group1mask = (root.attributeMask | (1<<bestAttribute));
        int group2mask = (root.attributeMask | (1<<bestAttribute));

        int group1index=0, group2index=0;

        // Add positives
        for(int data=0; data<root.positive.length; data++){
            if(root.positive[data].atrribute[bestAttribute]<bestPartition){
                group1PosiiveData[group1index] = root.positive[data];
                group1index++;
            }
            else {
                group2PosiiveData[group2index] = root.positive[data];
                group2index++;
            }
        }

        group1index=0; group2index=0;
        // Add negatives
        for(int data=0; data<root.negative.length; data++){
            if(root.negative[data].atrribute[bestAttribute]<bestPartition){
                group1NegativeData[group1index] = root.negative[data];
                group1index++;
            }
            else {
                group2NegativeData[group2index] = root.negative[data];
                group2index++;
            }
        }
        
        Vertex group1 = new Vertex(group1PosiiveData, group1NegativeData, group1mask, bestAttribute,0);
        Vertex group2 = new Vertex(group2PosiiveData, group2NegativeData, group2mask, bestAttribute,0);
        root.nextAttribute = bestAttribute;
        root.nextPartition = bestPartition;
        group1.direction = 0;
        group2.direction = 1;
        if(criteria==0){
            graph.addVertex(group1);
            graph.addVertex(group2);
            graph.addEdge(root, group1);
            graph.addEdge(root, group2);
        }
        else {
            graph1.addVertex(group1);
            graph1.addVertex(group2);
            graph1.addEdge(root, group1);
            graph1.addEdge(root, group2);
        }
        
        //System.out.println("bestGain:"+bestGain+"\nbestAttrrubute:"+bestAttribute+"\nbestPartiotion:"+bestPartition);
        id3(group1,criteria);
        id3(group2,criteria);
        
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
            double quant=1+(d.quant-min_q)*(10-1)/(max_q-min_q);
            double val=1+(d.val-min_v)*(10-1)/(max_v-min_v);
            double unit=1+(d.quant-min_up)*(10-1)/(max_up-min_up);
            //double val=(d.val-mean_v)/sd_v;
          //  double unit=(d.unit_price-mean_up)/sd_up;
            allData.get(i).set(quant, unit, val);
        }
    }
    

    public static boolean test(Data test, Vertex root, UndirectedGraph<Vertex, DefaultEdge> testgraph) {
        //System.out.println("\n"+test);
        Vertex currentVertex = root;
        while(currentVertex.nextAttribute != -1) {
            //System.out.println("Current Vertex:"+currentVertex);
            // Find it's children
            Vertex left=null, right=null;
            for(Vertex v: testgraph.vertexSet()) {
                if(testgraph.getEdge(currentVertex, v)!=null){
                    if(v.direction==0) left = v;
                    else if(v.direction==1) right = v;
                }
            }
            if(test.atrribute[currentVertex.nextAttribute] < currentVertex.nextPartition){
                currentVertex = left;
            }
            else {
                currentVertex = right;
            }
        }
        //System.out.println("Current Vertex:"+currentVertex);
        if(currentVertex.positive.length == 0){
            //System.out.println("Calculated result: false");
        }
        else {
            //System.out.println("Calculated result: true");
        }

        if(currentVertex.positive.length == 0) return false;
        else return true;
    }


    public static double calculatePPV(int tp, int tn){
        double a;
        a = (double)(tp)/(double)(tp+tn);
        return a;
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
    
    public static Result getresult(Vector<Data> train,Vector<Data> test,int type)
    {
        Result r=new Result();
        Vertex root1,root2;
        int positiveCount=0, negativeCount=0;
        int ok_count=0,fraud_count=0;
            Data rootPositiveData[], rootNegativeData[];
            for(int i=0; i<train.size(); i++){
                if(train.get(i).result_code==0) positiveCount++;
                else negativeCount++;
            }
        // System.out.println("Positive: "+positiveCount+", Negative: "+negativeCount);

            rootPositiveData = new Data[positiveCount];
            rootNegativeData = new Data[negativeCount];

            int posIndex=0, negIndex=0;
            for(int i=0; i<train.size(); i++){
                if(train.get(i).result_code==0){
                    rootPositiveData[posIndex] = train.get(i);
                    posIndex++;
                }
                else {
                    rootNegativeData[negIndex] = train.get(i);
                    negIndex++;
                }
            }
            root1 = new Vertex(rootPositiveData, rootNegativeData, 0, -1,0);
            root2 = new Vertex(rootPositiveData, rootNegativeData, 0, -1,1);

    //       System.out.println("vertex::root\ntotal:"+trainingData.size()+"\npositive:"+root.positive.length+"\nnegative:"+root.negative.length+"\nentropy:"+root.entropy);

            graph = new SimpleGraph<Vertex, DefaultEdge>(DefaultEdge.class);
            graph1 = new SimpleGraph<Vertex, DefaultEdge>(DefaultEdge.class);
            graph.addVertex(root1);
            graph1.addVertex(root2);
            //System.out.println(graph.toString());

            // Generate Tree
            id3(root1,0);
            id3(root2,1);
        // System.out.println("\n\nPrinting all the edges.............\n\n");
            for(DefaultEdge e: graph.edgeSet()){
            //  System.out.println(e);
            }
            //System.out.println("Tree Generation Complete");


            /*****************
            *               *
            *   Test data   *
            *               *
            *****************
            */

            //System.out.println("Testing data");
            //System.out.println("Total Data:"+ allData.size());
            //System.out.println("Training Size: "+ trainingData.size());
            //System.out.println("Testing Size: "+ testingData.size());

            /**********************
            *                    *
            *   Information Gain *
            *                    *
            **********************
            */
            int total = test.size();
            int tp=0,fp=0,fn=0,tn=0;
            //double accuracy, precision, recall, fMeasure, gMean;
            //System.out.println("Total data:"+total);
            for(int i=0; i<test.size();i++){
                boolean result = test(test.get(i), root1, graph);
                if(type==0)
                {
                    if(result){
                        if(test.get(i).result_code==0) tp++;
                        else tn++; 
                    }
                    else {
                        if(test.get(i).result_code==0) tp++;
                        else tn++;
                    }
                }
                else
                {
                    
                     if(result){
                         test.get(i).result="ok";
                     //if(d.result_code)
                     test.get(i).result_code=Constants.insp_ok;
                     ok_count++;
                    }
                    else {
                        test.get(i).result="fraud";
                     //if(d.result_code)
                     test.get(i).result_code=Constants.insp_fraud;
                     fraud_count++;
                    }
                    
                }
            }
           r.true_positive=tp;
           r.false_positive=tn;
           r.fraud_count=fraud_count;
           r.ok_count=ok_count;
           r.ppv=calculatePPV(tp, tn);
        return r;
    }

    public static void main(String[] args) throws IOException {

        // Variables
        
        double weighted_ppv=0;
        double accuracy1=0;
        int counter=0;
        Result r=new Result();

        // Read File

        readFile();
        normalize();
        divide_data();
        try{
        FileWriter writer = new FileWriter("id3.tex");
         
         writer.append("\\begin{table}[h]\n\\centering\n\\begin{tabular}{c|c|c|c|c|c|}\n");
        writer.append("Train & Test & Total Data &True Positive &False Positive&PPV \\\\ \n \\hline\n\\hline");
        // Chose Training Set
        while(counter<10)
        {
            // prepareDataSet(true,counter);
                train_ok_Data.clear();
                test_v_fold_Data.clear();
                train_v_fold_Data.clear();
                train_fraud_Data.clear();
                prepareDataset(true, counter);
            r=getresult(train_v_fold_Data, test_v_fold_Data, 0);
            weighted_ppv+=r.ppv;
           // System.out.print("counter: "+counter);
           // System.out.format(" PPV: %.4f", r.ppv);
            
            System.out.printf("True Positive: %d, False positive: %d, PPV: %.4f",r.true_positive,r.false_positive,r.ppv);
                String round_ppv=String.format("%.4f", r.ppv);
                writer.append("$(D^*-{d_{"+(counter+1)+"}^*})$ & $d_{"+(counter+1)+"}^*$ & "+test_v_fold_Data.size()+" & "+r.true_positive+" & "+r.false_positive+" & "+round_ppv+" \\\\ \n");

             System.out.println();

     

            counter++;
        }
    
        System.out.println("Weighted PPV: "+String.format("%.4f",weighted_ppv/10.0));
        writer.append("\\end{tabular}\n\\end{table}\n\n");
            writer.append("Weighted_PPV= "+String.format("%.4f", weighted_ppv/10.0));
         train_ok_Data.clear();
        test_v_fold_Data.clear();
        train_v_fold_Data.clear();
        train_fraud_Data.clear();
        
        r=getresult(trainData, testData, 1);
        System.out.println("Total Ok: "+r.ok_count +", Total Fraud: "+ r.fraud_count);
        writer.append("\nOn Test Set: \\hline \n \n Total Ok Data: "+r.ok_count+", Total Fraud Data: "+r.fraud_count);
        
        //accuracy = (double)accuracy/100.0;
       // System.out.println("Information Gain:\n Accuracy: "+accuracy);
        writer.close();
        generateCsvFile("id3.csv");

    } catch (IOException ex) {
            Logger.getLogger(ID3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }




}
