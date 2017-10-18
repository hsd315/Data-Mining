/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package id3problem;

/**
 *
 * @author Eshan
 */
public class Data {
    public int atrribute[] = new int[9];
    public boolean result;
    public int id;
    
    public Data(int id, String line){
        this.id = id;
        String tokens[] = line.split("[,]");
        for(int i=0; i<9; i++){
            atrribute[i] = Integer.parseInt(tokens[i]);
        }
        int resultInt = Integer.parseInt(tokens[9]);
        if(resultInt==2) result = Boolean.FALSE;
        else result = Boolean.TRUE;
    }

    @Override
    public String toString(){
        String s = "";
        for(int i=0; i<9; i++){
            s += Attribute.map(i)+": "+this.atrribute[i]+", ";
        }
        s+="result: "+this.result;
        return s;
    }
}
