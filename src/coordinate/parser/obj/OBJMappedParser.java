/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser.obj;

import coordinate.generic.g2.AbstractMesh;
import coordinate.generic.g2.AbstractMesh.MeshType;
import static coordinate.generic.g2.AbstractMesh.MeshType.FACE;
import static coordinate.generic.g2.AbstractMesh.MeshType.FACE_NORMAL;
import static coordinate.generic.g2.AbstractMesh.MeshType.FACE_UV;
import static coordinate.generic.g2.AbstractMesh.MeshType.FACE_UV_NORMAL;
import static coordinate.generic.g2.AbstractMesh.invalid;
import coordinate.generic.g2.AbstractParser;
import coordinate.generic.io.LineMappedReader;
import coordinate.parser.attribute.MaterialT;
import coordinate.parser.obj.OBJInfo;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.NONE;
import coordinate.utility.StringParser;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class OBJMappedParser implements AbstractParser {
    private AbstractMesh mesh;       
    private ArrayList<MaterialT> groupMaterials;
    
    private ArrayList<MaterialT> sceneMaterials;
    
    private int groupCount = -1;
    private int groupMaterialCount = -1;
    
    private boolean defaultMatPresent = true;
    
    private OBJInfo info;    
    private OBJInfo.SplitOBJPolicy splitPolicy = NONE;
    
    
    public ArrayList<MaterialT> getSceneMaterialList()
    {
        if(sceneMaterials.isEmpty())
            return new ArrayList<>(Arrays.asList(new MaterialT()));
        else
            return sceneMaterials;
    }
    
    public OBJInfo getInfo()
    {
        return info;
    }
    
    @Override
    public void read(String uri, AbstractMesh data)
    {
        read(new File(uri).toURI(), data);
    }
    
    @Override
    public void read(URI uri, AbstractMesh data)
    {        
        read(new LineMappedReader(uri),data);
    }
    
    @Override
    public void readString(String string, AbstractMesh mesh) {        
        throw new UnsupportedOperationException("strings as source file is not allowed");
    }
    
    public void readAttributes(String uri)
    {
        readAttributes(new File(uri).toURI());
    }
    
    public void readAttributes(URI uri)
    {
         info = new OBJInfo(uri);
         info.read();  
    }
    
    private void read(LineMappedReader reader, AbstractMesh data)
    {        
        this.mesh = data;   
        this.groupMaterials = new ArrayList<>();
        this.sceneMaterials = new ArrayList<>();
        
        while(true)
        {
            String line = reader.readLineString3();            
            if(line == null)
                break;
            
            StringParser parser = new StringParser(line);
            parser.skipContinousSpace(); //what if the line has leading space
            
            if(parser.currentIs("v "))
                readVertex(parser);
            else if(parser.currentIs("vn "))
                readNormal(parser);
            else if(parser.currentIs("vt "))
                readUV(parser);
            else if(parser.currentIs("g "))
                switch (splitPolicy) {
                    case GROUP:
                        readGroup(parser);
                        break;
                    case NONE:
                        readGroup(parser);
                        break;
                    default:
                        break;
                }
            else if(parser.currentIs("o "))
                switch (splitPolicy) {
                    case OBJECT:
                        readObject(parser);
                        break;
                    case NONE:
                        readObject(parser);
                        break;
                    default:                       
                        break;
                }                   
            else if(parser.currentIs("usemtl "))
                readMaterial(parser);
            else if(parser.currentIs("f "))
                readFaces(parser);
        }
    }
    
    private void readVertex(StringParser parser)
    {
        float x, y, z;
        x = parser.getFirstFloat();
        y = parser.getFirstFloat();
        z = parser.getFirstFloat();
        mesh.addPoint(x, y, z);
    }
    
    private void readUV(StringParser parser)
    {
        float x, y;
        x = parser.getFirstFloat();
        y = parser.getFirstFloat();
        mesh.addTexCoord(x, y);
    }
    
    private void readNormal(StringParser parser)
    {
        float x, y, z;
        x = parser.getFirstFloat();
        y = parser.getFirstFloat();
        z = parser.getFirstFloat();
        mesh.addNormal(x, y, z);
    }
    
    private void readGroup(StringParser parser)
    {
        groupCount++;
        parser.incrementPointer(2);
        if(defaultMatPresent)      
            if(parser.isCurrentNumber())
                groupMaterials.add(new MaterialT());  
            else
                groupMaterials.add(new MaterialT(parser.getToken()));
        else
            groupMaterials.add(sceneMaterials.get(sceneMaterials.size()-1));
        groupMaterialCount++;      
    }
    
    private void readObject(StringParser parser)
    {        
        groupCount++;
        parser.incrementPointer(2);
        if(defaultMatPresent)      
            if(parser.isCurrentNumber())
                groupMaterials.add(new MaterialT());  
            else
                groupMaterials.add(new MaterialT(parser.getToken()));
        else
            groupMaterials.add(sceneMaterials.get(sceneMaterials.size()-1));
        groupMaterialCount++;        
    }
    
    private void readMaterial(StringParser parser)
    {
        if(defaultMatPresent) defaultMatPresent = false;        
        sceneMaterials.add(new MaterialT(parser.getToken()));
        groupMaterials.add(sceneMaterials.get(sceneMaterials.size()-1));
        groupMaterialCount++;
    }
    
    private void readFaces(StringParser parser)
    {
        ArrayList<IndexT> array = new ArrayList(8);
        
        while (!parser.isNewLine()) {
            IndexT vi = parseRawTriple(parser);
            parser.skipContinousSpace();
            array.add(vi);
        }    
        
        //handle various type of face types
        if(array.size() == 3)                        
            modifyAdd(array.get(0).getMeshType(), 
                    array.get(0).vertex_index, array.get(1).vertex_index, array.get(2).vertex_index,
                    array.get(0).texcoord_index, array.get(1).texcoord_index, array.get(2).texcoord_index,
                    array.get(0).normal_index, array.get(1).normal_index, array.get(2).normal_index);     
        if(array.size() == 4)   
        {
            modifyAdd(array.get(0).getMeshType(), 
                    array.get(0).vertex_index, array.get(1).vertex_index, array.get(2).vertex_index,           //p1 p2 p3
                    array.get(0).texcoord_index, array.get(1).texcoord_index, array.get(2).texcoord_index,
                    array.get(0).normal_index, array.get(1).normal_index, array.get(2).normal_index); 
            modifyAdd(array.get(0).getMeshType(), 
                    array.get(0).vertex_index, array.get(2).vertex_index, array.get(3).vertex_index,           //p1 p3 p4        
                    array.get(0).texcoord_index, array.get(2).texcoord_index, array.get(3).texcoord_index,
                    array.get(0).normal_index, array.get(2).normal_index, array.get(3).normal_index); 
        }
    }
    
    //this modifies the read obj parser to friendly array read (also handles negative indices)
    private void modifyAdd(MeshType type, int... indices)
    {
        mesh.add(type, groupCount, getMaterialCount(), indices);
    }    
    
    public void setSplitPolicy(OBJInfo.SplitOBJPolicy splitPolicy)
    {
        this.splitPolicy = splitPolicy;
    }
    
    private int getMaterialCount()
    {
        if(groupMaterialCount<0) return 0;
        else return groupMaterialCount;
    }
    
    private IndexT parseRawTriple(StringParser parser) {               
        IndexT vi = new IndexT();  // 0x80000000 = -2147483648 = invalid
        
        vi.vertex_index = parser.getFirstInt();
        parser.skipIfNotSpaceAndChar('/');
               
        //read first individual /, and if not present then it is vertex index only, and return
        if (parser.isNotChar('/')) return vi;
        parser.incrementPointer();
                
        // i//k
        if (parser.isChar('/')) {            
            vi.normal_index = parser.getFirstInt();
            parser.skipIfNotSpaceAndChar('/');
            return vi;
        }
                
        // i/j/k or i/j
        vi.texcoord_index = parser.getFirstInt();
        parser.skipIfNotSpaceAndChar('/');
        if (parser.isNotChar('/')) 
            return vi;
        
        // i/j/k
        parser.incrementPointer();  // skip '/'
        vi.normal_index = parser.getFirstInt();
        parser.skipIfNotSpaceAndChar('/');
  
        return vi;
    }
   
    
    private static class IndexT
    {
        public int vertex_index, texcoord_index, normal_index;
        
        public IndexT()
        {
            vertex_index = texcoord_index = normal_index = invalid;
        }
        
        public MeshType getMeshType()
        {
            if(vertex_index     != invalid && 
               texcoord_index   == invalid && 
               normal_index     == invalid)
                return FACE;
            else if(vertex_index    != invalid&& 
                    texcoord_index  != invalid&& 
                    normal_index    == invalid)
                return FACE_UV;
            else if(vertex_index    != invalid&& 
                    texcoord_index  == invalid&& 
                    normal_index    != invalid)
                return FACE_NORMAL;
            else if(vertex_index    != invalid&& 
                    texcoord_index  != invalid&& 
                    normal_index    != invalid)
                return FACE_UV_NORMAL;
            else
                throw new UnsupportedOperationException("mesh type not recognised");
        }
        
        @Override
        public String toString()
        {
            return String.format("(%5d, %5d, %5d)", vertex_index, texcoord_index, normal_index);
        }
    }
}
