/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import java.util.HashMap;

/**
 *
 * @author user
 */
public class Plugins {
    private final HashMap<Class, Object> map = new HashMap();
    
    public <T> void addPluginClass(Class<T> clazz, T t)
    {
        if(t != null)
            map.put(clazz, t);
    }
    
    public <T> T get(Class<T> clazz)
    {
        if(map.containsKey(clazz))
            return (T) map.get(clazz);
        else
            throw new UnsupportedOperationException("No plugin registered");
    }
}
