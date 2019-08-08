/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Test2 {
    public static void main(String... args)
    {
        readBufferMapped("C:\\Users\\user\\Documents\\Scene3d\\powerplant\\powerplant.obj");
    }
    
    public static void readBuffer(String uri)
    {
        Path path = new File(uri).toPath();
        try {
            BufferedReader bf = Files.newBufferedReader(path, Charset.defaultCharset());
            String str = null;
            while((str = bf.readLine()) != null)
            {
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void readBufferMapped(String uri)
    {
        RandomAccessFile aFile;
        try {
            aFile = new RandomAccessFile
                        (uri, "r");
            FileChannel inChannel = aFile.getChannel();
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            buffer.load(); 
            for (int i = 0; i < buffer.limit(); i++)
            {
                
            }
            buffer.clear(); // do something with the data and clear/compact it.
            inChannel.close();
            aFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
