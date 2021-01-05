/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.parser.attribute;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class MaterialT implements Serializable {
    public char[] name;    
    
    public Color4T diffuse;         //diffuse    - r, g, b, w (pad)
    public float diffuseWeight;     //diffuse    - diffuse weight
    
    public Color4T reflection;      //reflection - r, g, b, w (pad)
    public float eu, ev, ior;       //reflection - eu, ev, ior
    public boolean iorEnabled;      //reflection - transmission enabled
    
    public Color4T emitter;         //emission   - r, g, b, w (power)
    public boolean emitterEnabled;  //emission   - emission enabled
    
    public MaterialT() 
    {
        this.name = "m_default".toCharArray();
        this.diffuse = new Color4T(0.9f, 0.9f, 0.9f);
        this.diffuseWeight = 1;
        
        this.reflection = new Color4T(0.9f, 0.9f, 0.9f);
        this.eu = 60; this.ev = 30; this.ior = 1;
        this.iorEnabled = false;
                
        this.emitter = new Color4T(1, 1, 1, 1);
        this.emitterEnabled = false;
    }
    public MaterialT(float dr, float dg, float db)
    {
        this.name = "m_default".toCharArray(); 
        this.diffuse = new Color4T(dr, dg, db);
        this.diffuseWeight = 1;
        
        this.reflection = new Color4T(0.9f, 0.9f, 0.9f);
        this.eu = 60; this.ev = 30; this.ior = 1;
        this.iorEnabled = false;
                
        this.emitter = new Color4T(1, 1, 1, 5);
        this.emitterEnabled = false;
    }   
        
    public MaterialT(String name)
    {
        this.name = name.toCharArray();
        this.diffuse = new Color4T(0.9f, 0.9f, 0.9f);
        this.diffuseWeight = 1;
        
        this.reflection = new Color4T(0.9f, 0.9f, 0.9f);
        this.eu = 60; this.ev = 30; this.ior = 1;
        this.iorEnabled = false;
                
        this.emitter = new Color4T(1, 1, 1, 5);
        this.emitterEnabled = false;
    }
    public MaterialT(String name, float dr, float dg, float db)
    {
        this.name = name.toCharArray(); 
        this.diffuse = new Color4T(dr, dg, db);
        this.diffuseWeight = 1;
        
        this.reflection = new Color4T(0.9f, 0.9f, 0.9f);
        this.eu = 60; this.ev = 30; this.ior = 1;
        this.iorEnabled = false;
                
        this.emitter = new Color4T(1, 1, 1, 5);
        this.emitterEnabled = false;
    }   
    
    public MaterialT(float dr, float dg, float db, float er, float eg, float eb)
    {
        this.name = "m_default".toCharArray(); 
        this.diffuse = new Color4T(dr, dg, db);
        this.diffuseWeight = 1;
        
        this.reflection = new Color4T(0.9f, 0.9f, 0.9f);
        this.eu = 60; this.ev = 30; this.ior = 1;
        this.iorEnabled = false;
                
        this.emitter = new Color4T(er, eg, eb, 5);
        this.emitterEnabled = true;
    } 
    
     public MaterialT(String name, float dr, float dg, float db, float er, float eg, float eb)
    {
        this(dr, dg, db, er, eg, eb);
        this.name = name.toCharArray();
    } 
     
    public MaterialT(MaterialT mat)
    {
        name = mat.name;
        
        diffuse = mat.diffuse.copy();
        diffuseWeight = mat.diffuseWeight;
        
        reflection = mat.reflection.copy();
        eu = mat.eu; ev = mat.ev; ior = mat.ior;
        iorEnabled = mat.iorEnabled;
                
        emitter = mat.emitter.copy();
        emitterEnabled = mat.emitterEnabled;
     }
            
    public boolean isEmitter()
    {
        return emitterEnabled;
    }    
    
    
    
    //non binding set
    public void set(MaterialT mat)
    {
        name = mat.name;
        
        diffuse = mat.diffuse.copy();
        diffuseWeight = mat.diffuseWeight;
        
        reflection = mat.reflection.copy();
        eu = mat.eu; ev = mat.ev; ior = mat.ior;
        iorEnabled = mat.iorEnabled;
                
        emitter = mat.emitter.copy();
        emitterEnabled = mat.emitterEnabled;
    }
    
    public MaterialT copy()
    {
        MaterialT mat = new MaterialT();
        mat.name = name;
        
        mat.diffuse = diffuse.copy();
        mat.diffuseWeight = diffuseWeight;
        
        mat.reflection = reflection.copy();
        mat.eu = eu; mat.ev = ev; mat.ior = ior;
        mat.iorEnabled = iorEnabled;
                
        mat.emitter = emitter.copy();
        mat.emitterEnabled = emitterEnabled;
        
        return mat;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("name:          ").append(name).append("\n").append("\n");
        
        builder.append("diffuse:       ").append(diffuse).append("\n");
        builder.append("diffuse weight ").append(diffuseWeight).append("\n").append("\n");
        
        builder.append("reflection     ").append(reflection).append("\n");
        builder.append("eu             ").append(eu).append("\n");
        builder.append("ev             ").append(ev).append("\n");       
        builder.append("ior enabled    ").append(iorEnabled).append("\n");
        builder.append("ior            ").append(ior).append("\n").append("\n");
        
        builder.append("emitter        ").append(emitter).append("\n");
        builder.append("is emitter:    ").append(isEmitter()).append("\n");
        return builder.toString();
    }

    public String getNameString() {
        return new String(name);
    }
}
