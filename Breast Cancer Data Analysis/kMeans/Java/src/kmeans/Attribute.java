/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dmhw3_p2;

/**
 *
 * @author Eshan
 */
public class Attribute {
    public static int id;
    public static String noattribute = "N/A";
    public static String attribute0 = "SCN";
    public static String attribute1 = "Clump Thickness";
    public static String attribute2 = "Uniformity of Cell Size";
    public static String attribute3 = "Uniformity of Cell Shape";
    public static String attribute4 = "Marginal Adhesion";
    public static String attribute5 = "Single Epithelial Cell Size";
    public static String attribute6 = "Bare Nuclei";
    public static String attribute7 = "Bland Chromatin";
    public static String attribute8 = "Normal Nucleoli";
    public static String attribute9 = "Mitoses";

    public static String map(int id) {
        if(id==-1) return noattribute;
        if(id==0) return attribute0;
        if(id==1) return attribute1;
        if(id==2) return attribute2;
        if(id==3) return attribute3;
        if(id==4) return attribute4;
        if(id==5) return attribute5;
        if(id==6) return attribute6;
        if(id==7) return attribute7;
        if(id==8) return attribute8;
        if(id==9) return attribute9;
        return null;
    }
}
