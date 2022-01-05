/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.io;

import coordinate.list.FloatList;
import coordinate.list.IntList;
import java.io.File;
import java.net.URI;

/**
 *
 * @author user
 */
public class LineMappedReader extends CharMappedReader {
    
    public LineMappedReader(String directoryPath, String fileName) {
        super(directoryPath, fileName);
    }
    
    public LineMappedReader(URI uri)
    {
        super(uri);
    }
    
    public LineMappedReader(File file)
    {
        super(file.toURI());
    }
    
    public String readLineString()
    {
        int previousPos = buffer.position();
        
        int end = length_until_newline();
        StringBuilder builder = new StringBuilder(end);
        for(int i = 0; i<end; i++)
            builder.append(getChar());
        
        buffer.position(previousPos);
        return builder.toString();
    }
        
    public float[] readLineFloatArray()
    {
        int previousPos = buffer.position();
        
        FloatList list = new FloatList(100);
        int limit = getEndOfLinePosition();
        double result[] = new double[]{0};
        while(position() < limit)
        {
            boolean read = parseDouble(limit, result);
            if(read)
                list.add((float) result[0]);
        }
       
        buffer.position(previousPos);
        return list.trim();
    }
    
    public int[] readLineIntArray()
    {
        int previousPos = buffer.position();
        
        IntList list = new IntList(100);
        int limit = getEndOfLinePosition();
        int result[] = new int[]{0};
        while(position() < limit)
        {
            boolean read = parseInteger(limit, result);
            if(read)
                list.add(result[0]);
        }
        
        buffer.position(previousPos);
        return list.trim();
    }
    
    public int[] readIntArrayUntilSpace()
    {        
        int previousPos = buffer.position();
        
        IntList list = new IntList(100);
        int limit = this.getUntilSpacePosition();
        int result[] = new int[]{0};        
        while(position() < limit)
        {
            boolean read = parseInteger(limit, result);
            if(read)
                list.add(result[0]);
        }
        
        buffer.position(previousPos);
        return list.trim();
    }    
    
    public int length_until_space() 
    {
        int previousPos = buffer.position();  //mark position
        
        int len = 0;       
        char c;
        if(buffer.hasRemaining())
        {
            c = (char)buffer.get();           
        }
        else
            return -1;

        while(true)
        {
            len++;
            
            if (Character.isWhitespace(c)) { 
                len--;
                break;                
            }
            
            if(buffer.hasRemaining())
                c = (char)buffer.get();
            else
                break;
            
            
        }  
        buffer.position(previousPos); //reset to initial start position
        return len;
    }
    
    public int length_until_newline() 
    {
        int previousPos = buffer.position();  //mark position
        int start = buffer.position();
        this.goToEndLine();
        int end = buffer.position();
        buffer.position(previousPos); //reset to initial start position
        return end - start;
    }
    
    public boolean currentLineContains(String target)
    {
        return currentLineIndexOf(target)>-1;
    }
    
    public int currentLineIndexOf(String target)
    {
        int length = length_until_newline();
        int previousPos = buffer.position(); //store position
        
        StringBuilder builder = new StringBuilder(length); //it's like a simple char array
        for(int i = 0; i<length; i++)
            builder.append(getChar());        
        int index = builder.indexOf(target);
        
        buffer.position(previousPos); //reset position
        return index;
    }
    
    public boolean currentLineContainsIsolated(String target)
    {
        int length = length_until_newline();
        int previousPos = buffer.position(); //store current position
        
        //read in the line 
        StringBuilder builder = new StringBuilder(length);
        for(int i = 0; i<length; i++)
            builder.append(getChar());      
                
        //get first match
        int index = builder.indexOf(target);
        if(index < 0) 
        {
            buffer.position(previousPos);
            return false;
        }
        
        //last index exclusive of the target string characters
        int lastIndex = index + target.length();         
                
        //hope next char at new index
        if(!isSpace(builder.charAt(lastIndex)))
        {
            buffer.position(previousPos);
            return false;
        }
        
        
        buffer.position(previousPos); //reset position
        return true;
    }
    
    public void goToNextDefinedLine()
    {              
        goToEndLine();            
        goToStartChar();
    }
    
        
    
}
