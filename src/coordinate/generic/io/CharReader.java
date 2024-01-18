/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.generic.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmburu
 */
public class CharReader {
    BufferedReader reader;
    public CharReader(Path path)
    {
        try {
            reader = Files.newBufferedReader(path);
        } catch (IOException ex) {
            Logger.getLogger(CharReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public char toChar(int v)
    {
        return (char)v;
    }
    
    private boolean isNewLine(char c)
    {        
        return (c == '\r' || c == '\n' || c == '\0');
    }  
    
    private boolean isSpace(char c)
    {
        return (c == ' ' || c == '\t');
    }
    
    private boolean isAllSpace(char c)
    {
        return isNewLine(c) || isSpace(c);
    }
    
    private boolean isDigit(char c)
    {
        return Character.isDigit(c);
    }
    
    private boolean tryParseDouble(double[] result) throws IOException
    {
        if (!hasNext()) {
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

        // How many characters were read in a loop.
        int read = 0;
        // Tells whether a loop terminated due to reaching s_end.
        boolean end_not_reached;

        /*
                BEGIN PARSING.
        */
        
        // Find out what sign we've got.
        if (peekNext() == '+' || peekNext() == '-') {
          sign = peekNext();
          skipNext();
        } else if (Character.isDigit(peekNext())) { /* Pass through. */
        } else {
          return false;
        }
        
        // Read the integer part.
        end_not_reached = hasNext();
        while (end_not_reached && Character.isDigit(peekNext())) {
          mantissa *= 10;
          mantissa += (int)(peekNext() - 0x30);
          skipNext();
          read++;
          end_not_reached = hasNext();
        }

        // We must make sure we actually got something.
        if (read == 0) 
            return false;
        // We allow numbers of form "#", "###" etc.
        if (!end_not_reached) 
            return assemble(mantissa, exponent, sign, result);
        
        // Read the decimal part.
        switch (peekNext()) {
            case '.':
                skipNext();
                read = 1;
                end_not_reached = hasNext();
                while (end_not_reached && Character.isDigit(peekNext())) {
                    // pow(10.0, -read)
                    double frac_value = 1.0;
                    for (int f = 0; f < read; f++) {
                        frac_value *= 0.1;
                    }
                    mantissa += (int)(peekNext() - 0x30) * frac_value;
                    read++;
                    skipNext();
                    end_not_reached = hasNext();
                }     break;
            case 'e':
            case 'E':
                break;
            default:
                return assemble(mantissa, exponent, sign, result);
        }
        
        if (!end_not_reached) 
            return assemble(mantissa, exponent, sign, result);

        // Read the exponent part.
        if (peekNext() == 'e' || peekNext() == 'E') {
          skipNext();
          // Figure out if a sign is present and if it is.
          end_not_reached = hasNext();
          if (end_not_reached && (peekNext() == '+' || peekNext() == '-')) {
            exp_sign = peekNext();
            skipNext();
          } else if (Character.isDigit(peekNext())) { /* Pass through. */
          } else {
            // Empty E is not allowed.
            return false;
          }

          read = 0;
          end_not_reached = hasNext();
          while (end_not_reached && Character.isDigit(peekNext())) {
            exponent *= 10;
            exponent += (int)(peekNext() - 0x30);
            skipNext();
            read++;
            end_not_reached = hasNext();
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
    
    public void skipNext() throws IOException
    {
        reader.read();
    }
        
    public void skipSpaceAndBeyond(char c) throws IOException
    {
        while(hasNext()) 
        {
            if(isNext(c))
                skipNext();
            else if(isAllSpace(peekNext()))
                skipNext();
            else
                break;
        }
    }
        
    public void skipSpace() throws IOException
    {
        while(hasNext())
        {
            if(!isAllSpace(peekNext()))
                break;
            skipNext();             
        }
    }
    
    public boolean isNext(char c) throws IOException
    {
        reader.mark(1);
        boolean isNext = false;
        if(hasNext() && nextChar() == c)
            isNext = true;
        reader.reset();
        return isNext;
    }
    
    public boolean isNextDigit() throws IOException
    {
        reader.mark(1);
        boolean isNext = false;
        if(hasNext() && isDigit(nextChar()))
            isNext = true;
        reader.reset();
        return isNext;
    }
    
    public boolean isNextNewLine() throws IOException
    {
        reader.mark(1);
        boolean isNext = false;
        if(hasNext() && isNewLine(nextChar()))
            isNext = true;
        reader.reset();
        return isNext;
    }
    
    public boolean isNextAllSpace() throws IOException
    {
        reader.mark(1);
        boolean isNext = false;
        if(hasNext() && isAllSpace(nextChar()))
            isNext = true;
        reader.reset();
        return isNext;
    }
    
    public boolean hasNext() throws IOException
    {
        reader.mark(1);
        int i = reader.read();
        reader.reset();
        return i > -1;
    }
        
    public char nextChar() throws IOException
    {
        return toChar(reader.read());
    }
    
    public double nextDouble() throws IOException
    {
        double[] value = new double[1];
        boolean success = tryParseDouble(value);
        if(!success)
            throw new UnsupportedOperationException("double parse failed");
        return value[0];
    }
        
    public char peekNext() throws IOException
    {
        reader.mark(1);
        int i = reader.read();
        char c = toChar(i);
        reader.reset();
        return c;
    }    
        
}
