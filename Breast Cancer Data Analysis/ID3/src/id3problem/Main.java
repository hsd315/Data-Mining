/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package id3problem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgrapht.*;
import org.jgrapht.graph.*;

/**
 *
 * @author Eshan
 */
public class Main {

    public static Vector<Data> allData = new Vector<Data>();
    public static Vector<Data> trainingData = new Vector<Data>();
    public static Vector<Data> testingData = new Vector<Data>();
    public static UndirectedGraph<Vertex, DefaultEdge> graph;
    public static UndirectedGraph<Vertex, DefaultEdge> graph1;

    public static void readFile(){
        // Variables
        BufferedReader datain;
        String line;

        // Open File
        try {
            datain = new BufferedReader(new FileReader("cancer_data_after_replace_random.csv"));
        } catch (FileNotFoundException ex) {
            datain = null;
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Read The Whole File and Create Dataset
        try {
            for (int i = 0; (line=datain.readLine())!= null; i++) {
                //System.out.println(line);
                if(i!=0)
                     allData.add(new Data(i, line));
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static void prepareDataSet(){

        // Chose Training Set
        trainingData.clear();
        testingData.clear();
        for(int i=0; i<allData.size(); i++){
            Random generator = new Random();
            int index = generator.nextInt(100);
            if(index<80) trainingData.add(allData.get(i));
            else testingData.add(allData.get(i));
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
        for(int attrib=0; attrib<9; attrib++){
            if( (mask&(~root.attributeMask)) != 0){
                // work with i'th attribute
                //System.out.println("work with "+attrib+"'th attribute");
                for(int partition=2; partition<=10; partition++){
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


    public static double calculateAccuracy(int tp, int tn, int fn, int fp){
        double a;
        a = (double)(tp+tn)/(double)(tp+fp+tn+fn);
        return a;
    }

    


    public static void main(String[] args) {

        // Variables
        Vertex root1,root2;
        double accuracy=0;
        double accuracy1=0;
        int counter=0;

        // Read File

        readFile();
        // Chose Training Set
        while(counter<100)
        {
        prepareDataSet();

        /**************************************************
         *                                                *
         *   Generate Tree via Id3 for Information Gain   *
         *                                                *
         **************************************************
         */

        // Create Root Node, positive for positive malignant data
        int positiveCount=0, negativeCount=0;
        Data rootPositiveData[], rootNegativeData[];
        for(int i=0; i<trainingData.size(); i++){
            if(trainingData.get(i).result) positiveCount++;
            else negativeCount++;
        }

        rootPositiveData = new Data[positiveCount];
        rootNegativeData = new Data[negativeCount];

        int posIndex=0, negIndex=0;
        for(int i=0; i<trainingData.size(); i++){
            if(trainingData.get(i).result){
                rootPositiveData[posIndex] = trainingData.get(i);
                posIndex++;
            }
            else {
                rootNegativeData[negIndex] = trainingData.get(i);
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
        //System.out.println("\n\nPrinting all the edges.............\n\n");
        for(DefaultEdge e: graph.edgeSet()){
         //   System.out.println(e);
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
        int total = testingData.size();
        int tp=0,fp=0,fn=0,tn=0;
        //double accuracy, precision, recall, fMeasure, gMean;
        //System.out.println("Total data:"+total);
        for(int i=0; i<testingData.size();i++){
            boolean result = test(testingData.get(i), root1, graph);
            if(result){
                if(testingData.get(i).result) tp++;
                else fn++; 
            }
            else {
                if(testingData.get(i).result) fp++;
                else tn++;
            }
        }

        //System.out.println("tp: "+tp+" fp: "+fp+" fn: "+fn+" tn: "+tn);
        accuracy = accuracy+calculateAccuracy(tp, tn, fn, fp);
     
        System.out.print("counter: "+counter);
        System.out.format(" IG::> Accuracy: %5.4f", calculateAccuracy(tp, tn, fn, fp));
       
       System.out.println();

        
        //System.out.println();

        counter++;
    }
        
        accuracy = (double)accuracy/100.0;
        System.out.println("Information Gain:\n Accuracy: "+accuracy);

    }






}
