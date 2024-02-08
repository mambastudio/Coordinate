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
public abstract class JSONValue {
    
    public abstract boolean isNumber();    
    public abstract boolean isString();     
    public abstract boolean isBoolean();
    public abstract boolean isArray();
    public abstract boolean isObject();
    public abstract boolean isNull();
    public abstract boolean isTrue();
    public abstract boolean isFalse();
    
    public double asDouble()
    {        
        if(isNumber())
            return ((JSONNumber)this).getDouble();
        else
            throw new UnsupportedOperationException("cannot be cast to number");
    }
    
    public int asInteger()
    {
        return (int)asDouble();
    }
    
    public String asString()
    {        
        if(isString())
            return ((JSONString)this).getString();
        else
            throw new UnsupportedOperationException("cannot be cast to string");
    }
    
    public boolean asBoolean()
    {        
        if(isBoolean())
        {            
            if(isFalse())
                return false;
            else if(isTrue())
                return true;
            else
                throw new UnsupportedOperationException("unrecognised boolean");
        }
        else
            throw new UnsupportedOperationException("cannot be cast to boolean");
    }
}
