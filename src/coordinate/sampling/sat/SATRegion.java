/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.sampling.sat;

/**
 *
 * @author user
 */
public class SATRegion {
    
    //full region
    private final int nu;
    private final int nv;
    
    //sub region
    private int minX, minY; //location global id
    private int strideX, strideY; //exclusive since they represent length
    
    public SATRegion(int nu, int nv)
    {
        this.nu = nu;
        this.nv = nv;
    }
    
    public void setRegion(int minX, int minY, int strideX, int strideY)
    {
        if(minX < 0 || minY < 0)
            throw new RuntimeException("out of range sat region");
        if(minX + strideX > nu || minY + strideY > nv)
            throw new RuntimeException("out of range sat region");
        if(strideX <= 0 || strideY <= 0)
            throw new RuntimeException("mismatch of sat region");
        this.minX = minX;
        this.minY = minY;
        this.strideX = strideX;
        this.strideY = strideY;
    }
    
    public int getIndex(int x, int y)
    {
        if(x < 0 || y < 0)
            throw new RuntimeException("out of range sat region");
        if(x > nu-1 || y > nv-1)
            throw new RuntimeException("out of range sat region");
        return y  * nu + x; //y*width + x
    }
    
    public int getLastIndexX()
    {
        return minX + lengthX() - 1;
    }
    
    public int getLastIndexY()
    {
        return minY + lengthY() - 1;
    }
    
    public int getMinX()
    {
        return minX;
    }
    
    public int getMinY()
    {
        return minY;
    }
    
    public int lengthX()
    {
        return strideX;
    }
    
    public int lengthY()
    {
        return strideY;
    }
    
    public int getNu()
    {
        return nu;
    }
    
    public int getNv()
    {
        return nv;
    }
    
    public int getRegionArea()
    {
        return lengthX()*lengthY();
    }
    
    public void setMin(int minX, int minY)
    {
        this.setRegion(minX, minY, strideX, strideY);
    }
    
    public void setStride(int strideX, int strideY)
    {
        this.setRegion(minX, minY, strideX, strideY);
    }
}
