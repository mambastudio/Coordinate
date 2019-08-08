/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

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
     
    public abstract void  initFromGlobalArray();
    public abstract int [] getArray();
    public abstract int getSize();  
    
    public int[] getGlobalArray()
    {
        return globalArray;
    }
}
