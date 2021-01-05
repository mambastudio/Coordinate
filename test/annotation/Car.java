/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package annotation;

/**
 *
 * @author user
 */
public class Car {
    @JsonField(arraysize = 13)    
    public final int[] make;
    
    public Car()
    {
        this.make = new int[13];
    }
    
}
