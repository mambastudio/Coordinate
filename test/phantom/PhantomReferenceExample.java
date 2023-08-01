package phantom;

import coordinate.utility.Sweeper;

public class PhantomReferenceExample {
    public static void main(String[] args) {
       
        test();

        // Run explicit garbage collection (not recommended in real-world applications)
        System.gc();

        // Allow some time for the garbage collector to run
        try {
            Thread.sleep(1000); // Wait for 1 second (adjust as needed)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
    
    
    }
    
    public static void test()
    {
        // Create an object and a phantom reference to it
        int megabyte = 1024 * 1024;
        Object myObject = new byte[10 * megabyte]; // Allocate 10 MB of memory
        
        Sweeper.getSweeper().register(myObject, ()-> System.out.println("object gc"));        
    }
}