/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.function;

/**
 *
 * @author user
 * @param <T>
 * @param <U>
 * @param <V>
 * @param <R>
 */
public interface TriFunction<T, U, V, R> {
    public R apply(T t, U u, V v);
}
