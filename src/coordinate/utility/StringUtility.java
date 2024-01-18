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
 */
public class StringUtility {
    
    public static boolean isEndOfLine(char c)
    {        
        return (c == '\r' || c == '\n' || c == '\0');
    }   
    
    public static boolean isEndOfLine(String str, int[] pointer)
    {        
        return (pointer[0] >= str.length() || StringUtility.isEndOfLine(str.charAt(pointer[0])));
    }
    
    public static void skipSpaceAndCarriageReturn(String token, int[] pointer) {        
        while (true)
        {            
            if(pointer[0] >= token.length()) //what if it's end of line already
                break;            
            //skip the usual space
            if(Character.isWhitespace(token.charAt(pointer[0]))) 
                pointer[0]++;  
            else
                break;
        }
    }
    
    public static double parseDoubleDefault(String str, double defaultValue)
    {
        Objects.requireNonNull(str);
        double[] result = new double[1];
        boolean success = tryParseDouble(str, 0, str.length(), result);
        return success ? result[0] : defaultValue;
    }
    
    //https://github.com/tinyobjloader/tinyobjloader/blob/release/experimental/tinyobj_loader_opt.h#L503
    public static boolean tryParseDouble(String str, int s, int s_end, double[] result)
    {
        if (s >= s_end) {
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
        int curr = s;

        // How many characters were read in a loop.
        int read = 0;
        // Tells whether a loop terminated due to reaching s_end.
        boolean end_not_reached;

        /*
                BEGIN PARSING.
        */
        
        // Find out what sign we've got.
        if (str.charAt(curr) == '+' || str.charAt(curr) == '-') {
          sign = str.charAt(curr);
          curr++;
        } else if (Character.isDigit(str.charAt(curr))) { /* Pass through. */
        } else {
          return false;
        }
        
        // Read the integer part.
        end_not_reached = (curr != s_end);
        while (end_not_reached && Character.isDigit(str.charAt(curr))) {
          mantissa *= 10;
          mantissa += (int)(str.charAt(curr) - 0x30);
          curr++;
          read++;
          end_not_reached = (curr != s_end);
        }

        // We must make sure we actually got something.
        if (read == 0) 
            return false;
        // We allow numbers of form "#", "###" etc.
        if (!end_not_reached) 
            return assemble(mantissa, exponent, sign, result);
        
        // Read the decimal part.
        switch (str.charAt(curr)) {
            case '.':
                curr++;
                read = 1;
                end_not_reached = (curr != s_end);
                while (end_not_reached && Character.isDigit(str.charAt(curr))) {
                    // pow(10.0, -read)
                    double frac_value = 1.0;
                    for (int f = 0; f < read; f++) {
                        frac_value *= 0.1;
                    }
                    mantissa += (int)(str.charAt(curr) - 0x30) * frac_value;
                    read++;
                    curr++;
                    end_not_reached = (curr != s_end);
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
        if (str.charAt(curr) == 'e' || str.charAt(curr) == 'E') {
          curr++;
          // Figure out if a sign is present and if it is.
          end_not_reached = (curr != s_end);
          if (end_not_reached && (str.charAt(curr) == '+' || str.charAt(curr) == '-')) {
            exp_sign = str.charAt(curr);
            curr++;
          } else if (Character.isDigit(str.charAt(curr))) { /* Pass through. */
          } else {
            // Empty E is not allowed.
            return false;
          }

          read = 0;
          end_not_reached = (curr != s_end);
          while (end_not_reached && Character.isDigit(str.charAt(curr))) {
            exponent *= 10;
            exponent += (int)(str.charAt(curr) - 0x30);
            curr++;
            read++;
            end_not_reached = (curr != s_end);
          }
          exponent *= (exp_sign == '+' ? 1 : -1);
          if (read == 0) 
              return false;
        }
        return assemble(mantissa, exponent, sign, result);
    }
    
    //Error if number does not exists
    public static int getFirstInt(String str, int[] pointer) {
        Objects.requireNonNull(pointer);
        Objects.requireNonNull(str);
        
        if(pointer[0] >= str.length())
            throw new IndexOutOfBoundsException("start index: "+pointer[0]+ " out of bound of string length: " +str.length());
        
        int value = 0;
        int sign = 1;
        //skip until first int occurs 
        while(!(str.charAt(pointer[0]) == '+' || str.charAt(pointer[0]) == '-' || Character.isDigit(str.charAt(pointer[0]))))
        {
            pointer[0]++;
            if(pointer[0] >= str.length())
                throw new IndexOutOfBoundsException("after loop no integer found in string");
        }      
        
        //register + or -
        if ((str.charAt(pointer[0]) == '+' || str.charAt(pointer[0]) == '-')) {   
            if(pointer[0] + 1 < str.length()) //+ or - in string but not at the end
            {
                //what if the character after + or - is not digit? do a recursive call
                if(!Character.isDigit(str.charAt(pointer[0] + 1)))
                {
                    pointer[0]++;
                    return getFirstInt(str, pointer);
                }
                if (str.charAt(pointer[0]) == '-') sign = -1;
                    pointer[0]++; 
            }
            else //+ or - was found at the end of the string
                throw new IndexOutOfBoundsException("after loop no integer found in string");
        }
       
        
        while (Character.isDigit(str.charAt(pointer[0]))) {  
          value *= 10;
          value += (int)(str.charAt(pointer[0]) - '0');
          pointer[0]++;
          if(pointer[0] >= str.length())
              break;
        }
        
        return value * sign;
    }
       
    // http://stackoverflow.com/questions/5710091/how-does-atoi-function-in-c-work
    public static int my_atoi(String str, int[] endIndex) {
        int value = 0;
        int sign = 1;
        int c = 0;
        
        if (str.charAt(c) == '+' || str.charAt(c) == '-') {
          if (str.charAt(c) == '-') sign = -1;
              c++;
        }
        while (((str.charAt(c)) >= '0') && (str.charAt(c) <= '9')) {  // isdigit(*c)
          value *= 10;
          value += (int)(str.charAt(c) - '0');
          c++;
          if(c >= str.length())
              break;
        }
        
        if(endIndex != null)
            endIndex[0] += c; //endIndex is not assigned only, but also accumulated
        return value * sign;
    }
    
    private static boolean assemble(double mantissa, int exponent, char sign, double[] result)
    {
        result[0] = (sign == '+' ? 1 : -1) *
                (exponent != 0 ? Math.scalb(mantissa * Math.pow(5.0, exponent), exponent)
                : mantissa);
        return true;
    }
}
