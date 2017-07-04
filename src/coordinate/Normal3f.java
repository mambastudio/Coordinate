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
package coordinate;

/**
 *
 * @author user
 */
public final class Normal3f extends Vector3f
{ 
    public Normal3f() {
        
    }

    public Normal3f(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;       
    }

    public Normal3f(Vector3f v) {
        this(v.x, v.y, v.z);
    }
    
    @Override
    public Normal3f normalize()
    {
        return (Normal3f) super.normalize();
    }
    
    @Override
    public Normal3f getCoordInstance() {
        return new Normal3f();
    }

    @Override
    public Normal3f copy() {
        return new Normal3f(x, y, z);
    }
}
