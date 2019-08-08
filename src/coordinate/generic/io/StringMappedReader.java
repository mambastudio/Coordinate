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
    
    public String getNextToken()
    {
        goToStartToken();
        String token = "";
        while(true)
        {
            if(buffer.hasRemaining())
            {
                char c = (char)buffer.get();
                if(Character.isWhitespace(c))
                    break;
                else
                    token += c;
            }
            else
                break;
        }            
        
        if(token.equals(""))
            return null;
        else
            return token;
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
        
        if(token.equals(""))
            return null;
        else
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
    
    public int getNextInt() 
    {
        return Integer.parseInt(getNextToken());
    }

    public float getNextFloat() 
    {
        return Float.parseFloat(getNextToken());
    }
    
    public char getNextChar()
    {        
        return (char)buffer.get();
    }
    
    public void skipTokens(int value)
    {
        String string = null;
        
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
}
