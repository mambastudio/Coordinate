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
         //C:\\Users\\user\\Documents\\Scene3d\\roadBike\\roadBike.obj
        OBJInfo objInfo = new OBJInfo("C:\\Users\\user\\Documents\\Scene3d\\roadBike\\roadBike.obj");
        System.out.println(objInfo.readAndClose());
        
    }
}
