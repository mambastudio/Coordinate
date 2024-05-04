/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

/**
 *
 * @author user
 * @param <R>
 * @param <C>
 */
public interface Range<R extends Range, C extends Number> {
    public C minValue();
    public C maxValue();
    public C value();
    public void setValue(C value);
    public C getRangeSize();
    public R copy();
}
