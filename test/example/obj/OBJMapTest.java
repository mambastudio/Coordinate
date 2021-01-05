/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example.obj;

import coordinate.generic.io.LineMappedReader;
import coordinate.parser.obj.OBJInfo;
import coordinate.parser.obj.OBJMappedParser;
import java.io.File;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class OBJMapTest {
    public static void main(String... args)
    {
        String file = "C:\\Users\\user\\Documents\\3D Scenes\\CornellBox\\CornellBox-Empty-CO.obj";
        //String file = "C:\\Users\\user\\Documents\\3D Scenes\\hair\\hair.obj";
        //String file = "C:\\Users\\user\\Documents\\3D Scenes\\San_Miguel\\san-miguel.obj";
        //String file = "C:\\Users\\user\\Desktop\\violin_case.obj";
        //String file = "C:\\Users\\user\\Documents\\GitHub\\RayTracing\\SimpleMesh.obj";
        
        //OBJInfo info = new OBJInfo(file);        
        //info.read2();
        //System.out.println(info);
        
        
        SimpleMesh mesh = new SimpleMesh();
        OBJMappedParser parser = new OBJMappedParser();
        parser.read(file, mesh);
        /*
        LineMappedReader parser = new LineMappedReader(new File(file).toURI());
                
        parser.goToStartChar();
        while(parser.hasRemaining())
        {            
            if(parser.isCurrent("o "))
            {               
                float[] array = parser.readLineFloatArray();
                
                System.out.println(parser.readLineString());
                
            }
            parser.goToNextDefinedLine();
        }*/
    }
}
