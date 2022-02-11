/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.sampling.sat;

import coordinate.utility.Utility;
import coordinate.utility.Value2Di;
import coordinate.utility.Value4Df;
import static java.lang.Math.max;

/**
 *
 * @author user
 * 
 * 
 * Takes that Summed Area Table approach, which seems simpler than pbrt Distribution2D
 * 
 */
public final class SAT {    
    private final float[] func;
    private final float[] sat;
    
    private int nu, nv; //global size
    
    private final SATRegion region;
        
    public SAT(int nu, int nv)
    {
        this.func = new float[nu * nv];
        this.sat  = new float[nu * nv];
        
        this.nu = nu;
        this.nv = nv;
        
        this.region = new SATRegion(nu, nv);
        this.region.setRegion(0, 0, nu, nv);
    }  
    
    public SAT(int nu, int nv, float[] arr)
    {
        this(nu, nv);
        setArray(arr);
    } 
    
    public int getNu()
    {
        return (int) nu;
    }
    
    public int getNv()
    {
        return (int) nv;
    }
    
    public SATRegion getRegion()
    {
        return region;
    }
    
    public void setRegion(int minX, int minY, int strideX, int strideY)
    {
        this.region.setRegion(minX, minY, strideX, strideY);
    }
    
    public void setRegion(SATRegion region)
    {
        setRegion(region.getMinX(), region.getMinY(), region.lengthX(), region.lengthY());
    }
    
    public void revertToFullRegion()
    {
        this.region.revertToFullRegion();
    }
    
     //sub region
    public SATRegion getSubRegionFromUnitBound(Value4Df unitBound)
    {
        SATRegion subregion;
        Value2Di minXY = new Value2Di(
                (int)(nu * unitBound.x), 
                (int)(nv * unitBound.y));
        Value2Di maxXY = new Value2Di(
                (int)(nu * unitBound.z), 
                (int)(nv * unitBound.w));
        
        Value2Di strideXY = new Value2Di(
                maxXY.x - minXY.x,
                maxXY.y - minXY.y);
        
        subregion = new SATRegion(nu, nv);        
        subregion.setRegion(minXY.x, minXY.y, strideXY.x, strideXY.y);
        
        return subregion;
    }
    
    public void setArray(float... arr)
    {
        System.arraycopy(arr, 0, func, 0, arr.length);
        System.arraycopy(arr, 0, sat, 0, arr.length);
        calculateSAT();
        
        //for(int i = 0; i<sat.length; i++)
        //    sat[i] = (int)sat[i];
    }
    
    public float[] getFuncArray()
    {
        return func;
    }
    
    public float[] getSATArray()
    {
        return sat;
    }
    
    public float getFunc(int x, int y)
    {
        int index = region.getIndex(x, y);
        return func[index];
    }
    
    public void setFunc(int x, int y, float value)
    {
        int index = region.getIndex(x, y);
        func[index] = value;
    }
    
    private float getCummulative(int x, int y)
    {
        int index = region.getIndex(x, y);
        return sat[index];
    }
    
    private float getCummulative(int index)
    {
        return sat[index];
    }
       
    private void setCummulative(int index, float value)
    {        
        sat[index] = value;
    }
    
    public void calculateSAT()
    {        
        prefixSumRow();        
        prefixSumCol();
    }
        
    //inclusive scan
    public void prefixSumRow()
    {
        for(int i = 0; i<func.length; i+=nu)
        {
            float v = 0;                       
            //scan
            for(int x = 0; x<nu; x++) //local index
            {
                int index = x + i; //global index of array
                v = v + getCummulative(index);
                setCummulative(index, v);               
            }
        }
    }
       
    //inclusive scan
    public void prefixSumCol()
    {
        for(int i = 0; i<func.length; i+=nv) //local size
        {
            float v = 0;
            int xi = (int) (i/nv); //get col index
            //scan
            for(int y = 0; y<nv; y++) //local index
            {
                int yi = (int) (i%nv + y);   //get row index
                
                int index = (int) (xi + yi*nu); //global index of array
                v = v + getCummulative(index);
                setCummulative(index, v);
            }
        }       
    }
    
    /**
     * Don't check
     *      x < region.getMinX();
     *      y < region.getMinY();
     * 
     * This is because getSATRange checks value at 
     *      region.getMinX-1;
     *      retion.getMinY-1;
     * 
     */
    
    private float getValueSAT(int x, int y)
    {
        
        if(x < 0)
            return 0;
        if(y < 0)
            return 0;         
        if(x > region.getLastIndexX())
            return 0;
        if(y > region.getLastIndexY())
            return 0;        
        else
            return getCummulative(x, y);
    }
    
    /** 
     * 
     * https://blog.demofox.org/2018/04/16/prefix-sums-and-summed-area-tables/
     * 
     * parameters are not ranges but exact value points  
     * 
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     * @return 
     */
    public float getSATRange(int minX, int minY, int maxX, int maxY)
    {
       
        float D = getValueSAT(maxX, maxY);
        float C = getValueSAT(minX-1, maxY);
        float B = getValueSAT(maxX, minY-1);
        float A = getValueSAT(minX-1, minY-1);          
        return A+D-B-C;
    }
        
    public float getConditional(int x, int y) //columnX, rowY
    {
        float funcInt = getFuncIntConditional(y);
        return getSATRange(region.getMinX(), y, x - 1, y)/funcInt;
    }
    
    public float getMarginal(int y) //rowY
    {   
        float marginalLast = getSATRange(region.getMinX(), region.getMinY(), region.getLastIndexX(), region.getLastIndexY());
        return getSATRange(region.getMinX(), region.getMinY(), region.getLastIndexX(), y - 1)/marginalLast;
    }
    
    /**
     * getSATRange(0, y, region.getLastIndexX(), y)
     * - this function calculates cdf in one line row along rowY
     * - functional integral conditional, you divide by the length of line
     * @param y
     * @return 
     */
    public float getFuncIntConditional(int y)
    {
        return getSATRange(region.getMinX(), y, region.getLastIndexX(), y);        
    }
    
    public float getFuncIntMarginal(int y)
    {
        float funcInt = getFuncIntConditional(y);
        return funcInt * region.getRegionArea();
    }
    
    public float getPdfContinuousConditional(int x, int y) //offset along col, which row y
    {
        float funcValue = getFunc(x, y);
        float funcInt = getFuncIntConditional(y);
        
        return funcValue/funcInt;
    }
        
    public float getPdfContinuousMarginal(int y)
    {
        float funcInt = getFuncIntConditional(y);
        float lastSAT = getSATRange(region.getMinX(), region.getMinY(), region.getLastIndexX(), region.getLastIndexY());
        
        return (funcInt * region.getRegionArea()) / lastSAT;
    }
    
    public float getPdf(int index)
    {
        int x = index%nu;
        int y = index/nu;
        
        return getPdf(x, y);
    }
    
    public float getPdf(int x, int y)
    {
        float pdfV = getPdfContinuousMarginal(y);
        float pdfU = getPdfContinuousConditional(x, y); //offset along col, which row y
        
        return pdfU * pdfV;
    }
        
    public float sampleContinuousConditional(float u, int y, int[] off, float[] pdf) //float u, int y, int off[], float pdf[]
    {
        int ptr = upperBoundConditional(y, region.getMinX(), region.getMinX()+region.lengthX(), u); //linear search
        int offset = max(0, ptr - 1);        
        
        if(off != null)
            off[0] = offset;
        
        // Compute offset along CDF segment
        float du = (u - getConditional(offset, y)) / (getConditional(offset + 1, y) - getConditional(offset, y));
       
        // Compute PDF for sampled offset
        if (pdf != null) {
            pdf[0] = getPdfContinuousConditional(offset, y);            
        }
        
        return (offset + du) / (region.getNu());    
    }
    
    
    public float sampleContinuousMarginal(float u, int[] off, float[] pdf)
    {
        int ptr = upperBoundMarginal(region.getMinY(), region.getMinY()+region.lengthY(), u); //linear search
        int offset = max(0, ptr - 1);
        
        if(off != null)
            off[0] = offset;
        
        // Compute offset along CDF segment
        float du = (u - getMarginal(offset)) / (getMarginal(offset + 1) - getMarginal(offset));
        
        // Compute PDF for sampled offset
        if (pdf != null) {            
            pdf[0] = getPdfContinuousMarginal(offset);
        }
        
        System.out.println(offset + du);
                       
        return (offset + du) / (region.getNv());    
    }
    
    public int sampleDiscreteConditional(float u, int y, float pdf[])
    {
        // Find surrounding CDF segments and _offset_
        int ptr = upperBoundConditional(y, region.getMinX(), region.getMinX()+region.lengthX(), u); //linear search
        int offset = max(0, ptr - 1);
        
        if (pdf != null) {
            pdf[0] = getPdfContinuousConditional(offset, y);
        }
        return offset;
    }
    
    public int sampleDiscreteMarginal(float u, float pdf[])
    {
        int ptr = upperBoundMarginal(region.getMinY(), region.getMinY()+region.lengthY(), u); //linear search
        int offset = max(region.getMinY(), ptr - 1);
        
        //similar to pdf marginal but has additional division
        if (pdf != null) {
            pdf[0] = getPdfContinuousMarginal(offset);
        }
        return offset;
    }
    
    public int sampleDiscreteConditional(float u, int y) //random number, rowY
    {
        int ptr = upperBoundConditional(y, region.getMinX(), region.getMinX()+region.lengthX(), u); //linear search
        return max(region.getMinX(), ptr - 1);
    }
        
    //binary search (from opencl code)    
    private int upperBoundConditional(int y, int first, int last, float value) 
    {
        int begin = first;
        int end = last;

        while(begin != end) {
            int mid = begin + (end - begin) / 2;
            float midValue =  getConditional(mid, y);

            boolean b_right = !(value < midValue);
            
            begin = b_right ? mid + 1 : begin;
            end =  b_right ? end : mid; 
        }

        return begin;
    }
    
    //binary search (from opencl code)    
    private int upperBoundMarginal(int first, int last, float value) //last is exclusive
    {
        int begin = first;
        int end = last;

        while(begin != end) {
            int mid = begin + (end - begin) / 2;
            float midValue =  getMarginal(mid);

            boolean b_right = !(value < midValue);
            begin = b_right ? mid + 1 : begin;
            end =  b_right ? end : mid; 
        }
        
        return Utility.clamp(begin, first, end);
    }
        
    public void sampleContinuous(float u0, float u1, float[] uv, int offset[], float[] pdf)
    {
        float[] pdfs = new float[2];
        float[] pdfTemp = new float[1];
        int offsetMarg[] = new int[1];
        int offsetCond[] = new int[1];
        
        //start with marginal and then conditional
        uv[1] = sampleContinuousMarginal(u1, offsetMarg, pdfTemp);
        pdfs[1] = pdfTemp[0];
        uv[0] =  sampleContinuousConditional(u0, offsetMarg[0], offsetCond, pdfTemp);
        pdfs[0] = pdfTemp[0];
        
        if(offset != null)
        {
            offset[0] = offsetCond[0];
            offset[1] = offsetMarg[0];
        }
        
        if(pdf != null)
            pdf[0] = pdfs[0] * pdfs[1];
    }
    
    public void sampleDiscrete(float u0, float u1, int[] offset, float[] pdf)
    {
        float[] pdfs = new float[2];
        float[] pdfTemp = new float[1];
        
        //start with marginal and then conditional
        offset[1]   = sampleDiscreteMarginal(u1, pdfTemp);
        pdfs[1]     = pdfTemp[0];
        offset[0]   = sampleDiscreteConditional(u0, offset[1], pdfTemp);
        pdfs[0]     = pdfTemp[0];
                        
        if(pdf != null)
            pdf[0] = pdfs[0] * pdfs[1]/region.getRegionArea();
    }
    
    
    //sample elements (func) and put the samples into a new grid float
    public float[] sampleToGrid(int count, int w, int h)
    {
        float lumMax = 0;
        int size = w * h;
        if(size <= 0 || count <= 0)
            throw new ArithmeticException("size or count is 0 or less: size " +size+ " count " +count);
        float[] arr = new float[size];
        
        for(int c = 0; c<count; c++)
        {
            /**
             * TODO - change the sample continous to discrete
             * nothing wrong, just that the sample discrete still return unit
             * range bounds, and also discrete values
            */
            
            float u0    = (float) Math.random();
            float u1    = (float) Math.random();
            float [] uv = new float[2]; //uv bounds are unit range [0-1]
            
            sampleContinuous(u0, u1, uv, null, null); //unit range [0-1]
            
            int sx = (int) (uv[0] * w);
            int sy = (int) (uv[1] * h);            
            int si = sx + w * sy;
            
            int x = (int) (uv[0] * nu);
            int y = (int) (uv[1] * nv);
            int i = (int) (x + nu * y);
                        
            if(arr[si] < func[i])
                arr[si] = func[i];
            
            if(arr[si] > lumMax)
                lumMax = arr[si];
        }
        System.out.println("maximum luminance32 " +lumMax);
        return arr;
    }
}
