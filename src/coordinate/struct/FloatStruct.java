/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import coordinate.list.FloatList;
import coordinate.struct.refl.FloatStructField;
import java.io.Serializable;
import java.nio.FloatBuffer;
import java.util.List;

/**
 *
 * @author user
 */
public abstract class FloatStruct implements Serializable {
    private float[] globalArray = null;
    private int globalArrayIndex = -1;
        
    protected int getGlobalArrayIndex()
    {
        return globalArrayIndex;
    }
    
    protected void setGlobalArray(float[] globalArray, int index)
    {
        this.globalArray = globalArray;
        this.globalArrayIndex = index * getSize();
    }
    
    protected void refreshGlobalArray()
    {
        if(globalArray == null || globalArrayIndex == -1) return;
        float [] array = getArray();
        
        System.arraycopy(array, 0, globalArray, globalArrayIndex, getSize());
        
    }
    
    public void initFromGlobalArray()
    {
        List<FloatStructField> fields = FloatStructField.getAllStructFields(this);
        FloatBuffer buffer = FloatBuffer.wrap(globalArray, globalArrayIndex, getSize());
        
        for(int i = 0; i<fields.size(); i++)
        {
            FloatStructField field = fields.get(i);
            if(field.isFloat())
                field.setFloat(buffer.get());
            else if(field.isAbstractCoordinateFloat())
            {
                float[] array = new float[field.getNumberOfFloats()];
                buffer.get(array);
                field.setCoordinateFloat(array);
            }
        }
    }
    public float[] getArray()
    {
        List<FloatStructField> fields = FloatStructField.getAllStructFields(this);
        FloatList arrayList = new FloatList();
        
        for(int i = 0; i<fields.size(); i++)
        {
            FloatStructField field = fields.get(i);
            if(field.isFloat())
                arrayList.add(field.getFloat());
            else if(field.isAbstractCoordinateFloat())
                arrayList.add(field.getCoordinateFloat());
        }
        
        return arrayList.trim();
    }
    public int getSize()
    {
        List<FloatStructField> fields = FloatStructField.getAllStructFields(this);
        int i = 0;
        i = fields.stream().map((field) -> field.getNumberOfFloats()).reduce(i, Integer::sum);
        return i;
    }
    
    public float[] getGlobalArray()
    {
        return globalArray;
    }
}
