/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser;

import coordinate.generic.AbstractMesh;
import coordinate.generic.AbstractMesh.MeshType;
import static coordinate.generic.AbstractMesh.MeshType.FACE;
import static coordinate.generic.AbstractMesh.MeshType.FACE_NORMAL;
import static coordinate.generic.AbstractMesh.MeshType.FACE_UV;
import static coordinate.generic.AbstractMesh.MeshType.FACE_UV_NORMAL;
import coordinate.generic.AbstractParser;
import coordinate.generic.StringReader;
import coordinate.list.IntList;
import java.io.File;
import java.net.URI;

/**
 *
 * @author user
 */
public class OBJParser implements AbstractParser{
    private AbstractMesh mesh;
        
    @Override
    public void read(String uri, AbstractMesh data)
    {
        read(new File(uri).toURI(), data);
    }
    
    @Override
    public void read(URI uri, AbstractMesh data)
    {
        this.mesh = data;
                
        StringReader parser = new StringReader(uri);
        
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
                default:
                    parser.getNextToken();
                    break;
            }                
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
        
        mesh.add(type, indices);
    }    
}
