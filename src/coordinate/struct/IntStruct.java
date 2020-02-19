/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import coordinate.list.IntList;
import coordinate.struct.refl.IntStructField;
import java.nio.IntBuffer;
import java.util.List;

/**
 *
 * @author user
 */
public abstract class IntStruct {
    private int[] globalArray = null;
    private int globalArrayIndex = -1;
        
    protected int getGlobalArrayIndex()
    {
        return globalArrayIndex;
    }
    
    protected void setGlobalArray(int[] globalArray, int index)
    {
        this.globalArray = globalArray;
        this.globalArrayIndex = index * getSize();
    }
    
    protected void refreshGlobalArray()
    {
        if(globalArray == null || globalArrayIndex == -1) return;
        int [] array = getArray();
        
        System.arraycopy(array, 0, globalArray, globalArrayIndex, getSize());
        
    }
     
    public void initFromGlobalArray()
    {
        List<IntStructField> fields = IntStructField.getAllStructFields(this);
        IntBuffer buffer = IntBuffer.wrap(globalArray, globalArrayIndex, getSize());
        
        for(int i = 0; i<fields.size(); i++)
        {
            IntStructField field = fields.get(i);
            if(field.isInt())
                field.setInt(buffer.get());
            else if(field.isAbstractCoordinateInt())
            {
                int[] array = new int[field.getNumberOfInts()];
                buffer.get(array);
                field.setCoordinateInt(array);
            }
        }
    }
    public int[] getArray()
    {
        List<IntStructField> fields = IntStructField.getAllStructFields(this);
        IntList arrayList = new IntList();
        
        for(int i = 0; i<fields.size(); i++)
        {
            IntStructField field = fields.get(i);
            if(field.isInt())
                arrayList.add(field.getInt());
            else if(field.isAbstractCoordinateInt())
                arrayList.add(field.getCoordinateInt());
        }
        
        return arrayList.trim();
    }
    public int getSize()
    {
        List<IntStructField> fields = IntStructField.getAllStructFields(this);
        int i = 0;
        i = fields.stream().map((field) -> field.getNumberOfInts()).reduce(i, Integer::sum);
        return i;
    }
    
    public int[] getGlobalArray()
    {
        return globalArray;
    }
}
