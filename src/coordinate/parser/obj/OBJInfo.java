/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser.obj;

import coordinate.generic.io.LineMappedReader;
import coordinate.generic.io.StringReader;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.GROUP;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.NONE;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.OBJECT;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.USEMTL;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public LineMappedReader reader = null;
    private SplitOBJPolicy splitPolicy = NONE;
    
    public OBJInfo()
    {
        
    }
    
    public OBJInfo(String stringFile) 
    {       
        File file = new File(stringFile);
        if(!file.exists())
            try {
                throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OBJInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        reader = new LineMappedReader(file.toURI());
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
        
        reader = new LineMappedReader(file.toURI());
    }
    
    //super fast read of file (memory mapped)
    public OBJInfo read()
    {
        reader.goToStartChar();
        
        while(reader.hasRemaining())
        {            
            if(reader.isCurrentIsolated("v"))
                v++;
            else if(reader.isCurrentIsolated("vn"))
                vn++;
            else if(reader.isCurrentIsolated("vt"))
                vt++;
            else if(reader.isCurrentIsolated("usemtl"))
                usemtl++;
            else if(reader.isCurrentIsolated("f"))
            {
                int intCount = reader.countInts();
                if(intCount == 4 || intCount == 8 || intCount == 12) //means it might be a quad hence break it into two
                    f+=2;
                else
                    f++;
            }                
            else if(reader.isCurrentIsolated("o"))
                o++;
            else if(reader.isCurrentIsolated("g"))
                g++;
            reader.goToNextDefinedLine();            
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
    
    //read from string
    public void readAttributesString(String string)
    {
        InputStream is = new ByteArrayInputStream(string.getBytes());
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
        readAttributesString(new StringReader(br));
    }
    
    private void readAttributesString(StringReader parser)
    {        
        while(parser.hasNext())
        {
            String currentToken = parser.getNextToken();            
            switch (currentToken) {
                case "v":
                    v++;
                    break;                
                case "vt":
                    vt++;
                    break;
                case "vn":
                    vn++;
                    break;
                case "f":
                    int intCount = 0;
                    while(parser.peekNextTokenIsNumber())
                    {
                        intCount++;
                        parser.skipTokens(1);
                    }
                    if(intCount == 4 || intCount == 8 || intCount == 12) //means it might be a quad hence break it into two
                        f+=2;
                    else
                        f++;
                    break;
                case "g":
                    g++;
                    break;  
                case "o":
                    o++;
                    break;
                case "usemtl":
                    usemtl++;
                    break;
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
    }
    
    public OBJInfo readAndClose()
    {
        read();            
        return this;
    }
    
    public List<SplitOBJPolicy> getAvailableSplitPolicy()
    {
        ArrayList<SplitOBJPolicy> list = new ArrayList();
        
        if(usemtl > 0)
            list.add(USEMTL);
        if(o > 0)
            list.add(OBJECT);
        if(g > 0)
            list.add(GROUP);
        
        return list;
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
    public void setSplitPolicy(SplitOBJPolicy splitPolicy){this.splitPolicy = splitPolicy;}
    
    
    public HashMap<String, String> getInfoString()
    {
        HashMap<String, String> map = new HashMap();
        map.put("vertices:", Integer.toString(v));
        map.put("normals:", Integer.toString(vn));
        map.put("uv vertices:", Integer.toString(vt));
        map.put("usemtl:", Integer.toString(usemtl));
        map.put("faces:", Integer.toString(f));
        map.put("objects:", Integer.toString(o));
        map.put("groups:", Integer.toString(g));
        return map;
    }
}
