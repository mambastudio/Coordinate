/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import java.io.Serializable;

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
     
    public abstract void  initFromGlobalArray();
    public abstract float[] getArray();
    public abstract int getSize();  
    
    public float[] getGlobalArray()
    {
        return globalArray;
    }
}
