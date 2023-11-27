/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

/**
 *
 * @author user
 */
public interface AbstractCoordinateInteger extends AbstractCoordinate {
    public int get(char axis);
    public void set(char axis, int value);
    public void set(int... values);  
    public void setIndex(int index, int value);
    public int[] getArray();
        
    public default int get(int i) {
        switch (i) {
            case 0:
                return get('x');
            case 1:
                return get('y');
            case 2:
                return get('z');
            case 3:
                return get('w');
            default:
                throw new UnsupportedOperationException("coordinate is not defined");
        }
    }
     
    public default String getString()
    {
        int[] array = getArray();
        
        switch (getSize()) {
            case 4:
                return String.format("(%8d, %8d, %8d, %8d)", array[0], array[1], array[2], array[3]);
            case 3:
                return String.format("(%8d, %8d, %8d)", array[0], array[1], array[2]);
            default:
                return String.format("(%8d, %8d)", array[0], array[1]);
        }
    }        
}
