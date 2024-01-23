/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.json;

import coordinate.json.values.JSONArray;
import coordinate.json.values.JSONObject;
import coordinate.json.values.JSONValue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jmburu
 */
public class JSONFormatString {
    private static int indent = 0;
    public static int increment = 3;
    
    public static String getString(JSONValue object)
    {
        if(object.isObject())
            return getString((JSONObject)object);
        else if(object.isArray())
            return getString((JSONArray)object);   
        else
            throw new UnsupportedOperationException("object to print is not supported");
    }
    
    public static String getString(JSONObject object)
    {
        return getStringObject(object, false);
    }
    
    public static String getString(JSONArray object)
    {
        return getStringArray(object, false);
    }
    
    public static String getStringObject(JSONObject object, boolean newLineBefore)
    {
        StringBuilder builder = new StringBuilder();
        if(newLineBefore)
            builder.append("\n").append(getIndentSpace()).append("{").append("\n");    
        else
            builder.append(getIndentSpace()).append("{").append("\n");    
        indent += increment;
        AtomicInteger index = new AtomicInteger();
        final int size = object.getSize();
        object.getSet().forEach(entry -> {
            JSONValue value = entry.getValue();
            String comma = index.get() >= size-1 ? "" : ",";
            if(value.isObject())
                builder.append(getIndentSpace()).append("\"").append(entry.getKey()).append("\"").append(" : ").append(getStringObject((JSONObject) entry.getValue(), true)).append(comma).append("\n");
            else if(value.isArray())
                builder.append(getIndentSpace()).append("\"").append(entry.getKey()).append("\"").append(" : ").append(getStringArray((JSONArray) entry.getValue(), true)).append(comma).append("\n");
            else if(value.isString())
                builder.append(getIndentSpace()).append("\"").append(entry.getKey()).append("\"").append(" : ").append("\"").append(entry.getValue()).append("\"").append(comma).append("\n");
            else
                builder.append(getIndentSpace()).append("\"").append(entry.getKey()).append("\"").append(" : ").append(entry.getValue()).append(comma).append("\n");
            index.incrementAndGet();
            
        });
        indent -= increment;
        builder.append(getIndentSpace()).append("}");       
        return builder.toString();
    }
    
    public static String getStringArray(JSONArray<? extends JSONValue> object, boolean newLineBefore)
    {
        StringBuilder builder = new StringBuilder();
        if(newLineBefore)
            builder.append("\n").append(getIndentSpace()).append("[").append("\n");    
        else
            builder.append(getIndentSpace()).append("[").append("\n");   
        indent += increment;
        AtomicInteger index = new AtomicInteger();
        final int size = object.getSize();
        object.getArray().forEach(value -> {            
            String comma = index.get() >= size-1 ? "" : ",";
            if(value.isObject())
                builder.append(getStringObject((JSONObject) value, false)).append(comma).append("\n");
            else if(value.isArray())
                builder.append(getIndentSpace()).append(getStringArray((JSONArray) value, false)).append(comma).append("\n");
            else
                builder.append(getIndentSpace()).append(value).append(comma).append("\n");
            index.incrementAndGet();
            
        });
        indent -= increment;
        builder.append(getIndentSpace()).append("]");       
        return builder.toString();
    }
    
    private static String getIndentSpace()
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<indent; i++)
            builder.append(' ');
        return builder.toString();
    }
}
