/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.parser.obj.OBJMappedParser;
import coordinate.parser.obj.OBJParser;
import java.util.Arrays;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        OBJMappedParser parser = new OBJMappedParser();
        parser.readAttributes("C:\\Users\\user\\Documents\\Scene3d\\simplebox\\powerplant.obj");
        
    }
}
