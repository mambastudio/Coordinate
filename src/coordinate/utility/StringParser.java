/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import java.util.Objects;

/**
 *
 * @author user
 * 
 * 
 * Conditional returns don't throw error
 * 
 * Good for parsing text format strings like OBJ formats 
 * 
 */
public class StringParser {
    private final String string;
    private final int[] pointer;
    
    public StringParser(String string)
    {
        Objects.requireNonNull(string);
        this.string = string;
        this.pointer = new int[1];
    }
    
    private StringParser(String string, int[] pointer)
    {
        Objects.requireNonNull(string);
        Objects.requireNonNull(pointer);
        this.string = string;
        this.pointer = pointer;
    }
    
    public void reset()
    {
        pointer[0] = 0;
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
    
    public boolean isCurrentDigit()
    {
        return (pointer[0] < string.length() && isDigit(string.charAt(pointer[0])));
    }
    
    public boolean isCurrentNumber()            
    {
        return (pointer[0] < string.length() && tryParseDouble(new double[1]));
    }
    
    public boolean isCurrentSpace()
    {
        
        return pointer[0] < string.length() && isAllSpace(currentChar());
    }
    
    public boolean isNewLine()
    {
        return (pointer[0] >= string.length() || isNewLine(string.charAt(pointer[0])));
    }
    
    public void goToEndOfLine()
    {
        pointer[0] = string.length();
    }
    
    public boolean isSpaceOrChar(char skipChar)
    {
        if(pointer[0] >= string.length()) //what if it's end of line already
            return false;
        return Character.isWhitespace(string.charAt(pointer[0])) || string.charAt(pointer[0]) == skipChar;
    }
    
    public boolean isChar(char charValue)
    {
        if(pointer[0] >= string.length()) //what if it's end of line already
            return false;
        return string.charAt(pointer[0]) == charValue;
    }
    
    public boolean isNotChar(char charValue)
    {
        return !isChar(charValue);
    }
    
    public void incrementPointer(int increment)
    {
        for(int i = 0; i<increment; i++)
            incrementPointer();
    }
    
    public void incrementPointer()
    {
        if(pointer[0] >= string.length()) //what if it's end of line already
            throw new IndexOutOfBoundsException("start index: "+pointer[0]+ " out of bound of string length: " +string.length());
        pointer[0]++;
    }
                   
    public void skipIfNotSpaceAndChar(char charValue)
    {                
        while (true)
        {            
            if(pointer[0] >= string.length()) //what if it's end of line already
                break; 
            if(!Character.isWhitespace(currentChar()) && isNotChar(charValue))
            {                
                pointer[0]++;
                continue;
            }                   
            return;
        }
    }
    
    public char currentChar()
    {
        if(pointer[0] >= string.length()) //what if it's end of line already
            throw new IndexOutOfBoundsException("start index: "+pointer[0]+ " out of bound of string length: " +string.length());
        return string.charAt(pointer[0]);
    }
    
    public void skipContinousSpace() {        
        while (true)
        {            
            if(pointer[0] >= string.length()) //what if it's end of line already
                break;            
            //skip all space
            if(isAllSpace(currentChar())) 
                pointer[0]++;  
            else
                break;
        }
    }
    
    public String getToken()
    {
        StringBuilder builder = new StringBuilder();
        skipContinousSpace();
        if(isNewLine())
            throw new IndexOutOfBoundsException("start index: "+pointer[0]+ " out of bound of string length: " +string.length());
        while(true)
        {
            if(isNewLine() || isCurrentSpace())
                break;
            builder.append(currentChar());
            incrementPointer();
        }
        return builder.toString();
    }
    
    public boolean contains(String token)
    {
        if(isNewLine())
            return false;
        return string.substring(pointer[0]).contains(token);
    }
    
    public String getRemainder()
    {
        return string.substring(pointer[0]);
    }
    
    public boolean currentIs(String token)
    {
        if(isNewLine() || token.length() > (string.length() - pointer[0]))
            return false;
        return string.substring(pointer[0], pointer[0] + token.length()).contains(token);
    }
    
    //http://stackoverflow.com/questions/5710091/how-does-atoi-function-in-c-work
    //Error if number does not exists
    public int getFirstInt() {        
        if(pointer[0] >= string.length())
            throw new IndexOutOfBoundsException("start index: "+pointer[0]+ " out of bound of string length: " +string.length());
        
        int value = 0;
        int sign = 1;
        //skip until first int occurs 
        while(!(string.charAt(pointer[0]) == '+' || string.charAt(pointer[0]) == '-' || Character.isDigit(string.charAt(pointer[0]))))
        {
            pointer[0]++;
            if(pointer[0] >= string.length())
                throw new IndexOutOfBoundsException("after loop no integer found in string");
        }      
        
        //register + or -
        if ((string.charAt(pointer[0]) == '+' || string.charAt(pointer[0]) == '-')) {   
            if(pointer[0] + 1 < string.length()) //+ or - in string but not at the end
            {
                //what if the character after + or - is not digit? do a recursive call
                if(!Character.isDigit(string.charAt(pointer[0] + 1)))
                {
                    pointer[0]++;
                    return new StringParser(string, pointer).getFirstInt();
                }
                if (string.charAt(pointer[0]) == '-') sign = -1;
                    pointer[0]++; 
            }
            else //+ or - was found at the end of the string
                throw new IndexOutOfBoundsException("after loop no integer found in string");
        }
        
        while (Character.isDigit(string.charAt(pointer[0]))) {  
          value *= 10;
          value += (int)(string.charAt(pointer[0]) - '0');
          pointer[0]++;
          if(pointer[0] >= string.length())
              break;
        }
        
        return value * sign;
    }
    
    public float getFirstFloat()
    {
        return (float)getFirstDouble();
    }
    
    public double getFirstDouble()
    {       
        if(pointer[0] >= string.length())        
            throw new IndexOutOfBoundsException("start index: "+pointer[0]+ " out of bound of string length: " +string.length());
        
        while(true)
        {
            if(currentIs("nan"))
            {
                incrementPointer(3);
                return 0;
            }
            double[] result = new double[1];
            if(tryParseDouble(result))           
                return result[0];
            pointer[0]++;
            if(pointer[0] >= string.length())
                throw new IndexOutOfBoundsException("after loop no integer found in string");
        }
    }
    
    private boolean tryParseDouble(double[] result)
    {
        if (pointer[0] >= string.length()) {
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
        if (string.charAt(pointer[0]) == '+' || string.charAt(pointer[0]) == '-') {
          sign = string.charAt(pointer[0]);
          pointer[0]++;
        } else if (Character.isDigit(string.charAt(pointer[0]))) { /* Pass through. */
        } else {
          return false;
        }
        
        // Read the integer part.
        end_not_reached = (pointer[0] != string.length());
        while (end_not_reached && Character.isDigit(string.charAt(pointer[0]))) {
          mantissa *= 10;
          mantissa += (int)(string.charAt(pointer[0]) - 0x30);
          pointer[0]++;
          read++;
          end_not_reached = (pointer[0] != string.length());
        }

        // We must make sure we actually got something.
        if (read == 0) 
            return false;
        // We allow numbers of form "#", "###" etc.
        if (!end_not_reached) 
            return assemble(mantissa, exponent, sign, result);
        
        // Read the decimal part.
        switch (string.charAt(pointer[0])) {
            case '.':
                pointer[0]++;
                read = 1;
                end_not_reached = (pointer[0] != string.length());
                while (end_not_reached && Character.isDigit(string.charAt(pointer[0]))) {
                    // pow(10.0, -read)
                    double frac_value = 1.0;
                    for (int f = 0; f < read; f++) {
                        frac_value *= 0.1;
                    }
                    mantissa += (int)(string.charAt(pointer[0]) - 0x30) * frac_value;
                    read++;
                    pointer[0]++;
                    end_not_reached = (pointer[0] != string.length());
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
        if (string.charAt(pointer[0]) == 'e' || string.charAt(pointer[0]) == 'E') {
          pointer[0]++;
          // Figure out if a sign is present and if it is.
          end_not_reached = (pointer[0] != string.length());
          if (end_not_reached && (string.charAt(pointer[0]) == '+' || string.charAt(pointer[0]) == '-')) {
            exp_sign = string.charAt(pointer[0]);
            pointer[0]++;
          } else if (Character.isDigit(string.charAt(pointer[0]))) { /* Pass through. */
          } else {
            // Empty E is not allowed.
            return false;
          }

          read = 0;
          end_not_reached = (pointer[0] != string.length());
          while (end_not_reached && Character.isDigit(string.charAt(pointer[0]))) {
            exponent *= 10;
            exponent += (int)(string.charAt(pointer[0]) - 0x30);
            pointer[0]++;
            read++;
            end_not_reached = (pointer[0] != string.length());
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
}
