/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser.obj;

import coordinate.generic.io.StringMappedReader;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.GROUP;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.NONE;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.OBJECT;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.USEMTL;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * 
 * Important for reading obj file since the structure of the format is not
 * standardized at all, hence parsing can be quite a hurdle for different file
 * types. This proves the importance of standardizing products.
 * 
 */
public class OBJInfo {
    public enum SplitOBJPolicy{
        USEMTL, GROUP, OBJECT, NONE
    }
    
    private int v = 0;
    private int vt = 0;
    private int vn = 0;
    private int usemtl = 0;
    private int f = 0;
    private int o = 0;
    private int g = 0;
    
    private StringMappedReader reader = null;    
    private SplitOBJPolicy splitPolicy = NONE;
    
    public OBJInfo(String stringFile) 
    {       
        File file = new File(stringFile);
        if(!file.exists())
            try {
                throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OBJInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        reader = new StringMappedReader(file.toURI());
    }
    
    public OBJInfo(URI uri)
    {
        File file = new File(uri);
        if(!file.exists())
            try {
                throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OBJInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        reader = new StringMappedReader(file.toURI());
    }
       
    //super fast read of file (memory mapped)
    public OBJInfo read()
    {
        while(reader.hasRemaining())
        {    
            char c = reader.getNextChar();
            switch (c) {
                case 'v':
                    {
                        char c1 = reader.peekNextChar(0);
                        if(c1 == 't')
                            vt++;
                        else if(c1 == 'n')
                            vn++;
                        else if(Character.isWhitespace(c1))
                            v++;break;
                    }
                case 'f':
                    {
                        char c1 = reader.peekNextChar(0);
                        char c2 = reader.peekNextChar(1);
                        if(Character.isWhitespace(c1) && Character.isDigit(c2))
                            f++;break;
                    }
                case 'o':
                    {
                        char c1 = reader.peekNextChar(0);
                        if(Character.isWhitespace(c1))
                            o++;break;
                    }
                case 'g':
                    {
                        char c1 = reader.peekNextChar(0);
                        if(c1 == '\r' || c1 == '\n') {
                            //do nothing
                        } else if (Character.isWhitespace(c1))
                            g++;break;
                    }
                case 'u':
                    {
                        char c1 = reader.peekNextChar(0);
                        char c2 = reader.peekNextChar(1);
                        char c3 = reader.peekNextChar(2);
                        char c4 = reader.peekNextChar(3);
                        char c5 = reader.peekNextChar(4);
                        if(c1 == 's' && c2 == 'e' &&
                           c3 == 'm' && c4 == 't' &&
                           c5 == 'l')
                            usemtl++;
                        break;
                    }                    
                default:
                    break;
            }
        }
        
        if(o>g && o>usemtl)
            splitPolicy = OBJECT;
        else if(g>o && g>usemtl)
            splitPolicy = GROUP;
        else if(usemtl>o && usemtl>g)
            splitPolicy = USEMTL;
        else if(usemtl == o && o == g && g > 0)  
            splitPolicy = USEMTL;
        else if(usemtl == o && o == g && g == 0)  
            splitPolicy = NONE;
        else if(usemtl>0 && usemtl == o)
            splitPolicy = USEMTL;
        else if(usemtl>0 && usemtl == g)
            splitPolicy = USEMTL;
        else if(o > 0 && o == g)
            splitPolicy = OBJECT;
       
            
                
        return this;
    }
    
    public OBJInfo readAndClose()
    {
        read();
        reader.close();                
        return this;
    }
    
    public StringMappedReader getStringMappedReader()
    {
        reader.rewind();
        return reader;
    }
    
    @Override
    public String toString()
    {
        
        StringBuilder builder = new StringBuilder();
        builder.append("v            ").append(v).append("\n");
        builder.append("vn           ").append(vn).append("\n");
        builder.append("vt           ").append(vt).append("\n");
        builder.append("usemtl       ").append(usemtl).append("\n");
        builder.append("f            ").append(f).append("\n");
        builder.append("o            ").append(o).append("\n");
        builder.append("g            ").append(g).append("\n");     
        builder.append("split policy ").append(splitPolicy);
        return builder.toString();
    }
    
    public int v(){return v;}
    public int vn(){return vn;}
    public int vt(){return vt;}
    public int usemtl(){return usemtl;}
    public int f(){return f;}
    public int o(){return o;}
    public int g(){return g;}
    public SplitOBJPolicy splitPolicy(){return splitPolicy;}
}
