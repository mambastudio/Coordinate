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
 * @param <F>
 */
public interface TemplateAlgorithms<DataType, DataValues, DataResults, F>  {
    public DataResults transform(DataValues values, F f);
    public DataType exclusive_scan(DataValues values, long n, DataResults result);
    public DataType reduce(DataValues values, long n, DataResults result, F f);
    public DataType partition(DataValues values, DataResults result, long n, DataValues flags);
    public void sort_pairs(DataValues keys_in, DataValues values_in, DataResults keys_out, DataResults values_out, long n);
}
