/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phantom;

/**
 *
 * @author user
 */
public class LargeObject {
    private StringBuffer data;
    public LargeObject() {
       this.data = new StringBuffer();
       for (long i = 0; i < 50000000; i++) {
           this.data.append('x');
       }
   }
    
   @Override
   protected void finalize() {
       System.out.println("The finalize method has been called on the TestClass object");
   }
}
