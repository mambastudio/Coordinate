/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import coordinate.shapes.TriangleShape;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author user
 */
public class Tri implements TriangleShape<Point3f, Vector3f, Ray, BBox>  {
    
    Point3f p1, p2, p3;
    
    private final Optional<Vector3f> n1;
    private final Optional<Vector3f> n2;
    private final Optional<Vector3f> n3;
    
    public Tri(Point3f p1, Point3f p2, Point3f p3)
    {
        this.p1 = Objects.requireNonNull(p1);
        this.p2 = Objects.requireNonNull(p2);
        this.p3 = Objects.requireNonNull(p3);
        
        this.n1 = Optional.empty();
        this.n2 = Optional.empty();
        this.n3 = Optional.empty();
    }
    
    public Tri(Point3f p1, Point3f p2, Point3f p3, Vector3f n1, Vector3f n2, Vector3f n3)
    {
        this.p1 = Objects.requireNonNull(p1);
        this.p2 = Objects.requireNonNull(p2);
        this.p3 = Objects.requireNonNull(p3);
        
        this.n1 = Optional.of(n1);
        this.n2 = Optional.of(n2);
        this.n3 = Optional.of(n3);
    }

    @Override
    public Vector3f e1() {
        return Point3f.sub(p2, p1);
    }

    @Override
    public Vector3f e2() {
        return Point3f.sub(p3, p1);
    }
    
    public Point3f v0()
    {    
        return p1;
    }
    
    @Override
    public Point3f p1() {
        return p1;
    }

    @Override
    public Point3f p2() {
        return p2;
    }

    @Override
    public Point3f p3() {
        return p3;
    }

    @Override
    public Optional<Vector3f> n1() {
        return n1;
    }

    @Override
    public Optional<Vector3f> n2() {
        return n2;
    }

    @Override
    public Optional<Vector3f> n3() {
        return n3;
    }
    
    /// Packs the normal components into a float3 structure.
    public Vector3f normal() { 
        return n(); 
    }
   
    @Override
    public BBox getBound() {
        BBox box = new BBox();
        box.include(p1, p2, p3);
        return box;
    }
}
