/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.json.values;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author jmburu
 * @param <T>
 */
public class JSONArray<T extends JSONValue> extends JSONValue{
    
    public ArrayList<T> array;
    
    public JSONArray()
    {
        array = new ArrayList();
    }
    
    public void add(T t)
    {
        array.add(t);
    }
    
    public void add(T... arr)
    {
        array.addAll(Arrays.asList(arr));
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
        return true;
    }

    @Override
    public boolean isObject() {
        return false;
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
        return array.toString();
    }
    
    public ArrayList<T> getArray()
    {
        return array;
    }
    
    public int getSize()
    {
        return array.size();
    }
}
