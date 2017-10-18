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
    double[] atrribute= new double[3];
    public String sales_id;
    public String product_id;
    public double quant;
    public double val;
    public double unit_price;
    public String result;
    public int result_code;
    public int data_id;
    
    public Data(int id, String line){
        this.data_id = id;
        String tokens[] = line.split("[,]");
        sales_id=tokens[0].replaceAll("\"", "");
        product_id=tokens[1].replaceAll("\"", "");
        quant=Double.parseDouble(tokens[2]);
        val=Double.parseDouble(tokens[3]);
        result=tokens[4].replaceAll("\"", "");
        atrribute[0]=quant;
        atrribute[1]=val;
        
                
        unit_price= Double.parseDouble(tokens[5]);
        atrribute[2]=unit_price;
        if(result.compareTo("unkn")==0)
            result_code=Constants.insp_unkn;
        else
        {
            if(result.compareTo("ok")==0)
                result_code=Constants.insp_ok;
            if(result.compareTo("fraud")==0)
                result_code=Constants.insp_fraud;
        }
        
                
       // for(int i=0; i<13; i++){
        //    attribute[i] = Double.parseDouble(tokens[i]);
       // }
       // int resultInt = Integer.parseInt(tokens[12]);
                       // 4 malignant
    }
    
    public void set(double q, double up,double v)
    {
        quant=q;
        val=v;
        unit_price=up;
        
    }

   
    public void print()
    {
        System.out.println("Data: id:"+data_id+", Sales_ID: "+sales_id+", Product:"+product_id+",Quant "+quant+", Val: "+val+"Insp: "+result);
    }
    
    
}
