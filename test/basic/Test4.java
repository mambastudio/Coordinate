/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.parser.obj.OBJInfo;

/**
 *
 * @author user
 */
public class Test4 {
    public static void main(String... args)
    {
        //String file = "C:\\Users\\user\\Documents\\3D Scenes\\CornellBox\\CornellBox-Empty-CO.obj";
        String file = "C:\\Users\\user\\Documents\\3D Scenes\\hair\\hair.obj";
        //String file = "C:\\Users\\user\\Documents\\3D Scenes\\San_Miguel\\san-miguel.obj";
        //String file = "C:\\Users\\user\\Desktop\\violin_case_2.obj";
        
        OBJInfo objInfo = new OBJInfo(file);
        System.out.println(objInfo.readAndClose());
        
    }
}
