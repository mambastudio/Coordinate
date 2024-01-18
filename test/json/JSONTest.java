/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author user
 */
public class JSONTest {
    public static void main(String... args) throws IOException
    {
        Path path = Paths.get("C:\\Users\\user\\Documents\\File Examples", "JSON-3.json");
        test(path);
    }
    
    public static void test(Path path) throws IOException
    {
        BufferedReader reader = Files.newBufferedReader(path);
        skipNextUntil(reader, '{');
        readObject(0, reader);
           
    }
    
    public static void readObject(int indent, BufferedReader reader) throws IOException
    {
        while(hasNext(reader))
        {   
            char c = nextChar(reader);
            if(c == '{')
            {               
                while(hasNext(reader))
                {
                    String key = parseKey(indent, reader);                    
                    skipNextBeyond(reader, ':'); //:              
                    String value = parseValue(indent, reader);                    
                    System.out.println(key+ " " +value);
                    if(isNext(reader, ','))
                        skipSpaceAndBeyond(reader, ',');
                    if(isNext(reader, '}'))
                        break;
                }
            }                   
        }
    }
    
    public static String parseKey(int indent, BufferedReader reader) throws IOException
    {
        skipSpace(reader);
        String key = readQuotes(reader);
        key = getSpace(indent) + key;
        skipSpace(reader);
        return key;
    }
    
    public static String parseValue(int indent, BufferedReader reader) throws IOException
    {
        skipSpace(reader);
        StringBuilder builder = new StringBuilder();
        while(hasNext(reader))
        {
            if(isNext(reader, ',') || isNext(reader, '}') || isNextNewLine(reader))
                break;            
            if(isDigit(peekNext(reader)))
            {
                double value = readNumber(reader);
                builder.append(value);
            }
            if(isNext(reader, '"'))
            {
                String value = readQuotes(reader);
                builder.append(value);
            }
            else if(isNext(reader, '{'))
            {
                readObject(indent + 5, reader);
            }
            else
                builder.append(nextChar(reader));
        }
        skipSpace(reader);
        return getSpace(indent) + builder.toString();
    }
    
    public static double readNumber(BufferedReader reader) throws IOException
    {
        skipSpaceAndBeyond(reader, ' ');
        double[] value = new double[1];
        boolean parseDouble = tryParseDouble(reader, value);
        if(!parseDouble)
            throw new UnsupportedOperationException("unable to parse double");
        skipSpaceAndBeyond(reader, ' ');
        return value[0];
    }
    
    public static void readString(BufferedReader reader) throws IOException
    {
        String string = readQuotes(reader);
        System.out.println("-" +string);
    }
    
    
    public static String readQuotes(BufferedReader reader) throws IOException
    {
        skipSpaceAndBeyond(reader, '"');
        StringBuilder builder = new StringBuilder();
        while(hasNext(reader) && !isNext(reader, '"'))        
            builder.append(nextChar(reader));   
        skipSpaceAndBeyond(reader, '"');
        return builder.toString();
    }
    
    public static void skipNext(BufferedReader reader) throws IOException
    {
        reader.read();
    }
    
    public static void skipNextBeyond(BufferedReader reader, char c) throws IOException
    {
        while(hasNext(reader) && !isNext(reader, c))       
            skipNext(reader);
        if(hasNext(reader))
            skipNext(reader);
    }
    
    public static void skipSpaceAndBeyond(BufferedReader reader, char c) throws IOException
    {
        while(hasNext(reader))
        {
            if(isNext(reader, c))
                skipNext(reader);
            else if(isAllSpace(peekNext(reader)))
                skipNext(reader);
            else
                break;
        }
    }
    
    public static void skipNextUntil(BufferedReader reader, char c) throws IOException
    {
        while(hasNext(reader) && !isNext(reader, c))       
            skipNext(reader);          
    }
    
    public static void skipSpace(BufferedReader reader) throws IOException
    {
        while(hasNext(reader))
        {
            if(!isAllSpace(peekNext(reader)))
                break;
            skipNext(reader);             
        }
    }
    
    public static boolean isNext(BufferedReader reader, char c) throws IOException
    {
        reader.mark(1);
        boolean isNext = false;
        if(hasNext(reader) && nextChar(reader) == c)
            isNext = true;
        reader.reset();
        return isNext;
    }
    
    public static boolean isNextNewLine(BufferedReader reader) throws IOException
    {
        reader.mark(1);
        boolean isNext = false;
        if(hasNext(reader) && isNewLine(nextChar(reader)))
            isNext = true;
        reader.reset();
        return isNext;
    }
    
    public static char nextChar(BufferedReader reader) throws IOException
    {
        return toChar(reader.read());
    }
    
    public static boolean hasNext(BufferedReader reader) throws IOException
    {
        reader.mark(1);
        int i = reader.read();
        reader.reset();
        return i > -1;
    }
    
    public static char peekNext(BufferedReader reader) throws IOException
    {
        reader.mark(1);
        int i = reader.read();
        char c = toChar(i);
        reader.reset();
        return c;
    }
    
    public static char toChar(int v)
    {
        return (char)v;
    }
    
    private static boolean isNewLine(char c)
    {        
        return (c == '\r' || c == '\n' || c == '\0');
    }  
    
    private static boolean isSpace(char c)
    {
        return (c == ' ' || c == '\t');
    }
    
    private static boolean isAllSpace(char c)
    {
        return isNewLine(c) || isSpace(c);
    }
    
    private static boolean isDigit(char c)
    {
        return Character.isDigit(c);
    }
    
    private static boolean tryParseDouble(BufferedReader reader, double[] result) throws IOException
    {
        if (!hasNext(reader)) {
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
        if (peekNext(reader) == '+' || peekNext(reader) == '-') {
          sign = peekNext(reader);
          skipNext(reader);
        } else if (Character.isDigit(peekNext(reader))) { /* Pass through. */
        } else {
          return false;
        }
        
        // Read the integer part.
        end_not_reached = hasNext(reader);
        while (end_not_reached && Character.isDigit(peekNext(reader))) {
          mantissa *= 10;
          mantissa += (int)(peekNext(reader) - 0x30);
          skipNext(reader);
          read++;
          end_not_reached = hasNext(reader);
        }

        // We must make sure we actually got something.
        if (read == 0) 
            return false;
        // We allow numbers of form "#", "###" etc.
        if (!end_not_reached) 
            return assemble(mantissa, exponent, sign, result);
        
        // Read the decimal part.
        switch (peekNext(reader)) {
            case '.':
                skipNext(reader);
                read = 1;
                end_not_reached = hasNext(reader);
                while (end_not_reached && Character.isDigit(peekNext(reader))) {
                    // pow(10.0, -read)
                    double frac_value = 1.0;
                    for (int f = 0; f < read; f++) {
                        frac_value *= 0.1;
                    }
                    mantissa += (int)(peekNext(reader) - 0x30) * frac_value;
                    read++;
                    skipNext(reader);
                    end_not_reached = hasNext(reader);
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
        if (peekNext(reader) == 'e' || peekNext(reader) == 'E') {
          skipNext(reader);
          // Figure out if a sign is present and if it is.
          end_not_reached = hasNext(reader);
          if (end_not_reached && (peekNext(reader) == '+' || peekNext(reader) == '-')) {
            exp_sign = peekNext(reader);
            skipNext(reader);
          } else if (Character.isDigit(peekNext(reader))) { /* Pass through. */
          } else {
            // Empty E is not allowed.
            return false;
          }

          read = 0;
          end_not_reached = hasNext(reader);
          while (end_not_reached && Character.isDigit(peekNext(reader))) {
            exponent *= 10;
            exponent += (int)(peekNext(reader) - 0x30);
            skipNext(reader);
            read++;
            end_not_reached = hasNext(reader);
          }
          exponent *= (exp_sign == '+' ? 1 : -1);
          if (read == 0) 
              return false;
        }
        return assemble(mantissa, exponent, sign, result);
    }
    
    private static boolean assemble(double mantissa, int exponent, char sign, double[] result)
    {
        result[0] = (sign == '+' ? 1 : -1) *
                (exponent != 0 ? Math.scalb(mantissa * Math.pow(5.0, exponent), exponent)
                : mantissa);
        return true;
    }
    
    public static String getSpace(int space)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<space; i++)
            builder.append(' ');
        return builder.toString();
    }
}
