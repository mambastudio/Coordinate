/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser.obj;

import coordinate.generic.AbstractMesh;
import static coordinate.generic.AbstractMesh.MeshType.FACE;
import static coordinate.generic.AbstractMesh.MeshType.FACE_NORMAL;
import static coordinate.generic.AbstractMesh.MeshType.FACE_UV;
import static coordinate.generic.AbstractMesh.MeshType.FACE_UV_NORMAL;
import coordinate.generic.AbstractParser;
import coordinate.generic.io.StringMappedReader;
import coordinate.parser.attribute.GroupT;
import coordinate.parser.attribute.MaterialT;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.GROUP;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.OBJECT;
import static coordinate.parser.obj.OBJInfo.SplitOBJPolicy.USEMTL;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class OBJMappedParser implements AbstractParser{
    
    
    private AbstractMesh mesh;       
    private ArrayList<MaterialT> groupMaterials;
    private ArrayList<GroupT> groups;
    
    private ArrayList<MaterialT> sceneMaterials;
    
    private int groupCount = -1;
    private int groupMaterialCount = -1;
    
    private OBJInfo info;
    
    public ArrayList<MaterialT> getGroupMaterialList()
    {
        return groupMaterials;
    }
    
    public ArrayList<MaterialT> getSceneMaterialList()
    {
        if(sceneMaterials.isEmpty())
            return new ArrayList<>(Arrays.asList(new MaterialT()));
        else
            return sceneMaterials;
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
        readAttributes(uri);
        read(data);
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
    
    
    private void read(AbstractMesh data)
    {        
        this.mesh = data;   
        this.groupMaterials = new ArrayList<>();
        this.sceneMaterials = new ArrayList<>();
        this.groups = new ArrayList<>();
        
        StringMappedReader parser = info.getStringMappedReader();
       
        while(parser.hasRemainingToken())
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
    private void readGroup(StringMappedReader reader)
    {   
        if(reader.getNextToken().equals("g") == (info.splitPolicy() == GROUP))
        {
            groups.add(new GroupT(reader.getNextToken(), groupCount));
            groupCount++;                

            if(info.usemtl() == 0)  
            {
                groupMaterials.add(new MaterialT());
                groupMaterialCount++;
            } 
            else
            {
                groupMaterials.add(sceneMaterials.get(sceneMaterials.size() - 1));
                groupMaterialCount++;
            }
        }
        else
            reader.skipToken(0);        
    }
    
    private void readObject(StringMappedReader reader)
    {
        
        if(reader.getNextToken().equals("o") == (info.splitPolicy() == OBJECT))
        {                        
            groups.add(new GroupT(reader.getNextToken(), groupCount)); //get object name
            groupCount++;

            if(info.usemtl() == 0)  
            {
                groupMaterials.add(new MaterialT());
                groupMaterialCount++;
            }             
            else
            {
                groupMaterials.add(sceneMaterials.get(sceneMaterials.size() - 1));
                groupMaterialCount++;
            }
        }
        else
            reader.skipToken(0);     
    }
    
    private void readMaterial(StringMappedReader reader)
    {            
        if(reader.getNextToken().equals("usemtl"))       
        {               
            String mtl = reader.getNextToken();
            MaterialT mat = new MaterialT(mtl);

            sceneMaterials.add(new MaterialT(mat));

            if(info.splitPolicy() == USEMTL)
            {
                groups.add(new GroupT(mtl, groupCount)); //get object name
                groupCount++;

                groupMaterials.add(sceneMaterials.get(sceneMaterials.size() - 1));
                groupMaterialCount++;
            }
        }         
    }
    
    private void readVertex(StringMappedReader reader) {
        if(reader.getNextToken().equals("v"))        
        {
            float x = reader.getNextFloat();
            float y = reader.getNextFloat();
            float z = reader.getNextFloat();
                        
            mesh.addPoint(x, y, z);
        }          
    }   
    
    private void readNormal(StringMappedReader reader) {
        if(reader.getNextToken().equals("vn"))        
        {
            float x = reader.getNextFloat();
            float y = reader.getNextFloat();
            float z = reader.getNextFloat();
                  
            mesh.addNormal(x, y, z);
        }
    }
    
    private void readUV(StringMappedReader reader) {
        if(reader.getNextToken().equals("vt"))   
        {           
            float x = reader.getNextFloat();
            float y = reader.getNextFloat();
            
            mesh.addTexCoord(x, y);
        }
    }
    
    private int getMaterialCount()
    {
        if(groupMaterialCount<0) return 0;
        else return groupMaterialCount;
    }
    
    private int[] getIntArray(String str)
    {    
        str = str.replaceAll("[^-?0-9]+", " ");
        String[] s = str.trim().split(" ");
        
        int[] array = new int[s.length];
        for(int i = 0; i<array.length; i++)
            array[i] = Integer.parseInt(s[i]);
        return array;
    }
    
    private void readFaces(StringMappedReader reader) {
         
        String face = reader.getNextLine();        
        boolean doubleBackSlash = face.contains("//");
               
        int[] array = getIntArray(face);  
         
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
    private void modifyAdd(AbstractMesh.MeshType type, int... indices)
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
    
    @Override
    public String toString()
    {
        return info.toString();
    }
}
