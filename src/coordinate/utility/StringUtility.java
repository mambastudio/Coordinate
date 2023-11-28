/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

/**
 *
 * @author user
 */
public class StringUtility {
    
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
        boolean end_not_reached = false;

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

        return false;
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
        }
        
        if(endIndex != null)
            endIndex[0] = c;
        return value * sign;
    }
}
