/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.generic.io.CharMappedReader;
import coordinate.generic.io.LineMappedReader;
import coordinate.generic.io.StringMappedReader;
import coordinate.generic.io.StringReader;
import coordinate.utility.Timer;

/**
 *
 * @author user
 */
public class TestStringMappedFile {
    public static void main(String... args)
    {
        testOBJLine();
    }
    
    public static void testOBJLine()
    {
        LineMappedReader reader = new LineMappedReader("C:\\Users\\user\\Documents\\3D Scenes\\box", 
                "box2.obj");
                
        String line;
        while(true)
        {

            line = reader.readLineString3();            
            if(line == null)
                break;
            if(line.contains("f "))
            {
                System.out.println(line);
                System.out.println(reader.my_atoi(line.substring(2)));
            }

        }
        
    }
    
    public static void testCharMapped()
    {
        CharMappedReader reader = new CharMappedReader("C:\\Users\\user\\Documents\\3D Scenes\\box", 
                "box2.obj");
        while(reader.hasNextToken())
        {
            String string = reader.getToken();
            System.out.println(string);
        }
    }
    
    public static void performance()
    {
        StringReader reader1 = new StringReader("C:\\Users\\user\\Documents\\3D Scenes\\003_96_ferrari_550_maranello_wwc\\OBJ", 
                "96_Ferrari_550_Maranello.obj");        
        
        Timer timer1 = Timer.timeThis(()->{
            while(reader1.hasNext())
            {
                String string = reader1.getNextToken();
            }});
        System.out.println(timer1);
        
        CharMappedReader reader2 = new CharMappedReader("C:\\Users\\user\\Documents\\3D Scenes\\003_96_ferrari_550_maranello_wwc\\OBJ", 
                "96_Ferrari_550_Maranello.obj");
        Timer timer2 = Timer.timeThis(()->{
            
            while(reader2.getToken() != null) {
            }
        });
        System.out.println(timer2);
        
        LineMappedReader reader3 = new LineMappedReader("C:\\Users\\user\\Documents\\3D Scenes\\003_96_ferrari_550_maranello_wwc\\OBJ", 
                "96_Ferrari_550_Maranello.obj");
        Timer timer3 = Timer.timeThis(()->{
            
            while(reader3.readLineString3() != null) {
            }
        });
        System.out.println(timer3);
    }
}
