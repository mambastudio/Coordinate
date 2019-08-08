/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.generic.io.StringMappedReader;

/**
 *
 * @author user
 */
public class TestStringMappedFile {
    public static void main(String... args)
    {
        StringMappedReader reader = new StringMappedReader("C:\\Users\\user\\Documents\\Scene3d\\sphere", "sphere-cylcoords-1k.obj");        
        while(reader.hasRemaining())
        {
            System.out.println(reader.getNextToken());
        }
    }
}
