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
import coordinate.json.values.JSONArray;

/**
 *
 * @author user
 */
public class JSONTest {
    public static void main(String... args) throws IOException
    {
        Path path = Paths.get("C:\\Users\\jmburu\\Documents\\File Examples", "JSON-5.json");
        JSONParserWhole parser = new JSONParserWhole(path);
        //JSONObject object = parser.parseObject();
        JSONArray object = parser.parseArray();
        
        System.out.println(JSONFormatString.getString(object));
    }
}
