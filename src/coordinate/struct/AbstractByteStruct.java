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
public interface AbstractByteStruct<GlobalBuffer> extends AbstractStruct {
    public int getByteSize();   
    public void setupOffset(Value1Di offset);
    
    //global buffer and index
    public void setGlobalBuffer(GlobalBuffer globalBuffer);
    public void setGlobalIndex(int globalIndex);
    
    //field values for struct as byte array
    public byte[] getFieldValuesAsArray();
    //get field values from global array
    public void  setFieldValuesFromGlobalArray();
    
    //alignment for memory
    public int setupAlignment();
    
}
