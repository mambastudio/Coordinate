/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.generic.io.LineMappedReader;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Test5 {
    public static void main(String... args)
    {
        processFloats();
        
        
    }
    
    public static void processFloats()
    {
        LineMappedReader reader = new LineMappedReader("C:\\Users\\user\\Desktop","test.txt");
        System.out.println(Arrays.toString(reader.readLineFloatArray()));
       
    }
    
    public static void kula(char c)
    {
        
    }
    
    public static void find()
    {
        LineMappedReader reader = new LineMappedReader("C:\\Users\\user\\Documents\\3D Scenes\\hair", "hair.obj");
        int count = 0;
        reader.goToStartChar();
        while(reader.hasRemaining())
        {
            if(reader.currentLineContains("/"))
                count++;
            reader.goToNextDefinedLine();
        }
        System.out.println(count);
    }
    
    public static void testNewLine()
    {
        LineMappedReader reader = new LineMappedReader("C:\\Users\\user\\Desktop","test.txt");
        System.out.println(reader.currentLineContainsIsolated("22"));
        
        
    }
    
    public static void processOBJ()
    {
        LineMappedReader reader = new LineMappedReader("C:\\Users\\user\\Documents\\3D Scenes\\hair", "hair.obj");
                
        int v = 0;
        int vt = 0;
        int usemtl = 0;
        int f = 0;
        int g = 0;
        int o = 0;
        
        reader.goToStartChar();        
        while(reader.hasRemaining())
        {                        
            if(reader.isCurrent("v "))
            {                
                v++;                           
            }
            else if(reader.isCurrent("vt "))
            {
                vt++;               
            }
            else if(reader.isCurrent("usemtl "))
            {
                usemtl++;                
            }
            else if(reader.isCurrent("f "))
            {
                f++;                  
            }
            else if(reader.isCurrent("g "))
            {
                g++;                  
            }
            else if(reader.isCurrent("o "))
            {
                o++;                              
            }
            
            reader.goToNextDefinedLine();
        }
        
        System.out.println(v);
        System.out.println(vt);
        System.out.println(usemtl);
        System.out.println(f);
        System.out.println(o);
        System.out.println(g);
    }
    
    public static void trial1(int n)
    {
        for(int i = 1; i<= n - 2; i++)    
            System.out.println(0+ " " +i+ " " +(i+1));
    }
    
    public static void trial2(int n)
    {
        for(int i = 1; i<= n - 5; i+=2)    
            System.out.println(0+ " " +(i+1)+ " " +(i+3)+ " " +1+ " " +(i+2)+ " " +(i+4));
    }
    
    public static void trial3(int n)
    {
        for(int i = 1; i<= n - 8; i+=3)    
            System.out.println(0+ " " +(i+2)+ " " +(i+5)+ " " +1+ " " +(i+3)+ " " +(i+6)+ " " +2+ " " +(i+4)+ " " +(i+7));
    }
    /**
     * if string is "lk//567567//" then the following are true and false:
     *  - 56 is true but 567 is false since for latter, subsequent char sequence is repeating
     *  - // is true but / is false since for latter, subsequent char sequence is repeating
     * 
     *
     * @param string
     * @param seq
     * @return 
    */
    public static boolean containsIsolated(String string, String seq)
    {
        int index = string.indexOf(seq);
        if(index < 0) return false;
        
        int newIndex = index + seq.length();         
        int nextIndex = string.indexOf(seq, newIndex);
        
        return nextIndex != newIndex;
    }
    
    //negative values are treated as zero
    public static void method1(int value)
    {        
        int x = (value >> 31) - (-value >> 31);
        int m = ((x-0) >> 31);
        int r =  (m & 0) + ((~m) & x);
        System.out.println(r);
    }
    //negative values are treated as one
    public static void method2(int value)
    {
        int x = (value >> 31) - (-value >> 31);
        int m = (x >> 31);
        int a = (x ^ m) - m;
        System.out.println(a);
    }
}
