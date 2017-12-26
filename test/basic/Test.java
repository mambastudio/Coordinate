/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.list.FloatList;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String... args)
    {
        FloatList list = FloatList.forEachAdd(10, (index, size) -> (float)(size * Math.random()));
        System.out.println(list);
        list.forEachSet((value, index, size) -> (value > 4f) ? 3f : 1f);
        System.out.println(list);
    }
}
