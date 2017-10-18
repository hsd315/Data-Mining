/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package naivebayes;

/**
 *
 * @author Eshan
 */
public class Data {
    public String sales_id;
    public String product_id;
    public int quant;
    public int val;
    public double unit_price;
    public String result;
    public int result_code;
    public int data_id;
    
    public Data(int id, String line){
        this.data_id = id;
        String tokens[] = line.split("[,]");
        sales_id=tokens[0].replaceAll("\"", "");
        product_id=tokens[1].replaceAll("\"", "");
        quant=Integer.parseInt(tokens[2]);
        val=Integer.parseInt(tokens[3]);
        result=tokens[4].replaceAll("\"", "");
        unit_price= Double.parseDouble(tokens[5]);
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

   
    public void print()
    {
        System.out.println("Data: id:"+data_id+", Sales_ID: "+sales_id+", Product:"+product_id+", Insp: "+result);
    }
    
    
}
