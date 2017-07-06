/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

import coordinate.generic.VCoord;
import coordinate.list.CoordinateList;
import coordinate.list.IntList;


/**
 *
 * @author user
 * @param <P>
 * @param <N>
 * @param <T>
 */
public abstract class AbstractMesh <P extends SCoord, N extends VCoord, T extends AbstractCoordinate>
{    
    public enum MeshType{FACE, FACE_NORMAL, FACE_UV_NORMAL, FACE_UV};
    
    protected IntList triangleFaces;
    
    protected CoordinateList<P> points = null;
    protected CoordinateList<N> normals = null;
    protected CoordinateList<T> texcoords = null;
    
    public abstract void addPoint(P p);
    public abstract void addPoint(float... values);
    public abstract void addNormal(N n);
    public abstract void addNormal(float... values);
    public abstract void addTexCoord(T t);
    public abstract void addTexCoord(float... values);
    
    public int[] getTriangleFacesArray()
    {
        return triangleFaces.trim();
    }
    
    public int pointSize()
    {
        return points.size();
    }
    
    public int pointArraySize()
    {
        return points.arraySize();
    }
        
    public P getPoint(int index)
    {
        return points.get(index);
    }
    
    public float[] getPointArray()
    {
        return points.getFloatArray();
    }
        
    public int normalSize()
    {
        return normals.size();
    }
    
    public int normalArraySize()
    {
        return normals.arraySize();
    }
        
    public N getNormal(int index)
    {
        return normals.get(index);
    }
    
    public float[] getNormalArray()
    {
        return normals.getFloatArray();
    }
    
    public int texCoordsSize()
    {
        return texcoords.size();
    }
    
    public int texCoordsArraySize()
    {
        return texcoords.arraySize();
    }
        
    public T getTexCoords(int index)
    {
        return texcoords.get(index);
    }
    
    public float[] getTexCoordsArray()
    {
        return texcoords.getFloatArray();
    }
    
    //Mesh face handling section        
    public void add(MeshType type, int... values)
    {
        switch(type)
        {
            case FACE:
            {
                addTriangle(values[0], values[1], values[2], -1, -1, -1, -1, -1, -1, -1);
                break;
            }
            case FACE_UV_NORMAL:
            {
                addTriangle(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], -1);
                break;
            }
            case FACE_UV:
            {
                addTriangle(values[0], values[1], values[2], values[3], values[4], values[5], -1, -1, -1, -1);
                break;
            }
            case FACE_NORMAL:
            {
                addTriangle(values[0], values[1], values[2], -1, -1, -1, values[3], values[4], values[5], -1);
                break;
            }
        }
    }
    
    private void addTriangle(int vert1, int vert2, int vert3, int uv1, int uv2, int uv3, int norm1, int norm2, int norm3, int material)
    {
        triangleFaces.add(vert1, vert2, vert3,  uv1, uv2, uv3, norm1, norm2, norm3, material);
    }
    
    private int getArrayIndexFromPrimID(int index)
    {
        return index*10;
    }
    
    public P getVertex1(int primID)
    {
        return points.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 0));
    }
    
    public P getVertex2(int primID)
    {
        return points.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 1));
    }

    public P getVertex3(int primID)
    {
        return points.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 2));
    }
        
    public T getTexcoord1(int primID)
    {
        return texcoords.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 3));
    }

    public T getTexcoord2(int primID)
    {
        return texcoords.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 4));
    }

    public T getTexcoord3(int primID)
    {
        return texcoords.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 5));
    }
    
    public N getNormal1(int primID)
    {
        return normals.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 6));
    }

    public N getNormal2(int primID)
    {
        return normals.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 7));
    }

    public N getNormal3(int primID)
    {
        return normals.get(triangleFaces.get(getArrayIndexFromPrimID(primID) + 8));
    }
    
    public int triangleSize()
    {
        return triangleFaces.size()/10;
    }
    
    public int triangleArraySize()
    {
        return triangleFaces.size();
    }
    
    public int[] triangleArray()
    {
        return triangleFaces.trim();
    }
    
    public String getInfo(int index)
    {
        return String.format("v: %7.0f, %7.0f, %7.0f, u: %7.0f, %7.0f, %7.0f, n: %7.0f, %7.0f, %7.0f, m: %7.0f", 
                (float)triangleFaces.get(index * 10 + 0), (float)triangleFaces.get(index * 10 + 1), (float)triangleFaces.get(index * 10 + 2),
                (float)triangleFaces.get(index * 10 + 3), (float)triangleFaces.get(index * 10 + 4), (float)triangleFaces.get(index * 10 + 5),
                (float)triangleFaces.get(index * 10 + 6), (float)triangleFaces.get(index * 10 + 7), (float)triangleFaces.get(index * 10 + 8),
                (float)triangleFaces.get(index * 10 + 9));
    }
    
    
}