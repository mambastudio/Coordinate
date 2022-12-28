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
package coordinate.transform;

/**
 *
 * @author user
 */
public final class Matrix {
     // m_row_col; stored column major
    public float[] m = new float[16];

    public Matrix()
    {
        for(int i=0; i<4; i++) 
            this.set(i, i, 1);
    }
    
    public Matrix(float a)
    { 
        for(int i = 0; i<16; i++)
            m[i] = a; 
    }

    public void addAssign(int r, int c, float a)
    {
        float oldValue = get(r, c);
        set(r, c, oldValue + a);
    }
    
    public float get(int i)
    {
        return m[i];
    }
    
    public float get(int r, int c)
    {
        return m[r + 4*c];
    }
    
    public void set(int r, int c, float value)
    {
        m[r + 4*c] = value;
    }
    
    public void set(int i, float value)
    {
        m[i] = value;
    }
    
    public void setRow(int r, float a, float b, float c, float d)
    {
        set(r, 0, a);
        set(r, 1, b);
        set(r, 2, c);
        set(r, 3, d);
    }
    
    public Matrix transpose()
    {
        Matrix mat = new Matrix();
        mat.setRow(0, get(0, 0), get(1, 0), get(2, 0), get(3, 0));
        mat.setRow(1, get(0, 1), get(1, 1), get(2, 1), get(3, 1));
        mat.setRow(2, get(0, 2), get(1, 2), get(2, 2), get(3, 2));
        mat.setRow(3, get(0, 3), get(1, 3), get(2, 3), get(3, 3));        
        return mat;
    }
    
    public static Matrix identity()
    {
        Matrix res = new Matrix();
        for(int i=0; i<4; i++) 
            res.set(i, i, 1);
        return res;
    }
  
    public Matrix mul(Matrix m)
    {
        return mul(this, m);
    }
    
    public static Matrix mul(Matrix left, Matrix right)
    {
        Matrix res = new Matrix();
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                res.set(i, j,  left.get(i, 0) * right.get(0, j)
                            +  left.get(i, 1) * right.get(1, j)
                            +  left.get(i, 2) * right.get(2, j)
                            +  left.get(i, 3) * right.get(3, j)
                );
            }
        }
        return res;
    }
    
    // Code for inversion taken from:
    // http://stackoverflow.com/questions/1148309/inverting-a-4x4-matrix
    public Matrix inverse()
    {
        float [] inv = new float[16];
        float det;
        
        int i;

    inv[0] = m[5] * m[10] * m[15] -
        m[5]  * m[11] * m[14] -
        m[9]  * m[6]  * m[15] +
        m[9]  * m[7]  * m[14] +
        m[13] * m[6]  * m[11] -
        m[13] * m[7]  * m[10];

    inv[4] = -m[4]  * m[10] * m[15] +
        m[4]  * m[11] * m[14] +
        m[8]  * m[6]  * m[15] -
        m[8]  * m[7]  * m[14] -
        m[12] * m[6]  * m[11] +
        m[12] * m[7]  * m[10];

    inv[8] = m[4]  * m[9] * m[15] -
        m[4]  * m[11] * m[13] -
        m[8]  * m[5] * m[15] +
        m[8]  * m[7] * m[13] +
        m[12] * m[5] * m[11] -
        m[12] * m[7] * m[9];

    inv[12] = -m[4]  * m[9] * m[14] +
        m[4]  * m[10] * m[13] +
        m[8]  * m[5] * m[14] -
        m[8]  * m[6] * m[13] -
        m[12] * m[5] * m[10] +
        m[12] * m[6] * m[9];

    inv[1] = -m[1]  * m[10] * m[15] +
        m[1]  * m[11] * m[14] +
        m[9]  * m[2] * m[15] -
        m[9]  * m[3] * m[14] -
        m[13] * m[2] * m[11] +
        m[13] * m[3] * m[10];

    inv[5] = m[0]  * m[10] * m[15] -
        m[0]  * m[11] * m[14] -
        m[8]  * m[2] * m[15] +
        m[8]  * m[3] * m[14] +
        m[12] * m[2] * m[11] -
        m[12] * m[3] * m[10];

    inv[9] = -m[0]  * m[9] * m[15] +
        m[0]  * m[11] * m[13] +
        m[8]  * m[1] * m[15] -
        m[8]  * m[3] * m[13] -
        m[12] * m[1] * m[11] +
        m[12] * m[3] * m[9];

    inv[13] = m[0]  * m[9] * m[14] -
        m[0]  * m[10] * m[13] -
        m[8]  * m[1] * m[14] +
        m[8]  * m[2] * m[13] +
        m[12] * m[1] * m[10] -
        m[12] * m[2] * m[9];

    inv[2] = m[1]  * m[6] * m[15] -
        m[1]  * m[7] * m[14] -
        m[5]  * m[2] * m[15] +
        m[5]  * m[3] * m[14] +
        m[13] * m[2] * m[7] -
        m[13] * m[3] * m[6];

    inv[6] = -m[0]  * m[6] * m[15] +
        m[0]  * m[7] * m[14] +
        m[4]  * m[2] * m[15] -
        m[4]  * m[3] * m[14] -
        m[12] * m[2] * m[7] +
        m[12] * m[3] * m[6];

    inv[10] = m[0]  * m[5] * m[15] -
        m[0]  * m[7] * m[13] -
        m[4]  * m[1] * m[15] +
        m[4]  * m[3] * m[13] +
        m[12] * m[1] * m[7] -
        m[12] * m[3] * m[5];

    inv[14] = -m[0]  * m[5] * m[14] +
        m[0]  * m[6] * m[13] +
        m[4]  * m[1] * m[14] -
        m[4]  * m[2] * m[13] -
        m[12] * m[1] * m[6] +
        m[12] * m[2] * m[5];

    inv[3] = -m[1] * m[6] * m[11] +
        m[1] * m[7] * m[10] +
        m[5] * m[2] * m[11] -
        m[5] * m[3] * m[10] -
        m[9] * m[2] * m[7] +
        m[9] * m[3] * m[6];

    inv[7] = m[0] * m[6] * m[11] -
        m[0] * m[7] * m[10] -
        m[4] * m[2] * m[11] +
        m[4] * m[3] * m[10] +
        m[8] * m[2] * m[7] -
        m[8] * m[3] * m[6];

    inv[11] = -m[0] * m[5] * m[11] +
        m[0] * m[7] * m[9] +
        m[4] * m[1] * m[11] -
        m[4] * m[3] * m[9] -
        m[8] * m[1] * m[7] +
        m[8] * m[3] * m[5];

    inv[15] = m[0] * m[5] * m[10] -
        m[0] * m[6] * m[9] -
        m[4] * m[1] * m[10] +
        m[4] * m[2] * m[9] +
        m[8] * m[1] * m[6] -
        m[8] * m[2] * m[5];

    det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];

    if (det == 0)
        return Matrix.identity();

    det = 1.f / det;

    Matrix res = new Matrix();
    for (i = 0; i < 16; i++)
        res.set(i, inv[i] * det);

    return res;

    }
    
    public static Matrix inverse(Matrix m)
    {
        return m.inverse();
    }
       
    public Matrix copy()
    {
        Matrix mat = new Matrix();
        for(int i = 0; i<m.length; i++)
            mat.set(i, get(i));
        return mat;
    }
    
    @Override
    public String toString()
    {     
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%.2f, %.2f, %.2f, %.2f \n", get(0, 0), get(0, 1), get(0, 2), get(0, 3)));
        builder.append(String.format("%.2f, %.2f, %.2f, %.2f \n", get(1, 0), get(1, 1), get(1, 2), get(1, 3)));
        builder.append(String.format("%.2f, %.2f, %.2f, %.2f \n", get(2, 0), get(2, 1), get(2, 2), get(2, 3)));
        builder.append(String.format("%.2f, %.2f, %.2f, %.2f \n", get(3, 0), get(3, 1), get(3, 2), get(3, 3)));
        return builder.toString();
    }
}
