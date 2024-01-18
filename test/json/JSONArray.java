/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.util.ArrayList;

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
    public String toString()
    {
        return array.toString();
    }
}
