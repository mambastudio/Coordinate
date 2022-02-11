/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.sampling.sat;

import coordinate.utility.Value2Df;
import coordinate.utility.Value2Di;
import coordinate.utility.Value4Df;
import static java.lang.Math.max;
import java.util.Arrays;

/**
 *
 * @author user
 * 
 * 
 * subgrid index means index of subgrid in comparison with the total number of 
 * subgrids available and not local coordinate inside subgrid
 * 
 */
public class SATSubgrid {
    protected final float[] func;
    protected final float[] sat;
    
    
    protected int nu, nv; //global size
    protected int subgridRangeX, subgridRangeY; //local size
    
    public SATSubgrid(int nu, int nv)
    {
        this.func = new float[nu * nv];
        this.sat  = new float[nu * nv];       
        
        this.nu = nu;
        this.nv = nv;
        
        this.subgridRangeX = nu;
        this.subgridRangeY = nv;        
    }
    
    public void reset()
    {
        Arrays.fill(func, 0);
        Arrays.fill(sat, 0);
    }
    
    public SATSubgrid(int subgridRangeX, int subgridRangeY, int nu, int nv)
    {
        if(nu%subgridRangeX != 0)
            throw new ArithmeticException("mismatch of SAT x length and rangex of subgrid");
        if(nv%subgridRangeY != 0)
            throw new ArithmeticException("mismatch of SAT y length and rangey of subgrid");
        
        this.func = new float[nu * nv];
        this.sat  = new float[nu * nv];
        
        this.nu = nu;
        this.nv = nv;
        
        this.subgridRangeX = subgridRangeX;
        this.subgridRangeY = subgridRangeY;        
    }  
    
    public SATSubgrid(int nu, int nv, float[] arr)
    {
        this(nu, nv);
        setArray(arr);
    } 
    
    public final void setArray(float... arr)
    {
        System.arraycopy(arr, 0, func, 0, arr.length);
        System.arraycopy(arr, 0, sat, 0, arr.length);
        calculateSAT();        
    }
    
    public final void setSubArray(float... arr)
    {
        int arrsize = subgridRangeX * subgridRangeY;
        if(arr.length != arrsize)
            throw new ArithmeticException("out of range length of subarray " +arr.length+ " which should be " +arrsize);
        
        //TODO
        for(int i = 0; i<func.length; i++)
            func[i] = arr[subgridIndexFromGlobalIndex(i)];
    }
    
    public void calculateSAT()
    {        
        prefixSumRow();        
        prefixSumCol();
    }
        
    //inclusive scan
    public void prefixSumRow()
    {
        for(int i = 0; i<func.length; i+=subgridRangeX)
        {
            float v = 0;                       
            //scan
            for(int x = 0; x<subgridRangeX; x++) //local index
            {
                int globalIndex = x + i; //global index of array
                v = v + sat[globalIndex];
                sat[globalIndex] = v;          
            }
        }
    }
       
    //inclusive scan
    public void prefixSumCol()
    {
        for(int i = 0; i<func.length; i+=subgridRangeY) //local size
        {
            float v = 0;
            int xi = i/nv; //get col index
            //scan
            for(int y = 0; y<subgridRangeY; y++) //local index
            {
                int yi = i%nv + y;   //get row index
                
                int globalIndex = xi + yi*nu; //global index of array
                v = v + sat[globalIndex];
                sat[globalIndex] = v;
            }
        }       
    }
       
    //get sat value within specific subgrid
    private float getSubgridValueSAT(int subgridIndex, int sx, int sy)
    {
        checkSubgridIndex(subgridIndex);
        boolean isSubgridInRange = isSubgridIndexInRangeXY(sx, sy);
        
        if(!isSubgridInRange)
            return 0;
        
        int globalIndex = globalIndexInSubgrid(subgridIndex, sx, sy);
        return sat[globalIndex];
    }
    
   
    
    /** 
     * parameters are not ranges but exact value points  
     * 
     * @param subgridIndex
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     * @return 
     */
    
    public float getSubgridSATRange(int subgridIndex, int minX, int minY, int maxX, int maxY)
    {
        checkSubgridIndex(subgridIndex);
        float A = getSubgridValueSAT(subgridIndex, maxX, maxY);
        float B = getSubgridValueSAT(subgridIndex, minX-1, maxY);
        float C = getSubgridValueSAT(subgridIndex, maxX, minY-1);
        float D = getSubgridValueSAT(subgridIndex, minX-1, minY-1);
        return A+D-B-C;
    }
    
    //x dimension size of subgrid
    public int subgridSizeX()
    {
        return subgridRangeX;
    }
    
    //y dimension size of subgrid
    public int subgridSizeY()
    {
        return subgridRangeY;
    }
    
    //number of subgrid in x direction
    public int subgridCountX()
    {
        return nu/subgridSizeX();
    }
    
    //number of subgrid in y direction
    public int subgridCountY()
    {
        return nv/subgridSizeY();
    }
    
    //total number of subgrid
    public int subgridCount()
    {
        return subgridCountX()*subgridCountY();
    }
    
    //area of one subgrid
    public int subgridArea()
    {
        return subgridSizeX()*subgridSizeY();
    }
    
    public int getSubgridSizeLastIndexX()
    {
        return subgridSizeX()-1;
    }
    
    public int getSubgridSizeLastIndexY()
    {
        return subgridSizeY()-1;
    }
    
    public Value4Df getSubgridUnitBound(Value2Di tileXY)
    {
        int tx = tileXY.x, ty = tileXY.y;
              
        Value2Df unitMin = new Value2Df(
                tx/(float)this.subgridRangeX,
                ty/(float)this.subgridRangeY);
        Value2Df unitMax = new Value2Df(
                (tx + 1)/(float)this.subgridRangeX,
                (ty + 1)/(float)this.subgridRangeY);
        Value4Df unitBound = new Value4Df(
                unitMin.x, unitMin.y,
                unitMax.x, unitMax.y
        );
        
        return unitBound;
    }    
       
    /** 
     * get index of sub-grid (1 dimension) from global index
     * 
     * this uses the enhanced form of:
     *          x = i%w;
     *          y = i/w;
     * then an enhanced form of:
     *      index = (x/ssizex) + (y/ssizey)*wsub 
     * where:
     *       wsub = number of sub-grids in x direction
     *     ssizex = subgrid size in x direction
     *     ssizey = subgrid size in y direction
     * 
     * @param globalIndex
     * @return 
     */
    public int subgridIndexFromGlobalIndex(int globalIndex)
    {
        if(globalIndex >= func.length)
            throw new ArithmeticException("global index out of range -> " +globalIndex+ " maximum index is " +(func.length-1));
        return (globalIndex % nu) / subgridSizeX() + (globalIndex / nu) / subgridSizeY() * subgridCountX();
    }
    
    /**
     * get index of sub-grid (2 dimension)
     * 
     * given global index, find the sub-grid coordinates (x, y)
     * 
     * @param globalIndex
     * @return 
     */
    public int[] subgridIndexXY(int globalIndex)
    {
        if(globalIndex >= func.length)
            throw new ArithmeticException("global index out of range -> " +globalIndex+ " maximum index is " +(func.length-1));
        int indexSubgrid = subgridIndexFromGlobalIndex(globalIndex);
        int x = indexSubgrid%subgridCountX();
        int y = indexSubgrid/subgridCountX();
        
        return new int[]{x, y};
    }
    
    /**
     * Global index of sub-grid given subgridIndexFromGlobalIndex(x, y)
 
        It is an enhanced form of x + y*w -> subIndexX*subSizeX + subIndexY*subArea * subCountX
     * 
     * @param subgridIndexX
     * @param subgridIndexY
     * @return 
     */
    public int subgridGlobalIndex(int subgridIndexX, int subgridIndexY)
    {
        if(subgridIndexX >= subgridCountX())
            throw new ArithmeticException("subgridIndex out of range in x direction -> " +subgridIndexX+ " range " +subgridCountX());
        if(subgridIndexY >= subgridCountY())
            throw new ArithmeticException("subgridIndex out of range in y direction -> " +subgridIndexY+ " range " +subgridCountY());
        
        return subgridIndexX * subgridSizeX() + subgridIndexY * subgridArea() * subgridCountX();
    }
    
    /**
     * to get x and y coordinate you;     * 
     *          x = i%w;
     *          y = i/w;
     * 
     * implementation here is a version of {@link #subgridGlobalIndex(int, int)}
     * 
     * @param subgridIndex
     * @return 
     */
    public int subgridGlobalIndex(int subgridIndex)
    {
        if(subgridIndex >= subgridCount())
            throw new ArithmeticException("subgridIndex out of range-> " +subgridIndex+ " range " +subgridCount());
        
        int subgridIndexX = subgridIndex%subgridCountX();
        int subgridIndexY = subgridIndex/subgridCountX();
        
        return subgridGlobalIndex(subgridIndexX, subgridIndexY);
    }
    
    public int[] subgridGlobalIndexXY(int subgridIndex)
    {
        int globalIndex = subgridGlobalIndex(subgridIndex);
        
        int globalIndexX  = globalIndex%nu;
        int globalIndexY  = globalIndex/nv;
        
        return new int[]{globalIndexX, globalIndexY};
    }
    /**
     * @param subgridIndex
     * @param localX is index x in sub-grid or local coordinate in sub-grid
     * @param localY is index y in sub-grid or local coordinate in sub-grid
     * @return      
     */
    public int[] globalIndexXYInSubgrid(int subgridIndex, int localX, int localY)
    {        
        int[] globalXY = subgridGlobalIndexXY(subgridIndex);
        globalXY[0] += localX;
        globalXY[1] += localY;
        return globalXY;
    }
    /**
     * @param subgridIndex
     * @param localX is index x in sub-grid or local coordinate in sub-grid
     * @param localY is index y in sub-grid or local coordinate in sub-grid
     * @return      
     */
    public int globalIndexInSubgrid(int subgridIndex, int localX, int localY)
    {
        int[] globalXY = globalIndexXYInSubgrid(subgridIndex, localX, localY);        
        return globalXY[0] + globalXY[1]*nu;
    }   
    
    public int globalIndexInSubgrid(int subgridIndex, int localIndex)
    {
        int localX = localIndex % this.subgridSizeX();
        int localY = localIndex / this.subgridSizeX();
        int[] globalXY = globalIndexXYInSubgrid(subgridIndex, localX, localY);        
        return globalXY[0] + globalXY[1]*nu;
    } 
   
    protected void checkSubgridIndex(int subgridIndex)
    {
        if(subgridIndex >= subgridCount())
            throw new ArithmeticException("subgridIndex out of range-> " +subgridIndex+ " range " +subgridCount());
    }
    
    protected boolean isSubgridIndexInRangeXY(int sx, int sy)
    {
        if(sx >= subgridSizeX() || sx < 0)
            return false;
        return !(sy >= subgridSizeY() || sy < 0);
    }
    
    public int getSubgridIndex(int subgridIndexX, int subgridIndexY)
    {
        return subgridIndexX + subgridIndexY * subgridCountX();
    }
    
       
    //PROBABILITIES
    
    /**
     * getSATRange(0, y, region.getLastIndexX(), y)
     * - this function calculates cdf in one line row along rowY
     * - functional integral conditional, you divide by the length of line
     * @param subgridIndex
     * @param y
     * @return 
     */
    public float getFuncIntConditional(int subgridIndex, int y)
    {        
        return getSubgridSATRange(subgridIndex, 0, y, getSubgridSizeLastIndexX(), y);        
    }
    
    public float getFunc(int subgridIndex, int sx, int sy)
    {
        int index = globalIndexInSubgrid(subgridIndex, sx, sy);
        return func[index];
    }
    
    public float[] getFuncArray()
    {
        return func;
    }
    
    public float[] getSATArray()
    {
        return sat;
    }
    
    public float getConditional(int subgridIndex, int x, int y) //columnX, rowY
    {
        float funcInt = getFuncIntConditional(subgridIndex, y);
        return getSubgridSATRange(subgridIndex, 0, y, x - 1, y)/funcInt;
    }
    
    public float getMarginal(int subgridIndex, int y) //rowY
    {
        float marginalLast = getSubgridValueSAT(subgridIndex, getSubgridSizeLastIndexX(), getSubgridSizeLastIndexY());        
        return getSubgridValueSAT(subgridIndex, getSubgridSizeLastIndexX(), y - 1)/marginalLast;
    }
      
    public float getPdfContinuousConditional(int subgridIndex, int x, int y) //offset along col, which row y
    {
        float funcValue = getFunc(subgridIndex, x, y);
        float funcInt = getFuncIntConditional(subgridIndex, y);
        
        return funcValue/funcInt;
    }
    
    
    public float getPdfContinuousMarginal(int subgridIndex, int y)
    {
        float funcInt = getFuncIntConditional(subgridIndex, y);
        float lastSAT = getSubgridValueSAT(subgridIndex, getSubgridSizeLastIndexX(), getSubgridSizeLastIndexY());
        
        float pdf = (funcInt * subgridArea()) / lastSAT;
        
        if(Float.isNaN(pdf))
            System.out.println(lastSAT);
        
        return pdf;
    }
    
    public float getPdf(int subgridIndex, int x, int y)
    {
        float pdfV = getPdfContinuousMarginal(subgridIndex, y);
        float pdfU = getPdfContinuousConditional(subgridIndex, x, y); //offset along col, which row y
        
        return pdfU * pdfV;
    }
        
    public float sampleContinuousConditional(int subgridIndex, float u, int y, int[] off, float[] pdf) //float u, int y, int off[], float pdf[]
    {
        int ptr = upperBoundConditional(subgridIndex, y, 0, subgridSizeX(), u); //linear search
        int offset = max(0, ptr - 1);        
        
        
        if(off != null)
            off[0] = offset;
        
        // Compute offset along CDF segment
        float du = (u - getConditional(subgridIndex, offset, y)) / (getConditional(subgridIndex, offset + 1, y) - getConditional(subgridIndex, offset, y));
       
        // Compute PDF for sampled offset
        if (pdf != null) {
            pdf[0] = getPdfContinuousConditional(subgridIndex, offset, y);            
        }
        
        return (offset + du) / subgridSizeX();    
    }
    
    
    public float sampleContinuousMarginal(int subgridIndex, float u, int[] off, float[] pdf)
    {
        int ptr = upperBoundMarginal(subgridIndex, 0, subgridSizeY(), u); //linear search
        int offset = max(0, ptr - 1);
        
        if(off != null)
            off[0] = offset;
        
       
        
        // Compute offset along CDF segment
        float du = (u - getMarginal(subgridIndex, offset)) / (getMarginal(subgridIndex, offset + 1) - getMarginal(subgridIndex, offset));
        
        // Compute PDF for sampled offset
        if (pdf != null) {            
            pdf[0] = getPdfContinuousMarginal(subgridIndex, offset);
        }
               
        return (offset + du) / subgridSizeY();    
    }
    
    public int sampleDiscreteConditional(int subgridIndex, float u, int y) //random number, rowY
    {
        int ptr = upperBoundConditional(subgridIndex, y, 0, subgridSizeX(), u); //linear search
        return max(0, ptr - 1);
    }
        
        
    private int upperBoundConditional(int subgridIndex, int y, int first, int last, float value) 
    {
        int x;
        for (x = first; x < last; x++) {
            if (getConditional(subgridIndex, x, y) > value) {
                break;
            }
        }
        return x;
    }
    
    private int upperBoundMarginal(int subgridIndex, int first, int last, float value) 
    {
        int y;
        for (y = first; y < last; y++) {
            if (getMarginal(subgridIndex, y) > value) {
                break;
            }
        }        
        return y;
    }
    
    public void sampleContinuous(int subgridIndex, float u0, float u1, float[] uv, float[] pdf)
    {
        this.sampleContinuous(subgridIndex, u0, u1, uv, pdf, null);
    }
    
    public void sampleContinuous(int subgridIndex, float u0, float u1, float[] uv, float[] pdf, int[] offset)
    {
        float[] pdfs = new float[2];
        float[] pdfTemp = new float[1];
        int v[] = new int[1];        
        
        //start with marginal and then conditional
        uv[1] = sampleContinuousMarginal(subgridIndex, u1, v, pdfTemp);
        pdfs[1] = pdfTemp[0];        
        if(offset != null)
            offset[1] = v[0];
        
        uv[0] = sampleContinuousConditional(subgridIndex, u0, v[0], v, pdfTemp);
        pdfs[0] = pdfTemp[0];
        if(offset != null)
            offset[0] = v[0];
        
        if(pdf != null)
            pdf[0] = pdfs[0] * pdfs[1];
        
        
    }
    
    public float[] getUpperGrid()
    {
        int subgridcount = subgridCount();
        
        float[] uppergrid = new float[subgridcount];
        
        
        for(int y = 0; y<subgridCountY(); y++)
        {
            for(int x = 0; x<subgridCountX(); x++)
            {
                int subgridIndex = getSubgridIndex(x, y);
                int globalIndex = globalIndexInSubgrid(
                        subgridIndex, 
                        getSubgridSizeLastIndexX(), 
                        getSubgridSizeLastIndexY());
                
                uppergrid[subgridIndex] = sat[globalIndex];
            }
        }
        
        return uppergrid;
    }
}
