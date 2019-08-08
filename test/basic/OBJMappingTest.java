/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.list.FloatList;
import coordinate.list.IntList;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class OBJMappingTest {
    public enum MeshType{FACE, FACE_NORMAL, FACE_UV_NORMAL, FACE_UV};
    static ArrayList<String> objects;
    static ArrayList<String> groups;
    static FloatList vArray;
    static FloatList vtArray;
    static FloatList vnArray;
    static IntList fArray;
    
        
    public static void main(String... args) throws Exception
    {
        RandomAccessFile file = new RandomAccessFile("C:\\Users\\user\\Documents\\Scene3d\\roadBike\\roadBike.obj", "rw");
        MappedByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());     
        parse1(buffer);           
    }
    
    public static void parse1(MappedByteBuffer cbuffer)
    {          
        int v = 0;
        int vt = 0;
        int vn = 0;
        int f = 0;
        int o = 0;
        int g = 0;
      
        while(cbuffer.hasRemaining())
        {            
            char c = (char) cbuffer.get();             
            if(c == 'v') 
                if(cbuffer.hasRemaining())
                {
                    char nxt = (char) cbuffer.get();
                    switch (nxt) {
                        case 't':
                            vt++;
                            break;
                        case 'n':
                            vn++;
                            break;
                        case ' ':
                            v++;
                            break;
                    }
                }
            if(c == 'f')
                if(cbuffer.hasRemaining())
                {
                    char nxt = (char) cbuffer.get();
                    switch (nxt) {
                        case ' ':
                            f++;
                            break;
                    }                   
                }
            if(c == 'o')
                if(cbuffer.hasRemaining())
                {
                    char nxt = (char) cbuffer.get();
                    switch (nxt) {
                        case ' ':
                            o++;
                            break;
                    }                   
                }               
            if(c == 'g')
                if(cbuffer.hasRemaining())
                {
                    char nxt = (char) cbuffer.get();
                    switch (nxt) {
                        case ' ':
                            g++;
                            break;
                    }                   
                }     
        }
               
        vArray = new FloatList(v*3);
        vtArray = new FloatList(vt*2);
        vnArray = new FloatList(vn*3);
        fArray = new IntList(f*10);
        objects = new ArrayList(o);
        
        System.out.println("v     " +v);
        System.out.println("vn    " +vn);
        System.out.println("vt    " +vt);
        System.out.println("f     " +f);
        System.out.println("o     " +o);
        System.out.println("g     " +g);
    }
    
    public static char peekNextChar(CharBuffer cbuffer)
    {
        int currentPostion = cbuffer.position();            
        char peekChar = cbuffer.get();
        cbuffer.position(currentPostion);
        return peekChar;
    }
    
    //put this in a thread or java future fibers
    public static void parseLine(CharBuffer cbuffer)
    {
        String line = readLine(cbuffer);
        String[] tokens = line.trim().split("[/\\s]+");
        switch (tokens[0]) {
            case "v":
                float v1 = Float.parseFloat(tokens[1]);
                float v2 = Float.parseFloat(tokens[2]);
                float v3 = Float.parseFloat(tokens[3]);
                vArray.add(v1, v2, v3);
                break;
            case "vn":
                {
                    float vn1 = Float.parseFloat(tokens[1]);
                    float vn2 = Float.parseFloat(tokens[2]);
                    float vn3 = Float.parseFloat(tokens[3]);
                    vnArray.add(vn1, vn2, vn3);
                    break;
                }
            case "vt":
                {
                    float vt1 = Float.parseFloat(tokens[1]);
                    float vt2 = Float.parseFloat(tokens[2]);
                    vtArray.add(vt1, vt2);
                    break;
                }
            case "f":
                {
                    IntList intArray = new IntList();
                   
                    for(String token: tokens)
                    {
                        try
                        {
                            int value = Integer.parseInt(token);
                            intArray.add(value);
                        }
                        catch(NumberFormatException e)
                        {
                            //IGNORE
                        }                                    
                    }
                    int[] array = intArray.trim();  
                    break;
                }
            default:
                break;
        }
               
    }
    public static String readLine(CharBuffer cbuffer)
    {
        goBack(cbuffer, 2);
        String line = "";
        while(true)
        {
            char c = cbuffer.get();
            if(c == '\n' || c == '\r')
                break;
            else
                line += c;
        }            
        return line;
    }
    
    public static void goBack(CharBuffer cbuffer, int step)
    {
        int position = cbuffer.position();
        cbuffer.position(position - step);
    }
    
}
