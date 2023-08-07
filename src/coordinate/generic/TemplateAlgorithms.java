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
 * @param <DataResults>
 * @param <FUnary>
 * @param <FBinary>
 */
public interface TemplateAlgorithms<DataType, DataValues, DataResults, FUnary, FBinary>  {
    public DataResults transform(DataValues values, FUnary f);
    public DataType exclusive_scan(DataValues values, long n, DataResults result);
    public DataType reduce(DataValues values, long n, DataResults result, FBinary f);
    public DataType partition(DataValues values, DataResults result, long n, DataValues flags);
    public void sort_pairs(DataValues keys_in, DataValues values_in, DataResults keys_out, DataResults values_out, long n);
}
