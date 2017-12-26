/*
 * The MIT License
 *
 * Copyright 2016 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package coordinate.generic;

/**
 *
 * @author user
 */
public abstract class AbstractCoordinate {        
    public abstract float get(char axis);
    public abstract void set(char axis, float value);
    public abstract void set(float... values);  
    public abstract void setIndex(int index, float value);
    public abstract int getSize();    
    public abstract float[] getArray();
        
     public float get(int i) {
        switch (i) {
            case 0:
                return get('x');
            case 1:
                return get('y');
            default:
                return get('z');
        }
    }
     
    @Override
    public String toString()
    {
        float[] array = getArray();
        
        switch (getSize()) {
            case 4:
                return String.format("(%8.2f, %8.2f, %8.2f, %8.2f)", array[0], array[1], array[2], array[3]);
            case 3:
                return String.format("(%.2f, %.2f, %.2f)", array[0], array[1], array[2]);
            default:
                return String.format("(%.2f, %.2f)", array[0], array[1]);
        }
    }        
}
