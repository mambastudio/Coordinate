/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author user
 */
public class Value2Di {
    public int x, y;
    
    public Value2Di()
    {
        x = y = 0;
    }
    
    public Value2Di(int x, int y)
    {
        this.x = x; this.y = y;
    }
    
    public void set(int x, int y)
    {
        this.x = x; this.y = y;
    }
    
    public void applyJitterFromCenter(int rangeX, int rangeY)
    {
        int minX = -1*Math.abs(rangeX);
        minX += x;        
        int minY = -1*Math.abs(rangeY);
        minY += y;        
        int maxX = Math.abs(rangeX);
        maxX += x;
        int maxY = Math.abs(rangeY);
        maxY += y;
                
        x = getRandomNumber(minX, maxX);
        y = getRandomNumber(minY, maxY);
        
    }
    
    private int getRandomNumber(int min, int max) {
        //ThreadLocalRandom.current().nextInt(min, max + 1);
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
    
    @Override
    public String toString()
    {
        int[] array = new int[]{x, y};
        return Arrays.toString(array);
    }
    
    public boolean allNonZero()
    {
        float array[] = new float[]{x, y};
        boolean result = true;
        int i;
        for(i = 0; i < array.length; ++i)
        {
            result &= array[i]>0;
        }
        return result;
    }
}
