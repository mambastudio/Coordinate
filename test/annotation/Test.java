/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package annotation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        Car car = new Car();
        for (Field f: Car.class.getFields()) {
            if(f.isAnnotationPresent(JsonField.class))
            {
                JsonField column = f.getAnnotation(JsonField.class);
                if (column != null)
                    System.out.println(column.arraysize());
            }
        }
        Field[] fields = Car.class.getFields();
        Field field = fields[0];
        
        if(field.getType().isArray())
        {
            Object myArray = Array.newInstance(field.getType().getComponentType(), 13);
            for(int i = 0; i<13; i++)
                Array.set(myArray, i, 3);
        }
    }
    
    public static boolean set(Object object, Field field, Object fieldValue) 
    {
        Class<?> clazz = object.getClass();
        while (clazz != null) 
        {
            field.setAccessible(true);
            try {
                field.set(object, fieldValue);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
                return true;            
        }
        return false;
}
}
