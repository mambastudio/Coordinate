/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import coordinate.memory.functions.NativeIntegerAlgorithm;
import coordinate.memory.NativeInteger;
import java.util.function.IntBinaryOperator;

/**
 *
 * @author jmburu
 */
public class SerialNativeAlgorithm implements NativeIntegerAlgorithm{

    @Override
    public NativeInteger transform(NativeInteger values, IntBinaryOperator f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer exclusive_scan(NativeInteger values, long n, NativeInteger result) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer reduce(NativeInteger values, long n, NativeInteger result, IntBinaryOperator f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer partition(NativeInteger values, NativeInteger result, long n, NativeInteger flags) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sort_pairs(NativeInteger keys_in, NativeInteger values_in, NativeInteger keys_out, NativeInteger values_out, long n) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
