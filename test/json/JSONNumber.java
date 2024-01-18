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
public class JSONNumber extends JSONValue{
    
    double number;
    
    public JSONNumber(double value)
    {
        this.number = value;
    }

    @Override
    public boolean isNumber() {
        return true;
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
        return false;
    }

    @Override
    public String toString()
    {
        return Double.toString(number);
    }
}
