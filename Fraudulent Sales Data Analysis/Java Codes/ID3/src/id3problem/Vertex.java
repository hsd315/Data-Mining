/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package id3problem;

/**
 *
 * @author Eshan
 */
public class Vertex {
    public Data positive[];
    public Data negative[];
    public double entropy;
    public int attributeMask;
    public int attribute;
    public int nextAttribute=-1;
    public int nextPartition=-1;
    public int direction=-1;

    public Vertex(Data positiveData[], Data negativeData[], int flagMask, int attrib,int criteria){
        int posCount = positiveData.length, negCount = negativeData.length;
        double dataCount = posCount + negCount;
        this.positive = positiveData;
        this.negative = negativeData;
        
        double posFrac = posCount/dataCount;
        double negFrac = negCount/dataCount;
        if(criteria==0)
        {
            if(posCount==0) this.entropy = 0.0;
            else if(negCount==0) this.entropy = 0.0;
            else this.entropy = -1*posFrac*Math.log10(posFrac)/Math.log10(2.0) - negFrac*Math.log10(negFrac)/Math.log10(2.0);
        }

        if(criteria==1)
        {
             if(posCount==0) this.entropy = 0.0;
            else if(negCount==0) this.entropy = 0.0;
            else this.entropy = 1-posFrac*posFrac-negFrac*negFrac;


        }
        this.attributeMask = flagMask;
        this.attribute = attrib;
    }

    @Override
    public String toString(){
        return "Entropy:"+this.entropy+" mask:"+Integer.toBinaryString(this.attributeMask);//attribute:"+Attribute.map(this.attribute);
    }
}
