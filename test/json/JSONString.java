/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 *
 * @author jmburu
 */
public class JSONString extends JSONValue{
    String string;
    
    public JSONString(String string)
    {
        this.string = string;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isString() {
        return true;
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
        return false;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
}
