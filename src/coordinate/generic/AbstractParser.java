/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic;

import java.net.URI;

/**
 *
 * @author user
 */
public interface AbstractParser {
    public void read(String uri, AbstractMesh mesh);
    public void read(URI uri, AbstractMesh mesh);
    public void readString(String string, AbstractMesh mesh);
}