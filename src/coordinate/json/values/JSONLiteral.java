/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.json.values;

/**
 *
 * @author user
 */
public class JSONLiteral extends JSONValue {
    
    private String string;
        
    public JSONLiteral(String string)
    {
        
        if(string.equals("true") || string.equals("false") || string.equals("null"))
            this.string = string;
        else
            throw new UnsupportedOperationException("literal: " +string+ " is neither a boolean or null");
        
    }
    
    public JSONLiteral(boolean value)
    {
        this(Boolean.toString(value));
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
        return string.equals("true") || string.equals("false");
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
    public boolean isNull() {
        return string.equals("null");
    }

    @Override
    public boolean isTrue() {
        return string.equals("true");
    }

    @Override
    public boolean isFalse() {
        return string.equals("false");
    }
    
    @Override
    public String toString()
    {
        return string;
    }
}
