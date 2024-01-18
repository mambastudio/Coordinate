/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 *
 * @author jmburu
 */
public abstract class JSONValue {
    
    public abstract boolean isNumber();    
    public abstract boolean isString();     
    public abstract boolean isBoolean();
    public abstract boolean isArray();
    public abstract boolean isObject();
    
}
