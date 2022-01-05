/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.println;

/**
 *
 * @author user
 */
public class PrintInteger {
    private int[] arr;    
    private int swidth = 3;
    
    private int xlength = 0;
    private int ylength = 1;
    
    public PrintInteger()
    {
        arr = null;
    }
    public PrintInteger(int... arr)
    {
        this.arr = arr;
        if(arr != null)
            xlength = arr.length;
    }
    
    public void set(int... arr)
    {
        this.arr = arr;
        if(arr != null)
            xlength = arr.length;
    }
    
    public void setPrecision(int swidth)
    {
        this.swidth = swidth;
    }
    
    public void setDimension(int xlength, int ylength)
    {
        if((xlength * ylength) > arr.length)
            throw new RuntimeException("out of range dimension");
        this.xlength = xlength;
        this.ylength = ylength;
    }
    
    public void printArray()
    {
        for(int y = 0; y<ylength; y++)
        {
            for(int x = 0; x<xlength; x++)
                System.out.printf("%-"+swidth+"d", arr[getIndex(x, y)]);
            System.out.println();
        }
    }
    
    private int getIndex(int x, int y)
    {
        if(x < 0 || y < 0)
            throw new RuntimeException("out of range sat region");
        if(x > xlength-1 || y > ylength - 1)
            throw new RuntimeException("out of range sat region");
        return y * xlength + x; //y*width + x
    }
}
