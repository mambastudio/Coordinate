/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

/**
 *
 * @author jmburu
 * @param <DataType>
 * @param <DataValues>
 * @param <DataTransform>
 * @param <DataResults>
 * @param <DataFlags>
 * @param <FUnary>
 * @param <FBinary>
 */
public interface TemplateAlgorithms<
        DataType, DataValues, DataResults, DataTransform, DataFlags, 
        FUnary, FBinary>  {
       
    public DataType reduce(DataValues values, long n, DataResults result, FBinary f);
    
    default DataTransform transform(DataValues values, FUnary f){
        throw new UnsupportedOperationException("not yet supported");
    }        
    default DataType exclusive_scan(DataValues values, long n, DataResults results, FBinary f){
        throw new UnsupportedOperationException("not yet supported");
    }        
    default DataType exclusive_scan(DataValues values, long n, DataResults results){
        throw new UnsupportedOperationException("not yet supported");
    }     
    default DataType partition(DataValues values, DataResults results, long n, DataFlags flags){
        throw new UnsupportedOperationException("not yet supported");
    }
    default void sort_pairs(DataValues keys_in, DataValues values_in, DataResults keys_out, DataResults values_out, long n){
        throw new UnsupportedOperationException("not yet supported");
    }
}
