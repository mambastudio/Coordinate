/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

/**
 *
 * @author user
 */
public class RangeFloat implements Range<RangeFloat, Float>{
       
    private float minValue;
    private float maxValue; 
    private float value;
    
    public RangeFloat()
    {        
        this(0, 1, 0);
    }
    
    public RangeFloat(float value)
    {
        this(0, 1, value);
    }
    
    public RangeFloat(float minValue,
                      float maxValue,
                      float value)
    {
        if(minValue > maxValue)
            throw new UnsupportedOperationException("range provided does not define a bound "
                    + "(minValue = " +minValue+ ")"
                    + "(maxValue = " +maxValue+ ")");   
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = wrapValue(value); 
    }
    
    @Override
    public Float minValue() {
        return minValue;
    }

    @Override
    public Float maxValue() {
        return maxValue;
    }

    @Override
    public Float value() {
        return value;
    }
    
    @Override
    public RangeFloat copy()
    {
        return new RangeFloat(minValue, maxValue, value);
    }

    @Override
    public void setValue(Float value) {
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        return Float.toString(value);
    }
    
    private float wrapValue(float value) {       
        if (value < minValue())
            return minValue();
        else if (value > maxValue())
            return maxValue();
        return value;
    }
    
    @Override
    public Float getRangeSize()
    {
        return maxValue() - minValue();
    }
}
