/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.struct;

import coordinate.struct.cache.StructArrayCache;
import coordinate.struct.cache.StructBufferCache;
import coordinate.struct.cache.StructDirectCache;
import coordinate.struct.cache.StructJNACache;
import coordinate.struct.cache.StructUnsafeCache;

/**
 *
 * @author user
 */
public class StructUtils {
    public static <MemoryType extends StructAbstractMemory<GlobalBuffer>, GlobalBuffer, CacheType extends StructAbstractCache<MemoryType, GlobalBuffer>> //generic types
         StructAbstractCache<MemoryType, GlobalBuffer> //return type
        createStructCache(Class<MemoryType> clazzStruct, Class<CacheType> clazzCache, long capacity) //method
    {
        if(StructAbstractMemory.class.isAssignableFrom(clazzStruct))
        { 
            if(StructArrayCache.class.isAssignableFrom(clazzCache))
                return new StructArrayCache(clazzStruct, (int) capacity);
            else if(StructBufferCache.class.isAssignableFrom(clazzCache))                
                return new StructBufferCache(clazzStruct, capacity);
            else if(StructDirectCache.class.isAssignableFrom(clazzCache))
                return new StructDirectCache(clazzStruct, (int) capacity);
            else if(StructUnsafeCache.class.isAssignableFrom(clazzCache))
                return new StructUnsafeCache(clazzStruct, capacity); 
            else if(StructJNACache.class.isAssignableFrom(clazzCache))
                return new StructJNACache(clazzStruct, capacity); 
        } 
        return null;
    }
        
       
}
