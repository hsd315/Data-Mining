/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

/**
 *
 * @author eshan
 */
public class Result {
    public double ppv;
    public int true_positive;
    public int false_positive;
    public int total_data;
    public int ok_count;
    public int fraud_count;
    Result()
    {
        ppv=0;
        true_positive=0;
        false_positive=0;
        total_data=0;
        ok_count=0;
        fraud_count=0;
    }
    
}
