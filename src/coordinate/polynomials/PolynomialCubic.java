/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.polynomials;

import coordinate.function.RootCallBackFloat;
import coordinate.polynomials.rootfinder.RootFinder;
import coordinate.polynomials.rootfinder.RootFinderNewton;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;
import java.util.Arrays;

/**
 *
 * @author jmburu
 */
public class PolynomialCubic extends Polynomials{
    
    public PolynomialCubic()
    {
        this(3, false, new RootFinderNewton(3, false), null);
    }

    private PolynomialCubic(int N, boolean boundingError, RootFinder rootFinder, RootCallBackFloat rootCallBack) {
        super(N, boundingError, rootFinder, rootCallBack);
    }

    @Override
    public int roots(float[] roots, float[] coef, float x0, float x1, float xError) 
    {
        float y0 = eval( coef, x0 );
	float y1 = eval( coef, x1 );

	float a   = coef[3]*3;
	float b_2 = coef[2];
	float c   = coef[1];

	float deriv[] = { c, 2*b_2, a, 0 };

	float delta_4 = b_2*b_2 - a*c;
        
        if(delta_4 > 0) 
        {
            float d_2 = (float) sqrt( delta_4 );
            float q = - ( b_2 + MultSign( d_2, b_2 ) );
            float rv0 = q / a;
            float rv1 = c / q;
            float xa = min( rv0, rv1 );
            float xb = max( rv0, rv1 );

            if ( IsDifferentSign(y0,y1)) 
            {
                if ( xa >= x1 || xb <= x0 || ( xa <= x0 && xb >= x1 ) ) {	// first, last, or middle interval only
                    roots[0] = rootFinder.findClosed( coef, deriv, x0, x1, y0, y1, xError );
                    return 1;
                }
            } 
            else 
            {
                if ( ( xa >= x1 || xb <= x0 ) || ( xa <= x0 && xb >= x1 ) ) 
                    return 0;
            }
            int numRoots = 0;
            if ( xa > x0 ) {
                float ya = eval( coef, xa );
                if ( IsDifferentSign(y0,ya) ) {
                    roots[0] = rootFinder.findClosed( coef, deriv, x0, xa, y0, ya, xError );	// first interval
                    if ( !boundError ) {
                        if ( IsDifferentSign(ya,y1) || ( xb < x1 &&  IsDifferentSign( ya, eval(coef,xb) ) ) ) {
                            float defPoly[] = new float[4];
                            deflate( defPoly, coef, roots[0] );
                            
                            //we can't get sub array of original reference in java
                            float[] newroots = Arrays.copyOfRange(roots, 1, roots.length);
                            int returnvalue = new PolynomialQuadratic(boundError).roots(newroots, defPoly, xa, x1, xError) + 1;
                            System.arraycopy(newroots, 0, roots, 1, newroots.length);
                            return returnvalue;
                            //return new PolynomialQuadratic(boundError).roots( roots+1, defPoly, xa, x1, xError);                                    
                        } 
                        else 
                            return 1;
                    } 
                    else numRoots++;
                }
                if ( xb < x1 ) 
                {
                    float yb = eval( coef, xb );
                    if ( IsDifferentSign(ya,yb) ) {
                        roots[!boundError ? 0 : numRoots++] =rootFinder.findClosed( coef, deriv, xa, xb, ya, yb, xError );
                        if ( !boundError ) 
                        {
                            if ( IsDifferentSign(yb,y1) ) {
                                float[] defPoly = new float[4];
                                deflate( defPoly, coef, roots[0] );
                                
                                //we can't get sub array of original reference in java
                                float[] newroots = Arrays.copyOfRange(roots, 1, roots.length);
                                int returnvalue = new PolynomialQuadratic(boundError).roots(newroots, defPoly, xb, x1, xError) + 1;
                                System.arraycopy(newroots, 0, roots, 1, newroots.length);
                                return returnvalue;                                
                                //return QuadraticRoots( roots+1, defPoly, xb, x1 ) + 1;
                            } 
                            else 
                                return 1;
                        }
                    }
                    if(IsDifferentSign(yb,y1)) {
                        roots[!boundError ? 0 : numRoots++ ] = rootFinder.findClosed( coef, deriv, xb, x1, yb, y1, xError );	// last interval
                        if ( !boundError ) return 1;
                    }
                } 
                else 
                {
                    if(IsDifferentSign(ya,y1)) 
                    {
                        roots[!boundError ? 0 : numRoots++] = rootFinder.findClosed( coef, deriv, xa, x1, ya, y1, xError );
                        if ( !boundError ) return 1;
                    }
                }
            } 
            else 
            {
                float yb = eval( coef, xb );
                if ( IsDifferentSign(y0,yb) ) {
                    roots[0] = rootFinder.findClosed( coef, deriv, x0, xb, y0, yb, xError );
                    if(!boundError) {
                        if ( IsDifferentSign(yb,y1) ) {
                            float defPoly[] = new float[4];
                            deflate(defPoly, coef, roots[0] );
                            
                            //we can't get sub array of original reference in java
                            float[] newroots = Arrays.copyOfRange(roots, 1, roots.length);
                            int returnvalue = new PolynomialQuadratic(boundError).roots(newroots, defPoly, xb, x1, xError) + 1;
                            System.arraycopy(newroots, 0, roots, 1, newroots.length);
                            return returnvalue;                     
                            //return QuadraticRoots( roots+1, defPoly, xb, x1 ) + 1;
                        } 
                        else 
                            return 1;
                    }
                    else 
                        numRoots++;
                }
                if(IsDifferentSign(yb,y1)) {
                    roots[!boundError ? 0 : numRoots++] = rootFinder.findClosed(coef, deriv, xb, x1, yb, y1, xError);	// last interval
                    if( !boundError ) return 1;
                }
            }
            return numRoots;
	} 
        else 
        {
            if(IsDifferentSign(y0,y1)) 
            {
                roots[0] = rootFinder.findClosed( coef, deriv, x0, x1, y0, y1, xError);
                return 1;
            }
            return 0;
	}
    }

    @Override
    public int roots(float[] roots, float[] coef, float xError) 
    {
        if(coef[3] != 0) 
        {
            float a   = coef[3]*3;
            float b_2 = coef[2];
            float c   = coef[1];

            float deriv[] = { c, 2*b_2, a, 0 };

            float delta_4 = b_2*b_2 - a*c;

            if ( delta_4 > 0 ) 
            {
                float d_2 = (float) sqrt( delta_4 );
                float q = - ( b_2 + MultSign( d_2, b_2 ) );
                float rv0 = q / a;
                float rv1 = c / q;
                float xa = min( rv0, rv1 );
                float xb = max( rv0, rv1 );

                float ya = eval( coef, xa );
                float yb = eval( coef, xb );

                if (!IsDifferentSign(coef[3],ya))
                {
                    roots[0] = rootFinder.findOpenMin( coef, deriv, xa, ya, xError );
                    if ( !boundError ) {
                        if (IsDifferentSign(ya,yb))
                        {
                            float defPoly[] = new float[4];
                            deflate( defPoly, coef, roots[0] );

                            //we can't get sub array of original reference in java
                            float[] newroots = Arrays.copyOfRange(roots, 1, roots.length);
                            int returnvalue = new PolynomialQuadratic(boundError).roots(newroots, defPoly, xError) + 1;
                            System.arraycopy(newroots, 0, roots, 1, newroots.length);
                            return returnvalue;                     
                            //return QuadraticRoots( roots+1, defPoly ) + 1;
                        }
                    } 
                    else {
                        if ( IsDifferentSign(ya,yb) ) {
                            roots[1] = rootFinder.findClosed( coef, deriv, xa, xb, ya, yb, xError );
                            roots[2] = rootFinder.findOpenMax( coef, deriv, xb, yb, xError );
                            return 3;
                        }
                    }
                } 
                else 
                {                    
                    roots[0] = rootFinder.findOpenMax( coef, deriv, xb, yb, xError );
                }
                return 1;

            } else {
                    float x_inf = - b_2 / a;
                    float y_inf = eval( coef, x_inf );
                    if(IsDifferentSign(coef[3],y_inf) ) 
                    {
                        roots[0] = rootFinder.findOpenMax( coef, deriv, x_inf, y_inf, xError );
                    } else 
                    {
                        roots[0] = rootFinder.findOpenMin( coef, deriv, x_inf, y_inf, xError );
                    }
                    return 1;
            }
	} else 
            return new PolynomialQuadratic(boundError).roots( roots, coef, xError);
    }

    @Override
    public boolean firstRoot(float[] roots, float[] coef, float x0, float x1, float xError) {
        float y0 = eval( coef, x0 );
	float y1 = eval( coef, x1 );

	float a   = coef[3]*3;
	float b_2 = coef[2];
	float c   = coef[1];

	float deriv[] = { c, 2*b_2, a, 0 };

	float delta_4 = b_2*b_2 - a*c;

	if ( delta_4 > 0 ) {
		float d_2 = (float) sqrt( delta_4 );
		float q = - ( b_2 + MultSign( d_2, b_2 ) );
		float rv0 = q / a;
		float rv1 = c / q;
		float xa = min( rv0, rv1 );
		float xb = max( rv0, rv1 );

		if ( IsDifferentSign(y0,y1) ) {
			if ( xa >= x1 || xb <= x0 || ( xa <= x0 && xb >= x1 ) ) {	// first, last, or middle interval only
				roots[0] = rootFinder.findClosed( coef, deriv, x0, x1, y0, y1, xError );	// first/last interval
				return true;
			}
		} else {
			if ( ( xa >= x1 || xb <= x0 ) || ( xa <= x0 && xb >= x1 ) ) return false;
		}

		if ( xa > x0 ) {
			float ya = eval( coef, xa );
			if ( IsDifferentSign(y0,ya) ) {
				roots[0] = rootFinder.findClosed( coef, deriv, x0, xa, y0, ya, xError );	// first interval
				return true;
			}
			if ( xb < x1 ) {
				float yb = eval( coef, xb );
				if ( IsDifferentSign(ya,yb) ) {
					roots[0] = rootFinder.findClosed( coef, deriv, xa, xb, ya, yb, xError );
					return true;
				}
				if ( IsDifferentSign(yb,y1) ) {
					roots[0] = rootFinder.findClosed( coef, deriv, xb, x1, yb, y1, xError );	// last interval
					return true;
				}
			} else {
				if ( IsDifferentSign(ya,y1) ) {
					roots[0] = rootFinder.findClosed( coef, deriv, xa, x1, ya, y1, xError );
					return true;
				}
			}
		} else {
			float yb = eval( coef, xb );
			if ( IsDifferentSign(y0,yb) ) {
				roots[0] = rootFinder.findClosed( coef, deriv, x0, xb, y0, yb, xError );
				return true;
			}
			if ( IsDifferentSign(yb,y1) ) {
				roots[0] = rootFinder.findClosed( coef, deriv, xb, x1, yb, y1, xError );	// last interval
				return true;
			}
		}

	} else {
		if ( IsDifferentSign(y0,y1) ) {
			roots[0] = rootFinder.findClosed( coef, deriv, x0, x1, y0, y1, xError );
			return true;
		}
	}
	return false;
    }

    @Override
    public boolean firstRoot(float[] roots, float[] coef, float xError) {
        if(coef[3] != 0) 
        {
            float a   = coef[3]*3;
            float b_2 = coef[2];
            float c   = coef[1];

            float deriv[] = { c, 2*b_2, a, 0 };

            float delta_4 = b_2*b_2 - a*c;

            if(delta_4 > 0) 
            {
                float d_2 = (float) sqrt( delta_4 );
                float q = - ( b_2 + MultSign( d_2, b_2 ) );
                float rv0 = q / a;
                float rv1 = c / q;
                float xa = min( rv0, rv1 );
                float xb = max( rv0, rv1 );

                float ya = eval( coef, xa );
                if(!IsDifferentSign(coef[3],ya)) 
                {
                    roots[0] = rootFinder.findOpenMin( coef, deriv, xa, ya, xError );
                } 
                else 
                {
                    float yb = eval( coef, xb );
                    roots[0] = rootFinder.findOpenMax( coef, deriv, xb, yb, xError );
                }
            } 
            else 
            {
                float x_inf = - b_2 / a;
                float y_inf = eval( coef, x_inf );
                if(IsDifferentSign(coef[3],y_inf)) 
                {
                    roots[0] = rootFinder.findOpenMax( coef, deriv, x_inf, y_inf, xError );
                } 
                else 
                {
                    roots[0] = rootFinder.findOpenMin( coef, deriv, x_inf, y_inf, xError );
                }
            }
            return true;
	} 
        else 
            return toBoolean(new PolynomialQuadratic(boundError).roots( roots, coef, xError ));
    }

    @Override
    public boolean hasRoot(float[] coef, float x0, float x1, float xError) {
        float y0 = eval( coef, x0 );
	float y1 = eval( coef, x1 );
	if (IsDifferentSign(y0,y1)) 
            return true;

	float a   = coef[3]*3;
	float b_2 = coef[2];
	float c   = coef[1];

	float delta_4 = b_2*b_2 - a*c;

	if(delta_4 > 0) {
            float d_2 = (float) sqrt( delta_4 );
            float q = - ( b_2 + MultSign( d_2, b_2 ) );
            float rv0 = q / a;
            float rv1 = c / q;
            float xa = min( rv0, rv1 );
            float xb = max( rv0, rv1 );

            if ( ( xa >= x1 || xb <= x0 ) || ( xa <= x0 && xb >= x1 ) ) return false;

            if ( xa > x0 ) {
                float ya = eval( coef, xa );
                if ( IsDifferentSign(y0,ya) ) return true;
                if ( xb < x1 ) {
                    float yb = eval( coef, xb );
                    if ( IsDifferentSign(y0,yb) ) return true;
                }
            } 
            else 
            {
                float yb = eval( coef, xb );
                if(IsDifferentSign(y0,yb)) 
                    return true;
            }
	}
	return false;
    }

    @Override
    public boolean hasRoot(float[] coef, float xError) {
        return hasRoot(coef, -Float.MAX_VALUE, Float.MAX_VALUE, xError);
    }

    @Override
    public boolean forEachRoot(RootCallBackFloat callback, float[] coef, float x0, float x1, float xError) 
    {
        float y0 = eval(coef, x0);
	float y1 = eval(coef, x1);

	float a   = coef[3]*3;
	float b_2 = coef[2];
	float c   = coef[1];

	float deriv[] ={ c, 2*b_2, a, 0 };

	float delta_4 = b_2*b_2 - a*c;

	if(delta_4 > 0 )
        {
            float d_2 = (float) sqrt( delta_4 );
            float t = - ( b_2 + MultSign( d_2, b_2 ) );
            float rv0 = t / a;
            float rv1 = c / t;
            float xa = min( rv0, rv1 );
            float xb = max( rv0, rv1 );

            if ( xa >= x1 || xb <= x0 ) 
            {
                if ( IsDifferentSign(y0,y1) ) 
                {
                    if (callback.call(rootFinder.findClosed( coef, deriv, x0, x1, y0, y1, xError))) 
                        return true;	// first/last interval
                }
            } 
            else if ( xa <= x0 && xb >= x1 ) 
            {
                if ( IsDifferentSign(y0,y1) ) 
                {
                    if (callback.call(rootFinder.findClosed( coef, deriv, x0, x1, y0, y1, xError)))
                        return true;
                }
            } 
            else if ( xa > x0 )
            {
                float ya = eval(coef, xa);
                if (IsDifferentSign(y0,ya) ) 
                {
                    if(callback.call(rootFinder.findClosed( coef, deriv, x0, xa, y0, ya, xError))) 
                        return true;	// first interval
                }
                if ( xb < x1 ) {
                    float yb = eval(coef, xb);
                    if(IsDifferentSign(ya,yb)) 
                    {
                        if(callback.call(rootFinder.findClosed(coef, deriv, xa, xb, ya, yb, xError))) 
                            return true;
                    }
                    if (IsDifferentSign(yb,y1)) {
                        if (callback.call(rootFinder.findClosed(coef, deriv, xb, x1, yb, y1, xError))) 
                            return true;	// last interval
                    }
                } 
                else 
                {
                    if (IsDifferentSign(ya,y1)) 
                    {
                        if(callback.call(rootFinder.findClosed(coef, deriv, xa, x1, ya, y1, xError))) 
                            return true;
                    }
                }
            } 
            else
            {
                float yb = eval(coef, xb);
                if(IsDifferentSign(y0,yb)) 
                {
                    if(callback.call(rootFinder.findClosed(coef, deriv, x0, xb, y0, yb, xError))) 
                        return true;
                }
                if(IsDifferentSign(yb,y1)) 
                {
                    if(callback.call(rootFinder.findClosed(coef, deriv, xb, x1, yb, y1, xError))) 
                        return true;	// last interval
                }
            }
	} 
        else 
        {
            if(IsDifferentSign(y0,y1)) 
            {
                if(callback.call(rootFinder.findClosed(coef, deriv, x0, x1, y0, y1, xError))) 
                    return true;
            }
	}
	return false;
    }

    @Override
    public boolean forEachRoot(RootCallBackFloat callback, float[] coef, float xError) {
        if ( coef[3] != 0 ) 
        {
            float a   = coef[3]*3;
            float b_2 = coef[2];
            float c   = coef[1];

            float deriv[] = { c, 2*b_2, a, 0 };

            float delta_4 = b_2*b_2 - a*c;

            if(delta_4 > 0) 
            {
                float d_2 = (float) sqrt(delta_4);
                float q = - (b_2 + MultSign(d_2, b_2));
                float rv0 = q / a;
                float rv1 = c / q;
                float xa = min( rv0, rv1);
                float xb = max( rv0, rv1);

                float ya = eval(coef, xa);
                float yb = eval(coef, xb);

                if(!IsDifferentSign(coef[3],ya)) 
                {
                    float root;
                    root = rootFinder.findOpenMin(coef, deriv, xa, ya, xError);
                    if (callback.call(root) ) 
                        return true;
                    if(!boundError) 
                    {
                        if(IsDifferentSign(ya,yb))
                        {
                            float defPoly[] = new float[4];
                            deflate(defPoly, coef, root);
                            return new PolynomialQuadratic().forEachRoot(callback, defPoly, xError);
                        }
                    } 
                    else 
                    {
                        if ( IsDifferentSign(ya,yb) ) {
                                if(callback.call(rootFinder.findClosed(coef, deriv, xa, xb, ya, yb, xError))) 
                                    return true;
                                return callback.call(rootFinder.findOpenMax(coef, deriv, xb, yb, xError));
                        }
                    }
                } 
                else 
                {
                    if (callback.call(rootFinder.findOpenMax(coef, deriv, xb, yb, xError))) 
                        return true;
                }
            } 
            else 
            {
                float x_inf = - b_2 / a;
                float y_inf = eval(coef, x_inf);
                if (IsDifferentSign(coef[3],y_inf)) 
                {
                    if(callback.call(rootFinder.findOpenMax(coef, deriv, x_inf, y_inf, xError))) 
                        return true;
                } 
                else 
                {
                    if (callback.call(rootFinder.findOpenMin(coef, deriv, x_inf, y_inf, xError))) 
                        return true;
                }
            }
            return false;
	} 
        else 
            return new PolynomialQuadratic().forEachRoot(callback, coef, xError );
    }
    
}
