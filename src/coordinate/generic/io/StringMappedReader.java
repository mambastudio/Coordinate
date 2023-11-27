/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * 
 * Beauty about using memory mapped file is that you can go to any position of 
 * the file without any issue. Meaning you can peek, skip, go back to any positional
 * tokens in the file to verify something or do whatever you want in parsing
 * 
 */
public class StringMappedReader {
    
    private FileChannel channel;
    private MappedByteBuffer buffer;
    private String delimiter = "";
       
    public StringMappedReader(String directoryPath, String fileName)
    {
        Path path = FileSystems.getDefault().getPath(directoryPath, fileName);
        try {
            RandomAccessFile rfile = new RandomAccessFile(path.toFile(), "rw");  
            channel = rfile.getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, rfile.length());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        }               
    }
    
    public StringMappedReader(String delimiter, String directoryPath, String fileName)
    {
        this(directoryPath, fileName);
        Objects.requireNonNull(delimiter);
        this.delimiter = delimiter;
    }
    
    public StringMappedReader(URI uri)
    {
        File file = new File(uri);
        Path path = file.toPath();
        
        try {
            RandomAccessFile rfile = new RandomAccessFile(path.toFile(), "rw");  
            channel = rfile.getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        }          
    }
    
    public StringMappedReader(String delimiter, URI uri)
    {
        this(uri);
        this.delimiter = delimiter;
    }
    
    public String getNextToken()
    {
        goToStartToken();
        char[] buf = new char[500];
        int index = 0;
        while(true)
        {
            if(buffer.hasRemaining())
            {
                char c = (char)buffer.get();                
                if(Character.isWhitespace(c) || delimiter.contains(""+c))
                    break;
                else{
                    buf[index] = c;
                    index++;
                }
                
            }
            else
                break;
        }            
        
        if(index == 0)
            return null;
        else
            return new String(buf, 0, index);
    }
    
    public String getNextLine()
    {
        goToStartToken();
        String token = "";
        while(true)
        {
            if(buffer.hasRemaining())
            {
                char c = (char)buffer.get();
                if(c == '\n' || c == '\r')
                    break;
                else
                    token += c;
            }
            else
                break;
        }            
        
        return token;
    }
    
    public void goToStartToken()
    {
        while(true)
        {
            if(buffer.hasRemaining())
            {
                char c = (char) buffer.get();
                if(!Character.isWhitespace(c))
                {
                    //go back one step
                    goBack(buffer, 1);
                    break;
                }  
            }
            else
                break;
        }            
    }
    
    //TODO
    //add out of range detection
    //http://stackoverflow.com/questions/5710091/how-does-atoi-function-in-c-work
    public int atoi()
    {
        int value = 0;
        int sign = 1;
        
        boolean started = false;
        
        char c;
              
        outer:
        while(true)
        {            
            if(buffer.hasRemaining())
                c = (char) buffer.get();
            else
                break;
            
            //check if number is negative
            if( c == '+' || c == '-' )             
                if( c == '-' ) 
                {
                    sign = -1;
                    if(buffer.hasRemaining())
                        c = (char) buffer.get(); 
                    else
                        break;
                } 
            
            if(Character.isDigit(c))
                started = true;
            
            while(Character.isDigit(c)) 
            {
                value *= 10;
                value += (int) (c-'0'); //finished updating, read next
                if(buffer.hasRemaining())
                    c = (char) buffer.get();
                else 
                    break outer;
            }
            
            if(started)
            {
                goBack(buffer, 1); //go back one step
                break;
            }
        }
                
        return value * sign;       
        
    }
    
    public float atof()
    {
        return (float)atod();
    }
    //TODO
    //implement tinyobjloader complete implementation
    //https://stackoverflow.com/questions/34927307/whats-wrong-with-this-implementation-of-atof-from-kr
    public double atod()
    {
        int sign;
        double number = 0.0, power = 1.0;
               
        char c;
        
        if(buffer.hasRemaining())
            c = (char)buffer.get();
        else
            return 0;
                
        while(Character.isWhitespace(c))
        {            
            if(buffer.hasRemaining())
                c = (char)buffer.get();
            else
                return 0;
        }
           
        sign = (c == '-') ? -1 : 1; // Save the sign

        if(c == '-' || c == '+') // Skip the sign
        {
            if(buffer.hasRemaining())
                c = (char)buffer.get();
            else
                return 0;
        }
        
        while(Character.isDigit(c)) // Digits before the decimal point
        {                
            number = 10.0 * number + (c - '0');
            if(buffer.hasRemaining())
                c = (char)buffer.get();
            else 
                break;
        }
        
        if(c == '.') // Skip the decimal point
            if(buffer.hasRemaining())
                c = (char)buffer.get();
            else
                return sign * number / power;
        
        while(Character.isDigit(c)) // Digits after the decimal point
        {            
            number = 10.0 * number + (c - '0');
            power *= 10.0;
            if(buffer.hasRemaining())
                c = (char)buffer.get();
            else
                break;
        }
        return sign * number / power;
    }
    
    
    
    public int getNextInt() 
    {
        return Integer.parseInt(getNextToken());
    }

    public float getNextFloat() 
    {        
        if(peekNextTokenIsNumber())
           return Float.parseFloat(getNextToken());
        else
           return 0;
    }
    
    public char getNextChar()
    {        
        return (char)buffer.get();
    }
    
    public void skipTokens(int value)
    {
        String string;
        
        for(int i = 0; i<value; i++)
        {
            string = getNextToken();
            if(string == null)
                return;
        }
    }
    
    public String peekNextToken()
    {
        return peekNextToken(0);
    }
    
    public boolean peekNextTokenIsNumber()
    {
        String str = peekNextToken();
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    
    public String peekNextToken(int skip)
    {
        goToStartToken();
        int position = buffer.position(); 
        String string = null;
        for(int i = 0; i<skip+1; i++)
            string = getNextToken();
        buffer.position(position);
        return string;
    }
    
    public String peekNextLine()
    {
        goToStartToken();
        int position = buffer.position(); 
        String token = "";
        while(true)
        {
            if(buffer.hasRemaining())
            {
                char c = (char)buffer.get();
                if(c == '\n' || c == '\r')
                    break;
                else
                    token += c;
            }
            else
                break;
        }            
        buffer.position(position);
        return token;
    }
    
    public char peekNextChar(int skip)
    {
        char c = 0; if(skip <0) return c;
        int position = buffer.position(); 
        for(int i = 0; i<skip+1; i++)
            c = getNextChar();
        buffer.position(position);
        return c;
    }
    
    public void skipToken(int skip)
    {
        goToStartToken();
        for(int i = 0; i<skip+1; i++)
            getNextToken();
    }
    
    public boolean hasRemaining()
    {
        return buffer.hasRemaining();
    }
    
    public boolean hasRemainingToken()
    {
        return peekNextToken() != null;
    }
    
    public void rewind()
    {
        buffer.rewind();
    }
    
    public static void goBack(ByteBuffer cbuffer, int step)
    {
        int position = cbuffer.position();
        cbuffer.position(position - step);
    }
    
    public void close()
    {
        try {
            channel.close();
        } catch (IOException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private double ldexp(double x, double e)
    {
        return x * Math.pow(e, Math.pow(2, e));
    }
}
