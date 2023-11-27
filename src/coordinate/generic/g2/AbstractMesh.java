/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.g2;

import coordinate.generic.AbstractRay;
import static coordinate.generic.g2.AbstractMesh.MeshMemoryType.HEAP;
import static coordinate.generic.g2.AbstractMesh.MeshMemoryType.NATIVE;
import coordinate.memory.type.MemoryStruct;
import coordinate.parser.attribute.GroupT;
import coordinate.parser.attribute.MaterialT;
import coordinate.shapes.AlignedBBoxShape;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author user
 * @param <S>
 * @param <V>
 * @param <T>
 * @param <R>
 * @param <B>
 * @param <TriangleIndex>
 * @param <TriShape>
 */
public abstract class AbstractMesh<
        S extends AbstractSCoordStruct<S, V, S>, 
        V extends AbstractVCoordStruct<S, V, V>, 
        T extends AbstractCoordFloatStruct<T>, 
        R extends AbstractRay<S, V, R>,
        B extends AlignedBBoxShape<S, V, R, B>,
        TriangleIndex extends AbstractTriangleIndex,
        TriShape extends AbstractTriangleStruct<S, V, R, B, TriShape>> {
    
    public enum MeshType{FACE, FACE_NORMAL, FACE_UV_NORMAL, FACE_UV};
    public enum MeshMemoryType{HEAP, NATIVE};
    
    protected final MeshMemoryType memoryType;
    
    protected MemoryStruct<TriangleIndex> triangleFaces;
    
    protected MemoryStruct<S> points = null;
    protected MemoryStruct<V> normals = null;
    protected MemoryStruct<T> texcoords = null;
    
    protected ArrayList<MaterialT> materials;
    protected ArrayList<GroupT> groups;
    
    protected final AtomicLong facesCount;
    protected final AtomicLong pointsCount;
    protected final AtomicLong normalsCount;
    protected final AtomicLong texcoordsCount;
    
    protected AbstractMesh(MeshMemoryType memoryType)
    {
        this.memoryType = memoryType;  
        
        this.facesCount = new AtomicLong();
        this.pointsCount = new AtomicLong();
        this.normalsCount = new AtomicLong();
        this.texcoordsCount = new AtomicLong();
    }
    
    public void setMaterialList(ArrayList<MaterialT> materialList)
    {
        this.materials = materialList;
    }
        
    public ArrayList<MaterialT> getMaterialList()
    {
        return materials;
    }
    
    public void initCoordList(S s, T t, V n, TriangleIndex tFaces,
            long sizeP, long sizeT, long sizeN, long sizeF)
    {
        if(memoryType == HEAP)
        {
            points = MemoryStruct.allocateHeap(s, sizeP);
            normals = MemoryStruct.allocateHeap(n, sizeN);
            texcoords = MemoryStruct.allocateHeap(t, sizeT);
            triangleFaces = MemoryStruct.allocateHeap(tFaces, sizeF);
        }
        else if(memoryType == NATIVE)
        {
            points = MemoryStruct.allocateNative(s, sizeP, false);
            normals = MemoryStruct.allocateNative(n, sizeN, false);
            texcoords = MemoryStruct.allocateNative(t, sizeT, false);
            triangleFaces = MemoryStruct.allocateNative(tFaces, sizeF, false);
        }        
    }
        
    public abstract void addPoint(S p);
    public abstract void addPoint(float... values);
    public abstract void addNormal(V n);
    public abstract void addNormal(float... values);
    public abstract void addTexCoord(T t);
    public abstract void addTexCoord(float... values);
    public abstract TriShape getTriangle(int index);    
    public abstract B getBounds();
    public abstract void addTriangle(int vert1, int vert2, int vert3, int uv1, int uv2, int uv3, int norm1, int norm2, int norm3, int data);
   
        
    //Mesh face handling section        
    public void add(MeshType type, int group, int material, int... values)
    {
        int data = 0;
        data = data | material;
        data = data | (group << 16);       
                
        
        switch(type)
        {
            case FACE:
            {
                addTriangle(values[0], values[1], values[2], -1, -1, -1, -1, -1, -1, data);
                break;
            }
            case FACE_UV_NORMAL:
            {
                addTriangle(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], data);
                break;
            }
            case FACE_UV:
            {
                addTriangle(values[0], values[1], values[2], values[3], values[4], values[5], -1, -1, -1, data);
                break;
            }
            case FACE_NORMAL:
            {
                addTriangle(values[0], values[1], values[2], -1, -1, -1, values[3], values[4], values[5], data);
                break;
            }
        }
    }
    
    public void printGroupAndMaterial(int data)
    {        
        System.out.println("Group " +((data >> 16) & 0xFFFF)+ " Material " + (data & 0xFFFF));        
    }
    
    public long pointSize(){
        return points.size();
    }
    
    public long texCoordsSize() {
        return texcoords.size();
    }
    
    public long normalSize() {
        return normals.size();
    }
            
    public S getVertex1(int primID)
    {
        return points.get(triangleFaces.get(primID).v_123.get('x'));
    }
    
    public S getVertex2(int primID)
    {
        return points.get(triangleFaces.get(primID).v_123.get('y'));
    }

    public S getVertex3(int primID)
    {        
        return points.get(triangleFaces.get(primID).v_123.get('z'));
    }
        
    public T getTexcoord1(int primID)
    {
        return texcoords.get(triangleFaces.get(primID).u_123m.get('x'));
    }

    public T getTexcoord2(int primID)
    {
        return texcoords.get(triangleFaces.get(primID).u_123m.get('y'));
    }

    public T getTexcoord3(int primID)
    {
        return texcoords.get(triangleFaces.get(primID).u_123m.get('z'));
    }
    
    public V getNormal1(int primID)
    {
        return normals.get(triangleFaces.get(primID).n_123.get('x'));
    }

    public V getNormal2(int primID)
    {
        return normals.get(triangleFaces.get(primID).n_123.get('y'));
    }

    public V getNormal3(int primID)
    {
        return normals.get(triangleFaces.get(primID).n_123.get('z'));
    }
    
    public boolean hasNormal(int primID)
    {
        return triangleFaces.get(primID).n_123.get('x') != -1;
    }
    
    public boolean hasUV(int primID)
    {
        return triangleFaces.get(primID).u_123m.get('x') != -1;
    }
    
    public long triangleSize()
    {
        return triangleFaces.size();
    }
            
    public String getInfo(int index)
    {
        return triangleFaces.get(index).toString();
    }
    
    
}
