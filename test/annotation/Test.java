/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package annotation;

import java.lang.reflect.Field;

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
    }
}
