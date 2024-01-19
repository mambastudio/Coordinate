/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.json.values;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author jmburu
 */
public class JSONObject extends JSONValue{

    Map<String, JSONValue> map;
    
    public JSONObject()
    {
        map = new LinkedHashMap();
    }
    
    public void put(String key, JSONValue value)
    {
        map.put(key, value);
    }
    
    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }
    
    
    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isTrue() {
        return false;
    }

    @Override
    public boolean isFalse() {
        return false;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append("\n");
        map.entrySet().forEach(_item -> {
            builder.append(_item.getKey()).append(": ").append(_item.getValue()).append("\n");
        });
        builder.append("}");
        return builder.toString();
    }

    public Set<Entry<String, JSONValue>> getSet()
    {
        return map.entrySet();
    }
    
    public int getSize()
    {
        return map.size();
    }
}
