/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.utility.StringParser;

/**
 *
 * @author user
 */
public class Test9 {
    public static void main(String... args)
    {
        String string = "josto is good ";
        StringParser parser = new StringParser(string);
        
        System.out.println(parser.getToken());
        System.out.println(parser.getToken());
        System.out.println(parser.getToken()); 
        
        parser.skipContinousSpace();
        System.out.println(parser.isNewLine());
    }
}
