/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dmhw3_p2;

/**
 *
 * @author Eshan
 */
public class Data {
    public int attribute[] = new int[10];
    public boolean result;
    public int id;
    
    public Data(int id, String line){
        this.id = id;
        String tokens[] = line.split("[,]");
        for(int i=0; i<10; i++){
            attribute[i] = Integer.parseInt(tokens[i]);
        }
        int resultInt = Integer.parseInt(tokens[10]);
        if(resultInt==2) result = Boolean.FALSE;  // 2 benign
        else result = Boolean.TRUE;                 // 4 malignant
    }

    @Override
    public String toString(){
        String s = "";
        for(int i=0; i<10; i++){
            s += "i: "+i+" "+ Attribute.map(i)+": "+this.attribute[i]+" \n ";
        }
        s+="result: "+this.result;
        return s;
    }
    
    public int[] get_attribute()
    {
        return this.attribute;
    }
    public int get_specific_attribute(int index)
    {
        return this.attribute[index];
        
    }
    
}
