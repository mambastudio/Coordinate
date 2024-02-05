/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic;

import coordinate.utility.StringParser;
import coordinate.utility.Timer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author user
 */
public class Test9 {
    public static void main(String... args)
    {
        String str = "84484779";
        AtomicInteger value = new AtomicInteger();
        Timer timer1 = Timer.timeThis(()->{
            value.set(parse_eight_digits(str));
        });
        System.out.println(value+ " " +timer1.toString());
        
        Timer timer2 = Timer.timeThis(()->{
            value.set(parse_eight_digits(str));
        });
        System.out.println(value+ " " +timer2.toString());
        Timer timer3 = Timer.timeThis(()->{
            value.set(parse_eight_digits(str));
        });
        System.out.println(value+ " " +timer3.toString());
        Timer timer4 = Timer.timeThis(()->{
            value.set(parse_eight_digits(str));
        });
        System.out.println(value+ " " +timer4.toString());
        
        Timer timer5 = Timer.timeThis(()->{
            value.set(Integer.parseInt(str));
        });
        System.out.println(value+ " " +timer5.toString());
        
        Timer timer6 = Timer.timeThis(()->{
            value.set(parseEightDigitsSwar(str));
        });
        System.out.println(value+ " " +timer6.toString());
        Timer timer7 = Timer.timeThis(()->{
            value.set(parseEightDigitsSwar(str));
        });
        System.out.println(value+ " " +timer7.toString());
        
    }
    
    public static int parse_eight_digits(String chars) {
        int x = chars.charAt(0) - '0';
        for (int j = 1; j < 8; j++)
            x = x * 10 + (chars.charAt(j) - '0');
        return x;
    }
    
    public static int parseEightDigitsSwar(String chars) {
        long val =  ((long) chars.charAt(0)) |
                    ((long) chars.charAt(1) << 8) |
                    ((long) chars.charAt(2) << 16) |
                    ((long) chars.charAt(3) << 24) |
                    ((long) chars.charAt(4) << 32) |
                    ((long) chars.charAt(5) << 40) |
                    ((long) chars.charAt(6) << 48) |
                    ((long) chars.charAt(7) << 56);
        
        long mask = 0x000000FF000000FFL;
        long mul1 = 0x000F424000000064L; // 100 + (1000000ULL << 32)
        long mul2 = 0x0000271000000001L; // 1 + (10000ULL << 32)
        val -= 0x3030303030303030L;
        val = (val * 10) + (val >> 8); // val = (val * 2561) >> 8;
        val = (((val & mask) * mul1) + (((val >> 16) & mask) * mul2)) >> 32;
        return (int) val;
    }
    
    public static int parse_eight_digits_unrolled(String chars) {
        int x = 10000000 * (chars.charAt(0)) + 1000000 * (chars.charAt(1)) +
                     100000 * (chars.charAt(2)) + 10000 * (chars.charAt(3)) + 1000 * (chars.charAt(4)) +
                     100 * (chars.charAt(5)) + 10 * (chars.charAt(6)) + (chars.charAt(7));
        return x - '0' * (1 + 10 + 100 + 1000 + 10000 + 100000 + 1000000 + 10000000);
    }
    
    public static void testStringParser()
    {
        String string = "josto is good ";
        StringParser parser = new StringParser(string);
        
        System.out.println(parser.getToken());
        System.out.println(parser.getToken());
        System.out.println(parser.getToken()); 
        
        parser.skipContinousSpace();
        System.out.println(parser.isNewLine());
    }
}
