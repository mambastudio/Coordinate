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
public class GroupT implements Serializable {
    public String name;
    public int materialIndex;
    
    public GroupT()
    {
        this.name = "g_default";
        this.materialIndex = 0;
    }
    
    public GroupT(String name)
    {
        this.name = name;
        this.materialIndex = 0;
    }
    public GroupT(String name, int materialIndex)
    {
        this.name = name; 
        this.materialIndex = materialIndex;
    }   
}
