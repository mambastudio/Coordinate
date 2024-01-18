/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import coordinate.generic.io.CharReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmburu
 */
public class JSONParser {
    CharReader reader;
    public JSONParser(Path path)
    {
        reader = new CharReader(path);
        try {
            reader.skipSpace();
        } catch (IOException ex) {
            Logger.getLogger(JSONParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JSONObject parse()
    {
        JSONObject object = null;
        try {
            object = readObject();
        } catch (IOException ex) {
            Logger.getLogger(JSONParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        Optional<JSONObject> optional = Optional.of(object);
        return optional.get();
    }
    
    private double readNumber() throws IOException
    {
        reader.skipSpaceAndBeyond(' ');         
        double value = reader.nextDouble();        
        reader.skipSpaceAndBeyond(' ');
        return value;
    }
    
    private String readQuotes() throws IOException
    {
        reader.skipSpaceAndBeyond('"');
        StringBuilder builder = new StringBuilder();
        while(reader.hasNext() && !reader.isNext('"'))        
            builder.append(reader.nextChar());   
        reader.skipSpaceAndBeyond('"');
        return builder.toString();
    }
    
    private String readLiteral() throws IOException
    {                
        StringBuilder builder = new StringBuilder();
        while(reader.hasNext())      
        {
            if(reader.isNext('}') || reader.isNext(',') || reader.isNextAllSpace())
                break;
            builder.append(reader.nextChar());
        }  
        return builder.toString();
    }
    
    private JSONObject readObject() throws IOException
    {
        JSONObject object = new JSONObject();
        if(reader.isNext('{'))
            reader.skipSpaceAndBeyond('{');
        while(reader.hasNext() && !reader.isNext('}'))
        {
            String key = parseKey(); 
            reader.skipSpaceAndBeyond(':'); //:  
            JSONValue value = parseValue();                    
            object.put(key, value);
            if(reader.isNext(','))
                reader.skipSpaceAndBeyond(',');
        }
        reader.skipSpaceAndBeyond('}');
        return object;
    }
        
    private JSONArray readArray() throws IOException
    {
        JSONArray array = new JSONArray();
        if(reader.isNext('['))
            reader.skipSpaceAndBeyond('[');
        while(reader.hasNext() && !reader.isNext(']'))
        {
            JSONValue value = parseValue();
            array.add(value);            
            if(reader.isNext(','))
                reader.skipSpaceAndBeyond(',');
        }
        reader.skipSpaceAndBeyond(']');
        return array;
    }
    
    private String parseKey() throws IOException
    {
        reader.skipSpace();
        String key = readQuotes();
        reader.skipSpace();
        return key;
    }
    
    private JSONValue parseValue() throws IOException
    {
        reader.skipSpace();
        JSONValue value;
        if(reader.isNext('"'))
            value = new JSONString(readQuotes());
        else if(reader.isNextDigit())
            value = new JSONNumber(readNumber());
        else if(reader.isNext('{'))
            value = readObject();
        else if(reader.isNext('['))        
            value = readArray();  
        else if(reader.isNext('t') || reader.isNext('f') || reader.isNext('n') )        
            value = new JSONLiteral(readLiteral());
        else
            value = null;
        reader.skipSpace();
        return value;
    }
}
