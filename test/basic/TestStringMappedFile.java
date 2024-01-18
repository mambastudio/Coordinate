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
import coordinate.list.IntList;
import coordinate.utility.StringParser;
import coordinate.utility.StringUtility;
import static coordinate.utility.StringUtility.getFirstInt;
import static coordinate.utility.StringUtility.isEndOfLine;
import static coordinate.utility.StringUtility.my_atoi;
import static coordinate.utility.StringUtility.skipSpaceAndCarriageReturn;
import coordinate.utility.Timer;
import java.util.ArrayList;
import java.util.Arrays;
import static coordinate.utility.StringUtility.isEndOfLine;

/**
 *
 * @author user
 */
public class TestStringMappedFile {
    public static void main(String... args)
    {
        testOBJLine();
        //String str = "as-345d+";
        //System.out.println(getFirstInt(str, new int[1]));
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
            ArrayList<index_t> f = new ArrayList(8);
            StringParser parser = new StringParser(line);
            if(parser.contains("f "))
            {             
                while (!parser.isNewLine()) {
                    index_t vi = parseRawTriple(parser);
                    parser.skipContinousSpace();
                    f.add(vi);
                }                
            }
            if(f.size() > 0)
                System.out.println(f);
        }
        
    }
    
    private static index_t parseRawTriple(StringParser parser) {               
        index_t vi = new index_t();  // 0x80000000 = -2147483648 = invalid
        
        vi.vertex_index = parser.getFirstInt();
        parser.skipIfNotSpaceAndChar('/');
               
        //read first individual /, and if not present then it is vertex index only, and return
        if (parser.isNotChar('/')) return vi;
        parser.incrementPointer();
                
        // i//k
        if (parser.isChar('/')) {            
            vi.normal_index = parser.getFirstInt();
            parser.skipIfNotSpaceAndChar('/');
            return vi;
        }
                
        // i/j/k or i/j
        vi.texcoord_index = parser.getFirstInt();
        parser.skipIfNotSpaceAndChar('/');
        if (parser.isNotChar('/')) 
            return vi;
        
        // i/j/k
        parser.incrementPointer();  // skip '/'
        vi.normal_index = parser.getFirstInt();
        parser.skipIfNotSpaceAndChar('/');
  
        return vi;
    }
   
    private static class index_t
    {
        public int vertex_index, texcoord_index, normal_index;
        
        public index_t()
        {
            vertex_index = texcoord_index = normal_index = (int)(0x80000000);
        }
        
        @Override
        public String toString()
        {
            return String.format("(%5d, %5d, %5d)", vertex_index, texcoord_index, normal_index);
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
