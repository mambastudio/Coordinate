/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.json.values;

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
    
    public double getDouble()
    {
        return number;
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
}
