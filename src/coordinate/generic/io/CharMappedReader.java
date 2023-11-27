/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.nio.ch.DirectBuffer;

/**
 *
 * @author user
 * 
 * 
 * https://github.com/tinyobjloader/tinyobjloader/blob/master/experimental/tinyobj_loader_opt.h
 * 
 */
public class CharMappedReader {
    private FileChannel channel;
    protected MappedByteBuffer buffer;
    private RandomAccessFile rfile;
       
    public CharMappedReader(String directoryPath, String fileName)
    {
        Path path = FileSystems.getDefault().getPath(directoryPath, fileName);
        try {
            rfile = new RandomAccessFile(path.toFile(), "rw");  
            channel = rfile.getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, rfile.length());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        }               
    }
    
    public CharMappedReader(URI uri)
    {
        File file = new File(uri);
        Path path = file.toPath();
        
        try {
            rfile = new RandomAccessFile(path.toFile(), "rw");  
            channel = rfile.getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StringMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
    }
    
    public CharMappedReader(File file)
    {
        this(file.toURI());
    }
    
    public void rewind(){
        buffer.rewind();
    }
    
    public long length()
    {
        try {
            return rfile.length();
        } catch (IOException ex) {
            Logger.getLogger(CharMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }
    
    //DANGEROUS - ONLY WORKS IN JDK 1.8
    //https://stackoverflow.com/questions/2972986/how-to-unmap-a-file-from-memory-mapped-using-filechannel-in-java
    //Other solutions exist in JDK 1.9 and above
    private void unmap()
    {
       sun.misc.Cleaner cleaner = ((DirectBuffer) buffer).cleaner();
       cleaner.clean();
    }
    
    public void close()
    {
        try {
          
            
            channel.force(false);  // doesn't help
            channel.close();       // doesn't help            
            rfile.close();           // try to make sure that this thing is closed!!!!!
            unmap();
            
        } catch (IOException ex) {
            Logger.getLogger(CharMappedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public char getChar()
    {
        if(buffer.hasRemaining())
                return (char) buffer.get();
            else
                return 0;
    }
       
    public String getToken()
    {
        StringBuilder builder = new StringBuilder();
        if(goToStartChar())
            while(hasRemaining() && !isSpace(peekChar()))
                builder.append(getChar()); 
        if(builder.length()>0)
            return builder.toString();
        else 
            return null;
    }
    
    public boolean hasNextToken()
    {
        return peekToken() != null;
    }
    
    //expensive.. try to avoid it
    public String peekToken()
    {
        int previousPos = buffer.position();
        String string = getToken();        
        buffer.position(previousPos);
        return string;
    }
    
    public void skipTokens(int size)
    {
        if(size > 0)
            for(int i = 0; i<size; i++)
                getToken();
    }
    
   
    public boolean isCurrent(String string)
    {        
        int previousPos = buffer.position();        
        char c; 
        
            
        for(int i = 0; i<string.length(); i++)
        {
            c = getChar();   
            if(string.charAt(i) != c)
            {
                buffer.position(previousPos);
                return false;
            }   
        }        
        buffer.position(previousPos);
        return true;
        
    }
    
    public boolean isCurrentIsolated(String string)
    {        
        int previousPos = buffer.position();        
        char c; 
        
        for(int i = 0; i<string.length(); i++)
        {
            c = getChar();   
            if(string.charAt(i) != c)
            {
                buffer.position(previousPos);
                return false;
            }   
        }     
        if(!isSpace(getChar())) //if there is no space after
        {
            buffer.position(previousPos);
            return false;
        }
        
        buffer.position(previousPos);
        return true;
        
    }
    
    public boolean isSpace(char c)
    {
        return (Character.isWhitespace(c) || c == '/');
    }
    
    public char peekChar()
    {         
        int position = buffer.position();
        char c = getChar();
        buffer.position(position);
        return c;
    }
    
    // http://stackoverflow.com/questions/5710091/how-does-atoi-function-in-c-work
    public boolean parseInteger(int s_end, int[] result) {          
        char c = getChar();
        if (buffer.position() > s_end){ 
            return false;
        }
        int value = 0;
        int sign = 1;
        boolean end_not_reached;
        
        int readCountSuccess = 0;

        if (c == '+' || c == '-') {
            if (c == '-') sign = -1;
                c = getChar();
        }
        
        end_not_reached = buffer.position() <= s_end;
        
        while (end_not_reached && Character.isDigit(c)) {  // isdigit(*c)
            value *= 10;
            value += (int)(c - '0');
            c = getChar();
            readCountSuccess++;
            end_not_reached = buffer.position() <= s_end;
        }
        if(readCountSuccess != 0)
            result[0] = value * sign;
        return readCountSuccess != 0;
    }
            
    public void goToStartDigit()
    {
        
        char c;
        
        if(buffer.hasRemaining())
            c = (char)buffer.get();
        else
            return;
                
        while(isSpace(c) || !Character.isDigit(c))
        {            
            if((c == '-') || (c == '+'))
                if(Character.isDigit(peekChar()))
                {                    
                    goBack(1);
                    return;
                }
            if(buffer.hasRemaining())
                c = (char)buffer.get();
            else
                return;
        }  
        goBack(1);
    }
    
    public boolean goToStartChar()
    {
        while(true)
        {            
            if(buffer.hasRemaining())
            {
                char c = peekChar();
                if(!(isSpace(c)))
                {                    
                    //go back one step                   
                    return true;
                }  
            }
            else
                return false;
            
            //proceed to next
            goNextPosition();
        }  
         
    }
    
    private void goBack(int step)
    {
        if(step<=0)
            return;
        int position = buffer.position();
        buffer.position(position - step);
    }
    
    public void goNextPosition()
    {
        if(hasRemaining())
            buffer.position(buffer.position()+1);
    }
                
    public boolean hasRemaining()
    {
        return buffer.hasRemaining();
    }
    
    public boolean isNextEndOfLine()
    {
        int previousPosition = buffer.position();
        char c  = getChar();
        boolean isEnd = isEndOfLine(c);
        buffer.position(previousPosition);
        return isEnd;
    }
    
    protected boolean isEndOfLine(char c)
    {        
        char pc = peekChar();
        if (c == 0) //'\0' also has no remaining      
            return true;        
        if (c == '\n') // this includes \r\n
            return true;        
        if (c == '\r') 
            if (pc != '\n')   // detect only \r case
                return true;
        return false;
    }
    
    public void goToEndLine()
    {
        while(true)
        {   
            char c  = getChar();
            char pc = peekChar();             
            if (c == 0) //'\0'
                return;
            if (c == '\n') // this includes \r\n            
                return;            
            if (c == '\r') 
                if (pc != '\n')   // detect only \r case
                {
                    getChar(); //read away \n
                    return;
                }
        }
        
    }
    
    public void goUntilSpace()
    {
        while(true)
        {
            char c  = getChar();            
            if (c == 0) //'\0'
                return;
            if (isSpace(c))         
                return;         
        }     
    }
    
    public int getUntilSpacePosition()
    {
        int previousPosition = buffer.position();
        goUntilSpace();
        int position = buffer.position();
        buffer.position(previousPosition);
        return position;
    }
    
    public int getEndOfLinePosition()
    {
        int previousPosition = buffer.position();
        goToEndLine();     
        int position = buffer.position();
        buffer.position(previousPosition);
        return position;
    }
    
    public char getEndOfLineChar()
    {
        int previousPos = buffer.position();
        goToEndLine();
        goBack(1);
        char c = getChar();
        buffer.position(previousPos);
        return c;
    }
    
    public boolean parseDouble(int s_end, double[] result)
    {
        char s = getChar();
        if (buffer.position() > s_end){ 
            return false;
        }
        double mantissa = 0.0;
        // This exponent is base 2 rather than 10.
        // However the exponent we parse is supposed to be one of ten,
        // thus we must take care to convert the exponent/and or the
        // mantissa to a * 2^E, where a is the mantissa and E is the
        // exponent.
        // To get the final double we will use ldexp, it requires the
        // exponent to be in base 2.
        int exponent = 0;
        
        // NOTE: THESE MUST BE DECLARED HERE SINCE WE ARE NOT ALLOWED
        // TO JUMP OVER DEFINITIONS.
        char sign = '+';
        char exp_sign = '+';
        char curr = s;

        // How many characters were read in a loop.
        int read = 0;
        // Tells whether a loop terminated due to reaching s_end.
        boolean end_not_reached;

        /*
                BEGIN PARSING.
        */
        // Find out what sign we've got.
        if (curr == '+' || curr == '-') {
          sign = curr;
          curr = getChar();
        } 
        else if (Character.isDigit(curr)) { /* Pass through. */
        } 
        else if(curr == 'n')
        {
            int position = buffer.position();
            char a = getChar();
            char n = getChar();
            
            if(a == 'a' && n == 'n')
            {
                result[0] = 0;
                return true;
            }            
            buffer.position(position);       
        }
        else {
          return false;
        }
        
        // Read the integer part.
        end_not_reached = (buffer.position() <= s_end);
        while (end_not_reached && Character.isDigit(curr)) {
          mantissa *= 10;
          mantissa += (int)(curr - 0x30);
          curr = getChar();
          read++;
          end_not_reached = (buffer.position() <= s_end);
        }

        // We must make sure we actually got something.
        if (read == 0) return false;
        // We allow numbers of form "#", "###" etc.
        if (!end_not_reached) return assemble(mantissa, exponent, sign, result);

        // Read the decimal part.
        switch (curr) {
            case '.':
                curr = getChar();
                read = 1;
                end_not_reached = (buffer.position() <= s_end);
                while (end_not_reached && Character.isDigit(curr)) {
                    // pow(10.0, -read)
                    double frac_value = 1.0;
                    for (int f = 0; f < read; f++) {
                        frac_value *= 0.1;
                    }
                    mantissa += (int)(curr - 0x30) * frac_value;
                    read++;
                    curr = getChar();
                    end_not_reached = (buffer.position() <= s_end);
                }   break;
            case 'e':
            case 'E':
                break;
            default:
                return assemble(mantissa, exponent, sign, result);
        }
        
        if (!end_not_reached) 
        {            
            return assemble(mantissa, exponent, sign, result);
        }
        
        // Read the exponent part.
        if (curr == 'e' || curr == 'E') {
            curr = getChar();
            // Figure out if a sign is present and if it is.
            end_not_reached = (buffer.position() <= s_end);
            if (end_not_reached && (curr == '+' || curr == '-')) {
                exp_sign = curr;
                curr = getChar();
            } else if (Character.isDigit(curr)) { /* Pass through. */
            } else {
                // Empty E is not allowed.
                return false;
            }

            read = 0;
            end_not_reached = (buffer.position() <= s_end);
            while (end_not_reached && Character.isDigit(curr)) {
                exponent *= 10;
                exponent += (int)(curr - 0x30);
                curr = getChar();
                read++;
                end_not_reached = (buffer.position() <= s_end);
            }
            exponent *= (exp_sign == '+' ? 1 : -1);
            if (read == 0) 
                return false;
        }
        return assemble(mantissa, exponent, sign, result);
    }
    
    private boolean assemble(double mantissa, int exponent, char sign, double[] result)
    {
        result[0] = (sign == '+' ? 1 : -1) *
                (exponent != 0 ? Math.scalb(mantissa * Math.pow(5.0, exponent), exponent)
                : mantissa);
        return true;
    }
    
    public int position()
    {
        return buffer.position();
    }
}
