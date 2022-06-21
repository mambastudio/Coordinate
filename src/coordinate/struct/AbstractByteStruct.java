/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import coordinate.utility.Value1Di;

/**
 *
 * @author user
 * @param <GlobalBuffer>
 */
public abstract class AbstractByteStruct<GlobalBuffer> implements AbstractStruct {
    public abstract int getByteSize();   
    public abstract void setupOffset(Value1Di offset);
    
    //global buffer and index
    public abstract void setGlobalBuffer(GlobalBuffer globalBuffer);
    public abstract void setGlobalIndex(int globalIndex);
    
    //field values for struct as byte array
    public abstract byte[] getFieldValuesAsArray();
    //get field values from global array
    public abstract void  setFieldValuesFromGlobalArray();
    
    //refresh global arrays from field value
    public abstract void refreshGlobalArray();  
    //refresh field values from global arrays
    public abstract void refreshFieldValues();
    
    //alignment for memory
    public abstract int setupAlignment();
    
}
