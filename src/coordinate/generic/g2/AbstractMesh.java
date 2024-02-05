/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.g2;

import coordinate.generic.AbstractRay;
import coordinate.memory.type.MemoryStructFactory.Int32;
import coordinate.memory.type.StructCache;
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
 * @param <PointsStruct>
 * @param <TexCoordsStruct>
 * @param <NormalssStruct>
 * @param <FacesStruct>
 * @param <SizeStruct>
 */
public abstract class AbstractMesh<
        S extends AbstractSCoordStruct<S, V, S>, 
        T extends AbstractCoordFloatStruct<T>, 
        V extends AbstractVCoordStruct<S, V, V>,        
        R extends AbstractRay<S, V, R>,
        B extends AlignedBBoxShape<S, V, R, B>,
        TriangleIndex extends AbstractTriangleIndex,
        TriShape extends AbstractTriangleStruct<S, V, R, B, TriShape>,
        
        PointsStruct extends StructCache<S, ?>,
        TexCoordsStruct extends StructCache<T, ?>,
        NormalssStruct extends StructCache<V, ?>,
        FacesStruct extends StructCache<TriangleIndex, ?>,
        SizeStruct extends StructCache<Int32, ?>> {
    
    public enum MeshType{FACE, FACE_NORMAL, FACE_UV_NORMAL, FACE_UV};
    public enum MeshMemoryType{HEAP, NATIVE};
    
    public static int invalid = 0x80000000; //0x80000000 = -2147483648 = invalid
    
    protected final MeshMemoryType memoryType;    
    
    //what the mesh entail (for gpu)
    protected PointsStruct points = null;
    protected TexCoordsStruct texcoords = null;
    protected NormalssStruct normals = null;    
    protected FacesStruct triangleFaces = null;
    protected SizeStruct size = null;
        
    protected ArrayList<MaterialT> materials;
    protected ArrayList<GroupT> groups;
    
    protected final AtomicLong facesCount;
    protected final AtomicLong pointsCount;
    protected final AtomicLong normalsCount;
    protected final AtomicLong texcoordsCount;
    
    private boolean isFree = false;
        
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
            PointsStruct points, TexCoordsStruct texcoords, 
            NormalssStruct normals, FacesStruct triangleFaces,
            SizeStruct size)           
    {
        this.points = points;
        this.normals = normals;
        this.texcoords = texcoords;
        this.triangleFaces = triangleFaces;
        this.size = size;
    }
    
    public PointsStruct getPoints()
    {
        return points;
    }
    
    public TexCoordsStruct getTexCoords()
    {
        return texcoords;
    }
    
    public NormalssStruct getNormals()
    {
        return normals;
    }
    
    public FacesStruct getFaces()
    {
        return triangleFaces;
    }
    
    public SizeStruct getMeshSize()
    {
        return size;
    }
        
    public abstract void addPoint(S p);
    public abstract void addPoint(float... values);
    public abstract void addNormal(V n);
    public abstract void addNormal(float... values);
    public abstract void addTexCoord(T t);
    public abstract void addTexCoord(float... values);
    public abstract TriShape getTriangle(int index);    
    public abstract B getBounds();
    public abstract B getBound(int primID);
    public abstract void addTriangle(int vert1, int vert2, int vert3, int uv1, int uv2, int uv3, int norm1, int norm2, int norm3, int data);
   
    //Mesh face handling section        
    public void add(MeshType type, int group, int material, int... values)
    {
        int data = 0;
        data = data | material;
        data = data | (group << 16);       
                
        //for handling negative values or otherwise transform to index zero-base
        values[0] = fixIndex(values[0], pointsCount.intValue());
        values[1] = fixIndex(values[1], pointsCount.intValue());
        values[2] = fixIndex(values[2], pointsCount.intValue());
        values[3] = fixIndex(values[3], texcoordsCount.intValue());
        values[4] = fixIndex(values[4], texcoordsCount.intValue());
        values[5] = fixIndex(values[5], texcoordsCount.intValue());
        values[6] = fixIndex(values[6], normalsCount.intValue());
        values[7] = fixIndex(values[7], normalsCount.intValue());
        values[8] = fixIndex(values[8], normalsCount.intValue());
        
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
                addTriangle(values[0], values[1], values[2], -1, -1, -1, values[6], values[7], values[8], data);
                break;
            }
        }
    }
    
    // Make index zero-base, and also support relative index.
    private int fixIndex(int idx, int n) {
        if(idx == 0x80000000) return idx;
        if (idx > 0) return idx - 1;
        if (idx == 0) return 0;
        return n + idx;  // negative value = relative
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
    
    public boolean isFree()
    {
        return isFree;
    }
    
    public void free()
    {
        points.free();
        texcoords.free();
        normals.free();    
        triangleFaces.free();
        size.free();
        
        isFree = true;
    }
}
