/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser;

import coordinate.generic.AbstractMesh;
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
        //if(parser.getNextToken().equals("vn"))        
            //data.add(new Normal3f(parser.getNextFloat(), parser.getNextFloat(), parser.getNextFloat()));
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
        
        if(intArray.size() == 3)
        {
            int[] array = intArray.trim();            
            mesh.add(FACE, array[0]-1, array[1]-1, array[2]-1);       
        }
        else if(intArray.size() == 4)
        {
            int[] array = intArray.trim();        
            mesh.add(FACE, array[0]-1, array[1]-1, array[2]-1); //p1 p2 p3
            mesh.add(FACE, array[0]-1, array[2]-1, array[3]-1); //p1 p3 p4        
        }
        else if(intArray.size() == 6 && doubleBackSlash)
        {
            int[] array = intArray.trim();        
            mesh.add(FACE_NORMAL, array[0]-1, array[2]-1, array[4]-1,
                                          array[1]-1, array[3]-1, array[5]-1);
        }
        else if(intArray.size() == 6)
        {
            int[] array = intArray.trim();        
            mesh.add(FACE_UV, array[0]-1, array[2]-1, array[4]-1,
                                      array[1]-1, array[3]-1, array[5]-1);
        }       
        else if(intArray.size() == 8 && doubleBackSlash)
        {           
            int[] array = intArray.trim();        
            mesh.add(FACE_NORMAL, array[0]-1, array[2]-1, array[4]-1,
                                          array[1]-1, array[3]-1, array[5]-1);
            mesh.add(FACE_NORMAL, array[0]-1, array[4]-1, array[6]-1,
                                          array[1]-1, array[5]-1, array[7]-1);     
        }
         else if(intArray.size() == 8)
        {                        
            int[] array = intArray.trim();        
            mesh.add(FACE_UV, array[0]-1, array[2]-1, array[4]-1,
                                      array[1]-1, array[3]-1, array[5]-1);        
            mesh.add(FACE_UV, array[0]-1, array[4]-1, array[6]-1,
                                      array[1]-1, array[5]-1, array[7]-1); 
        }
        else if(intArray.size() == 9)
        {
            int[] array = intArray.trim();        
            mesh.add(FACE_UV_NORMAL, array[0]-1, array[3]-1, array[6]-1,                             
                                             array[1]-1, array[4]-1, array[7]-1,
                                             array[2]-1, array[5]-1, array[8]-1); 
        }
        else if(intArray.size() == 12)
        {
            int[] array = intArray.trim();        
            mesh.add(FACE_UV_NORMAL, array[0]-1, array[3]-1, array[6]-1,                             
                                             array[1]-1, array[4]-1, array[7]-1,
                                             array[2]-1, array[5]-1, array[8]-1);   

            mesh.add(FACE_UV_NORMAL, array[0]-1, array[6]-1, array[9]-1,                             
                                             array[1]-1, array[7]-1, array[10]-1,
                                             array[2]-1, array[8]-1, array[11]-1); 
        }    
    }    
}
