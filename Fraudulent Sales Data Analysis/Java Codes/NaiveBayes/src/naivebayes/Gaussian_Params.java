/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayes;

/**
 *
 * @author eshan
 */
public class Gaussian_Params {
    public double mean;
    public double standard_devation;
    public double vairance;
    
    public Gaussian_Params()
    {
        mean=0;
        vairance=0;
        standard_devation=0;
    }

    public Gaussian_Params(double m,double v, double sd) {
        mean=m;
        vairance=v;
        standard_devation=sd;
    }
    
    
    
    
}
