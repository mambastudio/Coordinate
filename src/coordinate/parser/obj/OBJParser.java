/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser.obj;

import coordinate.generic.AbstractMesh;
import coordinate.generic.AbstractMesh.MeshType;
import static coordinate.generic.AbstractMesh.MeshType.FACE;
import static coordinate.generic.AbstractMesh.MeshType.FACE_NORMAL;
import static coordinate.generic.AbstractMesh.MeshType.FACE_UV;
import static coordinate.generic.AbstractMesh.MeshType.FACE_UV_NORMAL;
import coordinate.generic.AbstractParser;
import coordinate.generic.io.StringReader;
import coordinate.list.IntList;
import coordinate.parser.attribute.GroupT;
import coordinate.parser.attribute.MaterialT;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class OBJParser implements AbstractParser{
    private AbstractMesh mesh;       
    private ArrayList<MaterialT> groupMaterials;
    private ArrayList<GroupT> groups;
    
    private ArrayList<MaterialT> sceneMaterials;
    
    private int groupCount = -1;
    private int groupMaterialCount = -1;
    
    private boolean defaultMatPresent = true;
    
    public ArrayList<MaterialT> getGroupMaterialList()
    {
        return groupMaterials;
    }
    
    public ArrayList<MaterialT> getSceneMaterialList()
    {
        if(defaultMatPresent)
            return new ArrayList<>(Arrays.asList(new MaterialT()));
        else
            return groupMaterials;
    }
    
    public ArrayList<GroupT> getGroupList()
    {
        return groups;
    }
        
    @Override
    public void read(String uri, AbstractMesh data)
    {
        read(new File(uri).toURI(), data);
    }
    
    @Override
    public void read(URI uri, AbstractMesh data)
    {
        read(new StringReader(uri), data);
    }
    
    @Override
    public void readString(String string, AbstractMesh mesh) {
        InputStream is = new ByteArrayInputStream(string.getBytes());
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
        read(new StringReader(br), mesh);
    }
    
    public void readAttributes(String uri)
    {
        readAttributes(new File(uri).toURI());
    }
    
    public void readAttributes(URI uri)
    {
        readAttributes(new StringReader(uri));
    }
    
    public void readAttributes(StringReader parser)
    {
        int v = 0, vt = 0, vn = 0, f = 0, g = 0, o = 0, usemtl = 0;        
                        
        while(parser.hasNext())
        {
            String currentToken = parser.getNextToken();            
            switch (currentToken) {
                case "v":
                    v++;
                    break;                
                case "vt":
                    vt++;
                    break;
                case "vn":
                    vn++;
                    break;
                case "f":
                    f++;
                    break;
                case "g":
                    g++;
                    break;  
                case "o":
                    o++;
                    break;
                case "usemtl":
                    usemtl++;
                    break;
                default:                    
                    break;
            }                
        }       
        
        StringBuilder attributesString = new StringBuilder();
        attributesString.append("vertices              ").append(v).append("\n");
        attributesString.append("texture vertices      ").append(vt).append("\n");
        attributesString.append("normal vertices       ").append(vn).append("\n");
        attributesString.append("faces                 ").append(f).append("\n");
        attributesString.append("material              ").append(usemtl).append("\n");
        attributesString.append("groups                ").append(g).append("\n");
        attributesString.append("objects               ").append(o).append("\n");
        
        System.out.println(attributesString);
    }
    
    private void read(StringReader parser, AbstractMesh data)
    {
        this.mesh = data;   
        this.groupMaterials = new ArrayList<>();
        this.sceneMaterials = new ArrayList<>();
        this.groups = new ArrayList<>();
       
        while(parser.hasNext())
        {
            String peekToken = parser.peekNextToken();            
            switch (peekToken) {
                case "v":
                    readVertex(parser);
                    break;                
                case "vt":
                    readUV(parser);
                    break;
                case "vn":
                    readNormal(parser);
                    break;
                case "f":
                    readFaces(parser);
                    break;
                case "g":
                    readGroup(parser);
                    break;  
                case "o":
                    readObject(parser);
                    break;
                case "usemtl":
                    readMaterial(parser);
                    break;
                default:
                    parser.getNextToken();
                    break;
            }                
        }        
        
        if(groupMaterials.isEmpty())
            groupMaterials.add(new MaterialT());
        if(groups.isEmpty())
            groups.add(new GroupT());
        
        mesh.setMaterialList(groupMaterials);
        mesh.setGroupList(groups);        
    }
    private void readGroup(StringReader reader)
    {
        if(reader.getNextToken().equals("g"))
        {
            groups.add(new GroupT(reader.getNextToken(), groupCount));
            groupCount++;    
            
            if(defaultMatPresent)            
                groupMaterials.add(new MaterialT());               
            else
                groupMaterials.add(sceneMaterials.get(sceneMaterials.size()-1));
            groupMaterialCount++;
        }
    }
    
    private void readObject(StringReader reader)
    {
        if(reader.getNextToken().equals("o"))
        {
            groups.add(new GroupT(reader.getNextToken(), groupCount));
            groupCount++;
            if(defaultMatPresent)            
                groupMaterials.add(new MaterialT());               
            else
                groupMaterials.add(sceneMaterials.get(sceneMaterials.size()-1));
            groupMaterialCount++;
        }
    }
    
    private void readMaterial(StringReader reader)
    {
        if(reader.getNextToken().equals("usemtl"))
        {
            if(defaultMatPresent) defaultMatPresent = false;            
            sceneMaterials.add(new MaterialT(reader.getNextToken()));
            groupMaterialCount++;
        }
    }
    
    private void readVertex(StringReader reader) {
        if(reader.getNextToken().equals("v"))        
            mesh.addPoint(reader.getNextFloat(), reader.getNextFloat(), reader.getNextFloat());          
    }   
    
    private void readNormal(StringReader reader) {
        if(reader.getNextToken().equals("vn"))        
            mesh.addNormal(reader.getNextFloat(), reader.getNextFloat(), reader.getNextFloat());
    }
    
    private void readUV(StringReader reader) {
        if(reader.getNextToken().equals("vt"))        
            mesh.addTexCoord(reader.getNextFloat(), reader.getNextFloat());            
    }
    
    private int getMaterialCount()
    {
        if(groupMaterialCount<0) return 0;
        else return groupMaterialCount;
    }
    
    private void readFaces(StringReader reader) {
        IntList intArray = new IntList();
        boolean doubleBackSlash = reader.getLine().contains("//");
        
        reader.skipTokens(1); //parser.skip("f")
        
        while(reader.hasNext() && reader.peekNextTokenIsNumber())             
            intArray.add(reader.getNextInt());
        int[] array = intArray.trim();  
        
        //handle various type of face types
        if(array.length == 3)
        {                   
            modifyAdd(FACE, array[0], array[1], array[2]);       
        }
        else if(array.length == 4)
        {                  
            modifyAdd(FACE, array[0], array[1], array[2]); //p1 p2 p3
            modifyAdd(FACE, array[0], array[2], array[3]); //p1 p3 p4        
        }
        else if(array.length == 6 && doubleBackSlash)
        {                 
            modifyAdd(FACE_NORMAL, array[0], array[2], array[4],
                                          array[1], array[3], array[5]);
        }
        else if(array.length == 6)
        {                    
            modifyAdd(FACE_UV, array[0], array[2], array[4],
                                      array[1], array[3], array[5]);
        }       
        else if(array.length == 8 && doubleBackSlash)
        {                       
            modifyAdd(FACE_NORMAL, array[0], array[2], array[4],
                                          array[1], array[3], array[5]);
            modifyAdd(FACE_NORMAL, array[0], array[4], array[6],
                                          array[1], array[5], array[7]);     
        }
         else if(array.length == 8)
        {                                      
            modifyAdd(FACE_UV, array[0], array[2], array[4],
                                      array[1], array[3], array[5]);        
            modifyAdd(FACE_UV, array[0], array[4], array[6],
                                      array[1], array[5], array[7]); 
        }
        else if(array.length == 9)
        {             
            modifyAdd(FACE_UV_NORMAL, array[0], array[3], array[6],                             
                                             array[1], array[4], array[7],
                                             array[2], array[5], array[8]); 
        }
        else if(array.length == 12)
        {            
            modifyAdd(FACE_UV_NORMAL, array[0], array[3], array[6],                             
                                             array[1], array[4], array[7],
                                             array[2], array[5], array[8]);   

            modifyAdd(FACE_UV_NORMAL, array[0], array[6], array[9],                             
                                             array[1], array[7], array[10],
                                             array[2], array[8], array[11]); 
        }    
    }   
    
    //this modifies the read obj parser to friendly array read (also handles negative indices)
    private void modifyAdd(MeshType type, int... indices)
    {
        if(indices[0] >= 0)
            for(int i = 0; i<indices.length; i++)
                indices[i] = indices[i]-1;
        else
        {
            for(int i = 0; i<indices.length; i++)
                
                switch (i) {
                   
                    case 0:
                    case 1:
                    case 2:
                        indices[i] = indices[i] + mesh.pointSize();                         
                        break;
                    case 3:
                    case 4:
                    case 5:
                        indices[i] = indices[i] + mesh.texCoordsSize();
                        break;
                    case 6:
                    case 7:
                    case 8:
                        indices[i] = indices[i] + mesh.normalSize();
                        break;
                    default:
                        break;
                }
        }
        
        mesh.add(type, groupCount, getMaterialCount(), indices);
    }    
}
