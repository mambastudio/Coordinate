/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import coordinate.json.JSONFormatString;
import coordinate.json.JSONParserWhole;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import coordinate.json.values.JSONValue;

/**
 *
 * @author user
 */
public class JSONTest {
    public static void main(String... args) throws IOException
    {
        Path path = Paths.get("C:\\Users\\user\\Documents\\File Examples", "JSON-3.json");
        JSONParserWhole parser = new JSONParserWhole(path);        
        JSONValue object = parser.parse();
        
        System.out.println(JSONFormatString.getString(object));
    }
}
