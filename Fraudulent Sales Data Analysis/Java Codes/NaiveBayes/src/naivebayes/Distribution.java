/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayes;

/**
 *
 * @author eshan
 */
public class Distribution {
    public String id;
    public int frequency;
    
    public Distribution(String line)
    {
        String tokens[] = line.split("[,]");
        id=tokens[0].replaceAll("\"", "");
        frequency=Integer.parseInt(tokens[1]);
        
    }
    public Distribution()
    {
        id="";
        frequency=0;
    }
    public Distribution(String id, int f)
    {
        this.id=id;
        this.frequency=f;
    }
    public void incFrequency()
    {
        frequency++;
        
    }
    public void print()
    {
        System.out.println("id: "+ id+ "frequency: "+frequency);;
    }
    
    
}
