/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.json;

import coordinate.json.values.JSONObject;
import coordinate.json.values.JSONLiteral;
import coordinate.json.values.JSONNumber;
import coordinate.json.values.JSONString;
import coordinate.json.values.JSONArray;
import coordinate.json.values.JSONValue;
import coordinate.generic.io.CharReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmburu
 * 
 * https://www.json.org/json-en.html
 * https://jsonformatter.org/
 * https://github.com/json-iterator/test-data 
 * 
 */
public class JSONParserWhole {
    CharReader reader;
    public JSONParserWhole(Path path)
    {
        reader = new CharReader(path);
        try {
            reader.skipSpace();
        } catch (IOException ex) {
            Logger.getLogger(JSONParserWhole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JSONValue parse()
    {
        JSONValue value = null;
        try {
            if(reader.isNext('['))
                value = readArray();
            else if(reader.isNext('{'))
                value = readObject();
            else throw new UnsupportedOperationException("json file starts with " +reader.peekNext());
        } catch (IOException ex) {
            Logger.getLogger(JSONParserWhole.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Optional<JSONValue> optional = Optional.of(value);
        return optional.get();
    }
    
    private double readNumber() throws IOException
    {
        reader.skipSpace();         
        double value = reader.nextDouble();        
        reader.skipSpace();
        return value;
    }
    
    //TODO: deal with escape characters according to json specifications
    private String readString() throws IOException
    {
        reader.skipCharOnceAndSurroundingSpaces('"');
        StringBuilder builder = new StringBuilder();
        while(reader.hasNext() && !reader.isNext('"')) 
        {
            if(reader.isNext('\\')) //understand why java doesn't like \* as string
                builder.append(reader.nextString(2));            
            else
                builder.append(reader.nextChar());
        }   
        reader.skipCharOnceAndSurroundingSpaces('"');
        return builder.toString();
    }
    
    private String readLiteral() throws IOException
    {                
        reader.skipSpace();
        StringBuilder builder = new StringBuilder();
        while(reader.hasNext())      
        {
            if(reader.isNext('}') || reader.isNext(',') || reader.isNextAllSpace())
                break;
            builder.append(reader.nextChar());
        }  
        reader.skipSpace();
        return builder.toString();
    }
    
    private JSONObject readObject() throws IOException
    {
        JSONObject object = new JSONObject();
        if(reader.isNext('{'))
            reader.skipCharOnceAndSurroundingSpaces('{');
        while(reader.hasNext() && !reader.isNext('}'))
        {
            String key = readKey(); 
            reader.skipCharOnceAndSurroundingSpaces(':'); //:  
            JSONValue value = readValue();                    
            object.put(key, value);
            if(reader.isNext(','))
                reader.skipCharOnceAndSurroundingSpaces(',');
        }
        reader.skipCharOnceAndSurroundingSpaces('}');
        return object;
    }
        
    private JSONArray readArray() throws IOException
    {
        JSONArray array = new JSONArray();
        if(reader.isNext('['))
            reader.skipCharOnceAndSurroundingSpaces('[');
        while(reader.hasNext() && !reader.isNext(']'))
        {
            JSONValue value = readValue();
            array.add(value); 
            if(reader.isNext(','))
                reader.skipCharOnceAndSurroundingSpaces(',');                   
        }
        reader.skipCharOnceAndSurroundingSpaces(']');
        return array;
    }
    
    private String readKey() throws IOException
    {
        reader.skipSpace();
        String key = readString();
        reader.skipSpace();
        return key;
    }
    
    private JSONValue readValue() throws IOException
    {
        reader.skipSpace();
        JSONValue value;
        if(reader.isNext('"'))
            value = new JSONString(readString());
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
